package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TesteFimDeJogo extends TesteBase {

	private static final int LIMITE_PARA_DOIS_JOGADORES = 10;

	private void dispararFimNoTurnoDoPrimeiroJogador() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.ganharVotos(jogador(Cor.vermelho), LIMITE_PARA_DOIS_JOGADORES);
		jogo.finalizarTurno();
		jogarTurnoSemAcoes(Coluna.vermelha);
	}

	private void jogarRodadaExtra() throws JogadaInvalidaException {
		jogarTurnoSemAcoes(Coluna.verde);
		jogarTurnoSemAcoes(Coluna.verde);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void fichaNeutraNoLimiteDisparaOFim() {
		jogo.ganharVotos(jogador(Cor.vermelho), LIMITE_PARA_DOIS_JOGADORES);
		assertTrue("Com 2 jogadores o fim e disparado aos 10 votos neutros", jogo.isFimDisparado());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void fichaNeutraAbaixoDoLimiteNaoDisparaOFim() {
		jogo.ganharVotos(jogador(Cor.vermelho), LIMITE_PARA_DOIS_JOGADORES - 1);
		assertFalse("9 votos neutros nao bastam com 2 jogadores", jogo.isFimDisparado());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void votosDeJogadoresDiferentesSomamNaFichaNeutra() {
		jogo.ganharVotos(jogador(Cor.vermelho), 6);
		jogo.ganharVotos(jogador(Cor.azul), 4);
		assertTrue("6 + 4 votos levam a ficha neutra ao limite", jogo.isFimDisparado());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void partidaNaoTerminaAoFimDaRodadaEmQueOFimFoiDisparado() throws JogadaInvalidaException {
		dispararFimNoTurnoDoPrimeiroJogador();
		assertFalse("Ainda falta jogar uma rodada completa", jogo.isTerminado());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void partidaTerminaAposUmaRodadaCompletaExtra() throws JogadaInvalidaException {
		dispararFimNoTurnoDoPrimeiroJogador();
		jogarRodadaExtra();
		assertTrue("A partida acaba depois da rodada extra", jogo.isTerminado());
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void iniciarTurnoDepoisDoTerminoLancaExcecao() throws JogadaInvalidaException {
		dispararFimNoTurnoDoPrimeiroJogador();
		jogarRodadaExtra();
		jogo.iniciarTurno(Coluna.amarela);
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void apurarResultadoAntesDoTerminoLancaExcecao() throws JogadaInvalidaException {
		jogo.apurarResultado();
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void apuracaoAoFimDaPartidaApontaQuemTemMaisVotos() throws JogadaInvalidaException {
		dispararFimNoTurnoDoPrimeiroJogador();
		jogarRodadaExtra();
		assertEquals("O vermelho tem 10 votos e o azul nenhum", Cor.vermelho, jogo.apurarResultado()[0]);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void conquistarTodasAsCidadesDisparaOFim() throws JogadaInvalidaException {
		conquistarCidade(Cor.azul, "c2");
		conquistarCidade(Cor.azul, "c3");
		conquistarCidade(Cor.azul, "c4");
		darTributos(Cor.vermelho, TipoTributo.espada, 7);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.mover("A");
		jogo.atacarCidade("c1", TipoTesouro.pele);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		assertTrue("A ultima cidade foi conquistada", jogo.isFimDisparado());
	}
}
