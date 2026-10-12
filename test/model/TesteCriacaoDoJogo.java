package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TesteCriacaoDoJogo extends TesteBase {

	private static final Cor[] DUAS_CORES = new Cor[] { Cor.vermelho, Cor.azul };
	private static final Herdeiro[] DOIS_HERDEIROS = new Herdeiro[] { Herdeiro.altani, Herdeiro.chagatai };

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void jogoComUmJogadorLancaExcecao() throws JogadaInvalidaException {
		new Jogo(MapaDeTeste.criar(), new Cor[] { Cor.vermelho }, new Herdeiro[] { Herdeiro.altani }, new SorteadorFixo());
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void coresRepetidasLancamExcecao() throws JogadaInvalidaException {
		new Jogo(MapaDeTeste.criar(), new Cor[] { Cor.vermelho, Cor.vermelho }, DOIS_HERDEIROS, new SorteadorFixo());
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void herdeirosRepetidosLancamExcecao() throws JogadaInvalidaException {
		new Jogo(MapaDeTeste.criar(), DUAS_CORES, new Herdeiro[] { Herdeiro.jochi, Herdeiro.jochi }, new SorteadorFixo());
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void quantidadesDiferentesDeCoresEHerdeirosLancamExcecao() throws JogadaInvalidaException {
		new Jogo(MapaDeTeste.criar(), DUAS_CORES, new Herdeiro[] { Herdeiro.altani }, new SorteadorFixo());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void peoesComecamEmKarakorum() {
		assertEquals("Todos os peoes comecam em Karakorum", KARAKORUM, jogo.getPosicao(Cor.azul));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void segundoJogadorComecaComUmaMoeda() {
		assertEquals("O 1o e o 2o jogadores recebem 1 moeda", 1, jogo.getTributos(Cor.azul, TipoTributo.moeda));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void terceiroJogadorComecaComDuasMoedas() throws JogadaInvalidaException {
		jogo = criarJogo(new Cor[] { Cor.vermelho, Cor.azul, Cor.verde }, new Herdeiro[] { Herdeiro.altani, Herdeiro.chagatai, Herdeiro.ogedei });
		assertEquals("Do 3o jogador em diante sao 2 moedas", 2, jogo.getTributos(Cor.verde, TipoTributo.moeda));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void cadaProvinciaComecaComUmTributo() {
		assertEquals("A provincia pk comeca com 1 peca do seu icone", 1, jogo.getPecasNaProvincia("pk"));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void cidadesReveladasComecamComQuatroTesouros() {
		assertEquals("Cada cidade revelada recebe 4 tesouros", 4, jogo.getTesourosNaCidade("c3"));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void apenasTresCidadesSaoReveladas() {
		assertFalse("A quarta cidade fica na pilha", jogo.isCidadeRevelada("c4"));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void quatroCartasAbertasSaoReveladas() {
		assertEquals("Apenas 4 das 14 cartas abertas sao usadas", 4, jogo.getQuantidadeDeCartasAbertas());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void cincoMelhoriasSaoReveladas() {
		assertEquals("A pilha guarda as 39 melhorias restantes", 39, jogo.getAreaMelhorias().tamanhoDaPilha());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void cadaJogadorRecebeDuasCartasSecretas() throws JogadaInvalidaException {
		Jogo semEscolha = new Jogo(MapaDeTeste.criar(), DUAS_CORES, DOIS_HERDEIROS, new SorteadorFixo());
		assertEquals("Antes da escolha o jogador tem 2 cartas secretas", 2, semEscolha.getQuantidadeDeCartasSecretas(Cor.azul));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void iniciarTurnoSemEscolherCartaSecretaLancaExcecao() throws JogadaInvalidaException {
		Jogo semEscolha = new Jogo(MapaDeTeste.criar(), DUAS_CORES, DOIS_HERDEIROS, new SorteadorFixo());
		semEscolha.iniciarTurno(Coluna.vermelha);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void construtorPublicoUsaOMapaPadrao() throws JogadaInvalidaException {
		Jogo padrao = new Jogo(DUAS_CORES, DOIS_HERDEIROS);
		assertEquals("O mapa padrao tem as 9 cidades do manual", 9, padrao.getMapa().getCidades().size());
	}
}
