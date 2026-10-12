package model;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class TesteApuracao extends TesteBase {

	private void prepararPartidaDeTresJogadores() throws JogadaInvalidaException {
		jogo = criarJogo(new Cor[] { Cor.vermelho, Cor.azul, Cor.verde }, new Herdeiro[] { Herdeiro.altani, Herdeiro.chagatai, Herdeiro.ogedei });
	}

	private int influencia(Cor cor) {
		Map<Jogador, Integer> votos = ApuracaoFinal.votosDeInfluencia(jogo.getJogadores(), mapa);
		return votos.get(jogador(cor));
	}

	private List<Jogador> vencedores() {
		return ApuracaoFinal.vencedores(jogo.getJogadores(), mapa);
	}

	private void prepararMaioriaNaRussia() throws JogadaInvalidaException {
		colocarYurt(Cor.vermelho, "A");
		colocarYurt(Cor.vermelho, "B");
		conquistarCidade(Cor.azul, "c1");
		colocarMelhorias(Cor.vermelho, Coluna.amarela, TipoMelhoria.punho, 2);
		colocarMelhorias(Cor.azul, Coluna.amarela, TipoMelhoria.punho, 1);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void jogadorComMaisYurtsRecebeATabelaDoPrimeiro() throws JogadaInvalidaException {
		prepararMaioriaNaRussia();
		assertEquals("Mais yurts com 2 melhorias na coluna amarela valem 2 votos", 2, influencia(Cor.vermelho));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void segundoComMaisYurtsRecebeATabelaDoSegundo() throws JogadaInvalidaException {
		prepararMaioriaNaRussia();
		assertEquals("O yurt na cidade conquistada conta e 1 melhoria vale 1 voto", 1, influencia(Cor.azul));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void empatadosNoPrimeiroLugarRecebemATabelaDoPrimeiro() throws JogadaInvalidaException {
		prepararPartidaDeTresJogadores();
		colocarYurt(Cor.vermelho, "D");
		colocarYurt(Cor.vermelho, "E");
		colocarYurt(Cor.azul, "D");
		conquistarCidade(Cor.azul, "c2");
		conquistarCidade(Cor.verde, "c3");
		colocarMelhorias(Cor.azul, Coluna.vermelha, TipoMelhoria.punho, 4);
		assertEquals("Empatado em 1o com 4 melhorias na coluna vermelha", 3, influencia(Cor.azul));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void empateNoPrimeiroLugarDeixaNinguemEmSegundo() throws JogadaInvalidaException {
		prepararPartidaDeTresJogadores();
		colocarYurt(Cor.vermelho, "D");
		colocarYurt(Cor.vermelho, "E");
		colocarYurt(Cor.azul, "D");
		conquistarCidade(Cor.azul, "c2");
		conquistarCidade(Cor.verde, "c3");
		colocarMelhorias(Cor.verde, Coluna.vermelha, TipoMelhoria.punho, 4);
		assertEquals("Com empate no 1o lugar o verde nao fica em 2o", 0, influencia(Cor.verde));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void empatadosNoSegundoLugarRecebemATabelaDoSegundo() throws JogadaInvalidaException {
		prepararPartidaDeTresJogadores();
		colocarYurt(Cor.vermelho, "D");
		colocarYurt(Cor.vermelho, "E");
		colocarYurt(Cor.azul, "D");
		conquistarCidade(Cor.verde, "c2");
		colocarMelhorias(Cor.azul, Coluna.vermelha, TipoMelhoria.punho, 1);
		colocarMelhorias(Cor.verde, Coluna.vermelha, TipoMelhoria.punho, 1);
		assertEquals("Azul e verde empatam em 2o e cada um recebe 1 voto", 2, influencia(Cor.azul) + influencia(Cor.verde));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void jogadorSemYurtsNaRegiaoNaoRecebeInfluencia() throws JogadaInvalidaException {
		colocarYurt(Cor.vermelho, "A");
		colocarMelhorias(Cor.azul, Coluna.amarela, TipoMelhoria.punho, 1);
		assertEquals("Sem yurts na Russia o azul nao fica em 2o", 0, influencia(Cor.azul));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void vencedorEhQuemTemMaisVotos() {
		jogador(Cor.vermelho).adicionarVotos(5);
		jogador(Cor.azul).adicionarVotos(3);
		assertEquals("O vermelho tem mais votos", Cor.vermelho, vencedores().get(0).getCor());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void empateEmVotosEhDecididoPelosTesourosNaoEntregues() {
		jogador(Cor.vermelho).adicionarVotos(5);
		jogador(Cor.azul).adicionarVotos(5);
		darTesouros(Cor.azul, TipoTesouro.seda, 2);
		assertEquals("O azul tem mais tesouros", Cor.azul, vencedores().get(0).getCor());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void empateEmVotosETesourosEhDecididoPelaProximidadeDeKarakorum() {
		jogador(Cor.vermelho).adicionarVotos(5);
		jogador(Cor.azul).adicionarVotos(5);
		posicionar(Cor.vermelho, "B");
		posicionar(Cor.azul, "A");
		assertEquals("O azul em A esta a 1 parada de Karakorum", Cor.azul, vencedores().get(0).getCor());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void empatePersistenteCompartilhaAVitoria() {
		jogador(Cor.vermelho).adicionarVotos(5);
		jogador(Cor.azul).adicionarVotos(5);
		assertEquals("Os dois jogadores empatam em tudo", 2, vencedores().size());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void cartaSecretaEntraNaApuracao() {
		colocarYurt(Cor.vermelho, "A");
		colocarYurt(Cor.vermelho, "B");
		colocarYurt(Cor.vermelho, "D");
		assertEquals("O vermelho ficou com o Viajante e tem 3 yurts conectados", 1, ApuracaoFinal.votosDaCartaSecreta(jogador(Cor.vermelho), mapa));
	}
}
