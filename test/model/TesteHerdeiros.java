package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TesteHerdeiros extends TesteBase {

	@Before
	public void prepararPartidaComToluiEJochi() throws JogadaInvalidaException {
		jogo = criarJogo(new Cor[] { Cor.vermelho, Cor.azul }, new Herdeiro[] { Herdeiro.tolui, Herdeiro.jochi });
	}

	private void prepararPartidaComJochiPrimeiro() throws JogadaInvalidaException {
		jogo = criarJogo(new Cor[] { Cor.vermelho, Cor.azul }, new Herdeiro[] { Herdeiro.jochi, Herdeiro.tolui });
		darTributos(Cor.vermelho, TipoTributo.moeda, 1);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void toluiComecaComUmYurtEmKarakorum() {
		assertTrue("O Tolui tem um yurt em Karakorum", mapa.getKarakorum().temYurtDe(jogador(Cor.vermelho)));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void yurtDoToluiEmKarakorumSaiDaReserva() {
		assertEquals("Restam 11 yurts na reserva do Tolui", 11, jogo.getYurtsNaReserva(Cor.vermelho));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void toluiPulaKarakorumComOProprioYurt() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "A");
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.mover("F");
		assertEquals("De A para F passando por Karakorum com 1 movimento", "F", jogo.getPosicao(Cor.vermelho));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void toluiConstroiEmParadaAdjacenteMesmoComPeaoOponente() throws JogadaInvalidaException {
		posicionar(Cor.azul, "A");
		darTributos(Cor.vermelho, TipoTributo.yurt, 1);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.construirYurt("A");
		assertEquals("A e adjacente a Karakorum, onde esta o Tolui", 1, jogo.getYurtsNaParada("A"));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void yurtDoToluiEmKarakorumNaoContaParaInfluencia() {
		assertEquals("Karakorum nao pertence a nenhuma regiao", 0, (int) ApuracaoFinal.votosDeInfluencia(jogo.getJogadores(), mapa).get(jogador(Cor.vermelho)));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void yurtDoToluiEmKarakorumContaComoYurtNoMapa() {
		assertEquals("O yurt de Karakorum conta para as cartas de yurts no mapa", 1, Contagem.yurtsNoMapa(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void jochiNaoRecebeMelhoriaGratuitaPelaPrimeiraLinha() throws JogadaInvalidaException {
		prepararPartidaComJochiPrimeiro();
		colocarMelhorias(Cor.vermelho, Coluna.vermelha, TipoMelhoria.punho, 1);
		colocarMelhorias(Cor.vermelho, Coluna.verde, TipoMelhoria.punho, 1);
		jogo.iniciarTurno(Coluna.verde);
		jogo.comprarMelhoria(2);
		assertEquals("O 1o espaco da coluna branca do Jochi ja vem impresso", 0, jogo.getMelhoriasGratuitasPendentes());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void jochiRecebeMelhoriaGratuitaPelaSegundaLinha() throws JogadaInvalidaException {
		prepararPartidaComJochiPrimeiro();
		colocarMelhorias(Cor.vermelho, Coluna.vermelha, TipoMelhoria.punho, 2);
		colocarMelhorias(Cor.vermelho, Coluna.verde, TipoMelhoria.punho, 2);
		colocarMelhorias(Cor.vermelho, Coluna.amarela, TipoMelhoria.punho, 1);
		jogo.iniciarTurno(Coluna.verde);
		jogo.comprarMelhoria(2);
		assertEquals("A segunda linha da direito a melhoria gratuita", 1, jogo.getMelhoriasGratuitasPendentes());
	}
}
