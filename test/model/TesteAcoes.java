package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TesteAcoes extends TesteBase {

	private static final TipoTesouro[] DUAS_PELES = new TipoTesouro[] { TipoTesouro.pele, TipoTesouro.pele };

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
	public void pegarTributoDeProvinciaAdjacenteDaAPecaAoJogador() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.pegarTributo("pa");
		assertEquals("A provincia pa tem icone de espada e e adjacente a Karakorum", 1, jogo.getTributos(Cor.vermelho, TipoTributo.espada));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void pegarTributoDeProvinciaNaoAdjacenteLancaExcecao() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.pegarTributo("pd");
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void pegarMaisTributosQueAsAcoesDaColunaLancaExcecao() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.pegarTributo("pa");
		jogo.pegarTributo("pf");
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void primeiroTesouroDaCidadeCustaUmaEspada() throws JogadaInvalidaException {
		prepararAtaqueEmC1(3);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		assertEquals("De 3 espadas sobram 2", 2, jogo.getTributos(Cor.vermelho, TipoTributo.espada));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void segundoTesouroDaMesmaCidadeCustaDuasEspadas() throws JogadaInvalidaException {
		prepararAtaqueEmC1(3);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		jogo.atacarCidade("c1", TipoTesouro.pele);
		assertEquals("1 espada pelo primeiro e 2 pelo segundo consomem as 3", 0, jogo.getTributos(Cor.vermelho, TipoTributo.espada));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void tomarOUltimoTesouroConquistaACidade() throws JogadaInvalidaException {
		conquistarC1();
		assertEquals("O vermelho tomou o ultimo tesouro de c1", Cor.vermelho, jogo.getConquistador("c1"));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void conquistaColocaUmYurtDoJogadorNaCidade() throws JogadaInvalidaException {
		conquistarC1();
		assertEquals("Um dos 12 yurts sai da reserva para a cidade", 11, jogo.getYurtsNaReserva(Cor.vermelho));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void conquistaRevelaAProximaCidade() throws JogadaInvalidaException {
		conquistarC1();
		assertTrue("A cidade c4 estava na pilha e deve ser revelada", jogo.isCidadeRevelada("c4"));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void construirYurtEmKarakorumLancaExcecao() throws JogadaInvalidaException {
		darTributos(Cor.vermelho, TipoTributo.yurt, 1);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.construirYurt(Mapa.KARAKORUM);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void paradaDuplaAceitaDoisYurts() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "D");
		darTributos(Cor.vermelho, TipoTributo.yurt, 2);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.construirYurt("D");
		jogo.construirYurt("D");
		assertEquals("A parada dupla D comporta 2 yurts", 2, jogo.getYurtsNaParada("D"));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void paradaSimplesAceitaApenasUmYurt() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "A");
		darTributos(Cor.vermelho, TipoTributo.yurt, 2);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.construirYurt("A");
		jogo.construirYurt("A");
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void entregarDoisTesourosIguaisAoConselheiroCertoDaUmVoto() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "D");
		darTesouros(Cor.vermelho, TipoTesouro.pele, 2);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.entregarTesouros(DUAS_PELES);
		assertEquals("O conselheiro de D pede 2 peles e vale 1 voto", 1, jogo.getVotos(Cor.vermelho));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void entregarTresTesourosEspecificosEmKarakorumDaDoisVotos() throws JogadaInvalidaException {
		darTesouros(Cor.vermelho, TipoTesouro.pele, 1);
		darTesouros(Cor.vermelho, TipoTesouro.ferro, 1);
		darTesouros(Cor.vermelho, TipoTesouro.boi, 1);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.entregarTesouros(new TipoTesouro[] { TipoTesouro.pele, TipoTesouro.ferro, TipoTesouro.boi });
		assertEquals("Karakorum pede pele, ferro e boi e vale 2 votos", 2, jogo.getVotos(Cor.vermelho));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void entregaQueNaoAtendeAoPedidoLancaExcecao() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "D");
		darTesouros(Cor.vermelho, TipoTesouro.pele, 1);
		darTesouros(Cor.vermelho, TipoTesouro.ferro, 1);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.entregarTesouros(new TipoTesouro[] { TipoTesouro.pele, TipoTesouro.ferro });
	}
}
