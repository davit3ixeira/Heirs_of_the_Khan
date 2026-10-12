package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TesteMovimento extends TesteBase {

	@Test(timeout = DEFAULT_TIMEOUT)
	public void moverParaParadaAdjacenteConsomeUmMovimento() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("A");
		assertEquals("A coluna verde da 2 movimentos e mover para parada adjacente gasta 1", 1, jogo.getMovimentosRestantes());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void moverParaParadaAdjacenteAtualizaAPosicaoDoPeao() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("A");
		assertEquals("O peao deve estar na parada de destino", "A", jogo.getPosicao(Cor.vermelho));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void moverParaParadaNaoAdjacenteLancaExcecao() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("B");
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void moverSemMovimentosRestantesLancaExcecao() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.mover("A");
		jogo.mover("B");
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void pularParadaComYurtProprioCustaApenasUmMovimento() throws JogadaInvalidaException {
		colocarYurt(Cor.vermelho, "A");
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("B");
		assertEquals("Pular o yurt proprio em A nao deve custar movimento extra", 1, jogo.getMovimentosRestantes());
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void pularParadaComYurtDeOutroJogadorNaoEhPermitido() throws JogadaInvalidaException {
		colocarYurt(Cor.azul, "A");
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("B");
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void paradaPuladaNaoServeDeBaseParaPegarTributo() throws JogadaInvalidaException {
		colocarYurt(Cor.vermelho, "A");
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("B");
		jogo.pegarTributo("pb");
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void terminarTurnoEmParadaSimplesOcupadaLancaExcecao() throws JogadaInvalidaException {
		posicionar(Cor.azul, "A");
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("A");
		jogo.finalizarTurno();
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void terminarTurnoEmParadaDuplaOcupadaEhPermitido() throws JogadaInvalidaException {
		posicionar(Cor.azul, "D");
		posicionar(Cor.vermelho, "B");
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("D");
		jogo.finalizarTurno();
		assertEquals("O turno deve passar ao azul apos terminar na parada dupla", Cor.azul, jogo.getJogadorDaVez());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void passarPorParadaOcupadaSemTerminarNelaEhPermitido() throws JogadaInvalidaException {
		posicionar(Cor.azul, "A");
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("A");
		jogo.mover("B");
		jogo.finalizarTurno();
		assertEquals("O peao deve terminar em B depois de passar por A", "B", jogo.getPosicao(Cor.vermelho));
	}
}
