package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TesteAtaque extends TesteBase {

	private void prepararAtaqueEmC1(int espadas) throws JogadaInvalidaException {
		darTributos(Cor.vermelho, TipoTributo.espada, espadas);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.mover("A");
	}

	private void conquistarC1() throws JogadaInvalidaException {
		prepararAtaqueEmC1(7);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		jogo.atacarCidade("c1", TipoTesouro.pele);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void primeiroTesouroDaCidadeCustaUmaEspada() throws JogadaInvalidaException {
		prepararAtaqueEmC1(3);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		assertEquals("De 3 espadas deve sobrar 2", 2, jogo.getTributos(Cor.vermelho, TipoTributo.espada));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void segundoTesouroDaMesmaCidadeNoMesmoTurnoCustaDuasEspadas() throws JogadaInvalidaException {
		prepararAtaqueEmC1(3);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		assertEquals("1 espada pelo primeiro e 2 pelo segundo consomem as 3", 0, jogo.getTributos(Cor.vermelho, TipoTributo.espada));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void segundoAtaqueSemDuasEspadasLancaExcecao() throws JogadaInvalidaException {
		prepararAtaqueEmC1(2);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		jogo.atacarCidade("c1", TipoTesouro.pele);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void tesouroAtacadoPassaAoJogador() throws JogadaInvalidaException {
		prepararAtaqueEmC1(1);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		assertEquals("O jogador deve receber a pele tomada da cidade", 1, jogo.getTesouros(Cor.vermelho, TipoTesouro.pele));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void atacarCidadeNaoAdjacenteAParadaVisitadaLancaExcecao() throws JogadaInvalidaException {
		prepararAtaqueEmC1(1);
		jogo.atacarCidade("c2", TipoTesouro.pele);
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void atacarCidadeAindaNaoReveladaLancaExcecao() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "F");
		darTributos(Cor.vermelho, TipoTributo.espada, 1);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.atacarCidade("c4", TipoTesouro.ferro);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void tomarOUltimoTesouroConquistaACidade() throws JogadaInvalidaException {
		conquistarC1();
		assertEquals("O vermelho tomou o ultimo tesouro de c1", Cor.vermelho, jogo.getConquistador("c1"));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void conquistaColocaUmYurtDoConquistadorNaCidade() throws JogadaInvalidaException {
		conquistarC1();
		assertEquals("Um dos 12 yurts sai da reserva para o centro da cidade", 11, jogo.getYurtsNaReserva(Cor.vermelho));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void conquistaRevelaAProximaCidadeDaPilha() throws JogadaInvalidaException {
		conquistarC1();
		assertTrue("A cidade c4 estava na pilha e deve ser revelada", jogo.isCidadeRevelada("c4"));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void cidadeReveladaAposConquistaRecebeQuatroTesouros() throws JogadaInvalidaException {
		conquistarC1();
		assertEquals("A nova cidade recebe 4 tesouros da pilha", 4, jogo.getTesourosNaCidade("c4"));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void espadasGastasVoltamAoEstoque() throws JogadaInvalidaException {
		conquistarC1();
		assertEquals("O estoque tem 10 espadas, 2 vao para as provincias pa e pd, 7 saem e as 7 gastas voltam", 8, jogo.getEstoque(TipoTributo.espada));
	}
}
