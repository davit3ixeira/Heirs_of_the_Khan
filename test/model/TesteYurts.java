package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TesteYurts extends TesteBase {

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void construirYurtEmKarakorumLancaExcecao() throws JogadaInvalidaException {
		darTributos(Cor.vermelho, TipoTributo.yurt, 1);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.construirYurt(KARAKORUM);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void paradaDuplaAceitaDoisYurtsDoMesmoJogador() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "D");
		darTributos(Cor.vermelho, TipoTributo.yurt, 2);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.construirYurt("D");
		jogo.construirYurt("D");
		assertEquals("A parada dupla D deve ter 2 yurts", 2, jogo.getYurtsNaParada("D"));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void paradaSimplesAceitaApenasUmYurt() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "A");
		darTributos(Cor.vermelho, TipoTributo.yurt, 2);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.construirYurt("A");
		jogo.construirYurt("A");
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void construirEmParadaNaoVisitadaLancaExcecao() throws JogadaInvalidaException {
		darTributos(Cor.vermelho, TipoTributo.yurt, 1);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.construirYurt("A");
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void construirEmParadaVisitadaComPeaoOponenteEhPermitido() throws JogadaInvalidaException {
		posicionar(Cor.azul, "A");
		darTributos(Cor.vermelho, TipoTributo.yurt, 1);
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("A");
		jogo.construirYurt("A");
		assertEquals("O yurt deve ser construido mesmo com o peao azul em A", 1, jogo.getYurtsNaParada("A"));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void construirSemTributoDeYurtLancaExcecao() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "A");
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.construirYurt("A");
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void yurtVirtualEhGastoAntesDaPeca() throws JogadaInvalidaException {
		colocarMelhorias(Cor.vermelho, Coluna.vermelha, TipoMelhoria.yurtVirtual, 1);
		posicionar(Cor.vermelho, "A");
		darTributos(Cor.vermelho, TipoTributo.yurt, 1);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.construirYurt("A");
		assertEquals("O yurt virtual paga a construcao e a peca fica com o jogador", 1, jogo.getTributos(Cor.vermelho, TipoTributo.yurt));
	}
}
