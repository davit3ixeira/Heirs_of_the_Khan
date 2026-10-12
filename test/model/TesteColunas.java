package model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TesteColunas extends TesteBase {

	private void ambosJogamAColunaVermelha() throws JogadaInvalidaException {
		jogarTurnoSemAcoes(Coluna.vermelha);
		jogarTurnoSemAcoes(Coluna.vermelha);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void colunaAtivadaFicaOcupada() throws JogadaInvalidaException {
		jogarTurnoSemAcoes(Coluna.vermelha);
		assertFalse("A peca de ativacao permanece na coluna vermelha", jogo.isColunaDisponivel(Cor.vermelho, Coluna.vermelha));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void colunaOcupadaNaoPodeSerReativada() throws JogadaInvalidaException {
		ambosJogamAColunaVermelha();
		jogo.iniciarTurno(Coluna.vermelha);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void colunaBrancaRecuperaAsPecasDasOutrasColunas() throws JogadaInvalidaException {
		ambosJogamAColunaVermelha();
		jogo.iniciarTurno(Coluna.branca);
		assertTrue("A coluna vermelha volta a ficar livre", jogo.isColunaDisponivel(Cor.vermelho, Coluna.vermelha));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void colunaBrancaRecuperaAPropriaPecaDeAtivacao() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.branca);
		assertTrue("A peca usada na coluna branca tambem e recuperada", jogo.isColunaDisponivel(Cor.vermelho, Coluna.branca));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void colunaBrancaExigeMoverOKhan() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.branca);
		jogo.finalizarTurno();
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void naoSeIniciaUmTurnoComOutroEmAndamento() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.iniciarTurno(Coluna.verde);
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void acaoSemTurnoIniciadoLancaExcecao() throws JogadaInvalidaException {
		jogo.mover("A");
	}
}
