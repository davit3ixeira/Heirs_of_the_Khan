package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TesteTributo extends TesteBase {

	@Test(timeout = DEFAULT_TIMEOUT)
	public void pegarTributoDeProvinciaAdjacenteDaAPecaAoJogador() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.pegarTributo("pa");
		assertEquals("A provincia pa tem icone de espada e e adjacente a Karakorum", 1, jogo.getTributos(Cor.vermelho, TipoTributo.espada));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void pegarTributoRetiraAPecaDaProvincia() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.pegarTributo("pa");
		assertEquals("A provincia pa deve ficar vazia", 0, jogo.getPecasNaProvincia("pa"));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void pegarTributoDeProvinciaNaoAdjacenteLancaExcecao() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.pegarTributo("pd");
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void quantidadeDeTributosEhLimitadaPelosPunhos() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.pegarTributo("pa");
		jogo.pegarTributo("pf");
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void paradaVisitadaDuranteOMovimentoPermitePegarTributo() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.mover("A");
		jogo.pegarTributo("pb");
		assertEquals("A provincia pb e adjacente apenas a A, visitada no turno", 1, jogo.getTributos(Cor.vermelho, TipoTributo.yurt));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void paradaInicialContinuaValendoDepoisDoMovimento() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.amarela);
		jogo.mover("A");
		jogo.pegarTributo("pf");
		assertEquals("A provincia pf so e adjacente a Karakorum, onde o turno comecou", 0, jogo.getPecasNaProvincia("pf"));
	}
}
