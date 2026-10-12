package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TesteTurno extends TesteBase {

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void partidaComUmJogadorLancaExcecao() throws JogadaInvalidaException {
		new Jogo(criarMapaDeTeste(), new Cor[] { Cor.vermelho });
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void partidaComCoresRepetidasLancaExcecao() throws JogadaInvalidaException {
		new Jogo(criarMapaDeTeste(), new Cor[] { Cor.vermelho, Cor.vermelho });
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void terceiroJogadorComecaComDuasMoedas() throws JogadaInvalidaException {
		Jogo tresJogadores = new Jogo(criarMapaDeTeste(), new Cor[] { Cor.vermelho, Cor.azul, Cor.verde });
		assertEquals("O 1o e o 2o recebem 1 moeda; os demais, 2", 2, tresJogadores.getTributos(Cor.verde, TipoTributo.moeda));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void moverParaParadaAdjacenteConsomeUmMovimento() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("A");
		assertEquals("A coluna verde da 2 movimentos e o passo para A gasta 1", 1, jogo.getMovimentosRestantes());
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void moverParaParadaNaoAdjacenteLancaExcecao() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("B");
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void pularParadaComYurtProprioCustaApenasUmMovimento() throws JogadaInvalidaException {
		colocarYurt(Cor.vermelho, "A");
		jogo.iniciarTurno(Coluna.verde);
		jogo.mover("B");
		assertEquals("Pular o yurt proprio em A nao custa movimento extra", 1, jogo.getMovimentosRestantes());
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void paradaPuladaNaoServeParaPegarTributo() throws JogadaInvalidaException {
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
		assertEquals("Depois do turno valido a vez passa ao azul", Cor.azul, jogo.getJogadorDaVez());
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void reusarColunaLancaExcecao() throws JogadaInvalidaException {
		jogarTurnoSemAcoes(Coluna.vermelha);
		jogarTurnoSemAcoes(Coluna.vermelha);
		jogo.iniciarTurno(Coluna.vermelha);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void colunaBrancaLiberaTodasAsColunas() throws JogadaInvalidaException {
		jogarTurnoSemAcoes(Coluna.vermelha);
		jogarTurnoSemAcoes(Coluna.vermelha);
		jogo.iniciarTurno(Coluna.branca);
		assertTrue("A coluna vermelha volta a ficar livre", jogo.isColunaDisponivel(Cor.vermelho, Coluna.vermelha));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void acaoSemTurnoIniciadoLancaExcecao() throws JogadaInvalidaException {
		jogo.mover("A");
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void somaDosVotosNoLimiteDisparaOFim() {
		jogo.ganharVotos(jogo.getJogador(Cor.vermelho), 6);
		jogo.ganharVotos(jogo.getJogador(Cor.azul), 4);
		assertTrue("Com 2 jogadores o fim e disparado aos 10 votos somados", jogo.isFimDisparado());
	}
}
