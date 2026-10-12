package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TesteKhan extends TesteBase {

	@Test(timeout = DEFAULT_TIMEOUT)
	public void khanNaProvinciaDaUmTributoAElaPropria() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.amarela);
		jogo.moverKhanParaProvincia("pk");
		assertEquals("A provincia do Khan tinha 1 peca e recebe mais 1", 2, jogo.getPecasNaProvincia("pk"));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void khanNaProvinciaDaUmTributoAsDuasVizinhasIndicadas() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.amarela);
		jogo.moverKhanParaProvincia("pk");
		assertEquals("A soma das vizinhas pd e pe deve passar de 2 para 4", 4, jogo.getPecasNaProvincia("pd") + jogo.getPecasNaProvincia("pe"));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void khanRespeitaOMaximoDeTresPecasPorProvincia() throws JogadaInvalidaException {
		mapa.getProvincia("pd").adicionarPecas(2);
		jogo.iniciarTurno(Coluna.amarela);
		jogo.moverKhanParaProvincia("pk");
		assertEquals("A provincia pd ja estava cheia e deve continuar com 3", 3, jogo.getPecasNaProvincia("pd"));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void khanNaoPodePermanecerNaMesmaProvincia() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.amarela);
		jogo.moverKhanParaProvincia("pk");
		jogo.finalizarTurno();
		jogo.iniciarTurno(Coluna.amarela);
		jogo.moverKhanParaProvincia("pk");
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void khanSoPodeIrParaProvinciaDoKhan() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.amarela);
		jogo.moverKhanParaProvincia("pa");
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void finalizarTurnoDaColunaAmarelaSemMoverOKhanLancaExcecao() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.amarela);
		jogo.finalizarTurno();
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void moverKhanComColunaSemIconeDoKhanLancaExcecao() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.moverKhanParaAreaDeMelhorias();
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void khanNaAreaComEspacoDeMoedaVazioDaUmaMoeda() throws JogadaInvalidaException {
		darTributos(Cor.vermelho, TipoTributo.moeda, 1);
		jogo.iniciarTurno(Coluna.amarela);
		jogo.comprarMelhoria(0);
		jogo.moverKhanParaAreaDeMelhorias();
		assertEquals("As 2 moedas foram gastas e o Khan deu 1 pelo espaco de moeda vazio", 1, jogo.getTributos(Cor.vermelho, TipoTributo.moeda));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void khanNaAreaComEspacoDeEspadaOcupadoNaoDaEspada() throws JogadaInvalidaException {
		darTributos(Cor.vermelho, TipoTributo.moeda, 1);
		jogo.iniciarTurno(Coluna.amarela);
		jogo.comprarMelhoria(0);
		jogo.moverKhanParaAreaDeMelhorias();
		assertEquals("O espaco de espada estava ocupado, logo nenhuma espada e recebida", 0, jogo.getTributos(Cor.vermelho, TipoTributo.espada));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void khanNaAreaReabasteceOsEspacosVazios() throws JogadaInvalidaException {
		darTributos(Cor.vermelho, TipoTributo.moeda, 1);
		jogo.iniciarTurno(Coluna.amarela);
		jogo.comprarMelhoria(0);
		jogo.moverKhanParaAreaDeMelhorias();
		assertNotNull("O espaco 0 deve receber uma nova melhoria", jogo.getMelhoriaNaArea(0));
	}
}
