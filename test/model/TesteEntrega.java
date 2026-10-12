package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TesteEntrega extends TesteBase {

	private static final TipoTesouro[] DUAS_PELES = new TipoTesouro[] { TipoTesouro.pele, TipoTesouro.pele };

	private void entregarDuasPelesNaParadaDupla() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "D");
		darTesouros(Cor.vermelho, TipoTesouro.pele, 2);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.entregarTesouros(DUAS_PELES);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void entregaDeDoisIguaisDaUmVoto() throws JogadaInvalidaException {
		entregarDuasPelesNaParadaDupla();
		assertEquals("O conselheiro de D pede 2 peles e vale 1 voto", 1, jogo.getVotos(Cor.vermelho));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void entregaRetiraOsTesourosDoJogador() throws JogadaInvalidaException {
		entregarDuasPelesNaParadaDupla();
		assertEquals("As 2 peles entregues saem do jogador", 0, jogo.getTesouros(Cor.vermelho, TipoTesouro.pele));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void primeiroAEntregarRecebeOBonusDoConselheiro() throws JogadaInvalidaException {
		entregarDuasPelesNaParadaDupla();
		assertEquals("O bonus de D e duas espadas", 1, jogo.getBonusDisponiveis(Cor.vermelho, TipoBonus.duasEspadas));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void segundaEntregaAoMesmoConselheiroNaoDaOutroBonus() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "D");
		darTesouros(Cor.vermelho, TipoTesouro.pele, 4);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.entregarTesouros(DUAS_PELES);
		jogo.entregarTesouros(DUAS_PELES);
		assertEquals("O bonus so e dado ao primeiro a entregar", 1, jogo.getBonusDisponiveis(Cor.vermelho, TipoBonus.duasEspadas));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void entregaEmKarakorumDaDoisVotos() throws JogadaInvalidaException {
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

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void entregarTesourosQueNaoPossuiLancaExcecao() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "D");
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.entregarTesouros(DUAS_PELES);
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void entregarForaDeParadaDeConselheiroLancaExcecao() throws JogadaInvalidaException {
		posicionar(Cor.vermelho, "A");
		darTesouros(Cor.vermelho, TipoTesouro.pele, 2);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.entregarTesouros(DUAS_PELES);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void tesouroVirtualPermiteEntregarUmTesouroAMenos() throws JogadaInvalidaException {
		jogador(Cor.vermelho).getTabuleiro().colocarMelhoria(new Melhoria(TipoMelhoria.tesouroVirtual, Coluna.vermelha, TipoTesouro.pele));
		posicionar(Cor.vermelho, "D");
		darTesouros(Cor.vermelho, TipoTesouro.pele, 1);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.entregarTesouros(new TipoTesouro[] { TipoTesouro.pele });
		assertEquals("A pele virtual completa o pedido de 2 peles", 1, jogo.getVotos(Cor.vermelho));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void bonusDeDuasEspadasDaDuasEspadasDoEstoque() throws JogadaInvalidaException {
		entregarDuasPelesNaParadaDupla();
		jogo.usarBonus(TipoBonus.duasEspadas);
		assertEquals("O bonus usado entrega 2 espadas", 2, jogo.getTributos(Cor.vermelho, TipoTributo.espada));
	}
}
