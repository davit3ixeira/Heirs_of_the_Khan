package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class TesteMelhorias extends TesteBase {

	private void iniciarTurnoVerdeComDuasMoedas() throws JogadaInvalidaException {
		darTributos(Cor.vermelho, TipoTributo.moeda, 1);
		jogo.iniciarTurno(Coluna.verde);
	}

	private void completarLinhaComprandoMelhoriaAmarela() throws JogadaInvalidaException {
		colocarMelhorias(Cor.vermelho, Coluna.vermelha, TipoMelhoria.punho, 1);
		colocarMelhorias(Cor.vermelho, Coluna.verde, TipoMelhoria.punho, 1);
		iniciarTurnoVerdeComDuasMoedas();
		jogo.comprarMelhoria(2);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void comprarMelhoriaCustaDuasMoedas() throws JogadaInvalidaException {
		iniciarTurnoVerdeComDuasMoedas();
		jogo.comprarMelhoria(1);
		assertEquals("As 2 moedas devem ser gastas", 0, jogo.getTributos(Cor.vermelho, TipoTributo.moeda));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void melhoriaCompradaVaiParaAColunaDaSuaCorDeFundo() throws JogadaInvalidaException {
		iniciarTurnoVerdeComDuasMoedas();
		jogo.comprarMelhoria(1);
		assertEquals("A melhoria da posicao 1 tem fundo verde", 1, jogo.getQuantidadeDeMelhorias(Cor.vermelho, Coluna.verde));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void comprarMelhoriaSemMoedasSuficientesLancaExcecao() throws JogadaInvalidaException {
		jogo.iniciarTurno(Coluna.verde);
		jogo.comprarMelhoria(1);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void moedaVirtualEhGastaAntesDasPecas() throws JogadaInvalidaException {
		darTributos(Cor.vermelho, TipoTributo.moeda, 1);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.comprarMelhoria(1);
		assertEquals("A coluna vermelha da 1 moeda virtual, entao so 1 peca e gasta", 1, jogo.getTributos(Cor.vermelho, TipoTributo.moeda));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void melhoriasRestantesDeslizamParaADireita() throws JogadaInvalidaException {
		Melhoria daPosicaoTres = jogo.getAreaMelhorias().get(3);
		iniciarTurnoVerdeComDuasMoedas();
		jogo.comprarMelhoria(4);
		assertSame("A melhoria da posicao 3 deve deslizar para a posicao 4", daPosicaoTres, jogo.getAreaMelhorias().get(4));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void compraNaoRevelaNovaMelhoria() throws JogadaInvalidaException {
		iniciarTurnoVerdeComDuasMoedas();
		jogo.comprarMelhoria(4);
		assertNull("O espaco mais a esquerda deve ficar vazio ate o Khan reabastecer", jogo.getMelhoriaNaArea(0));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void melhoriaCompradaNaoAlteraAsAcoesDoTurnoCorrente() throws JogadaInvalidaException {
		iniciarTurnoVerdeComDuasMoedas();
		jogo.comprarMelhoria(1);
		assertEquals("A moeda virtual comprada para a coluna verde so vale na proxima ativacao", 0, jogo.getAcoes().getVirtuais(TipoTributo.moeda));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void completarUmaLinhaGeraMelhoriaGratuitaPendente() throws JogadaInvalidaException {
		completarLinhaComprandoMelhoriaAmarela();
		assertEquals("A primeira linha das colunas coloridas foi completada", 1, jogo.getMelhoriasGratuitasPendentes());
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void finalizarTurnoComMelhoriaGratuitaPendenteLancaExcecao() throws JogadaInvalidaException {
		completarLinhaComprandoMelhoriaAmarela();
		jogo.finalizarTurno();
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void melhoriaGratuitaVaiParaAColunaBranca() throws JogadaInvalidaException {
		completarLinhaComprandoMelhoriaAmarela();
		jogo.escolherMelhoriaGratuita(Jogo.MELHORIA_DA_PILHA);
		assertEquals("A melhoria gratuita ocupa a coluna branca", 1, jogo.getQuantidadeDeMelhorias(Cor.vermelho, Coluna.branca));
	}

	@Test(timeout = DEFAULT_TIMEOUT, expected = JogadaInvalidaException.class)
	public void comprarParaColunaCompletaLancaExcecao() throws JogadaInvalidaException {
		colocarMelhorias(Cor.vermelho, Coluna.verde, TipoMelhoria.punho, 4);
		iniciarTurnoVerdeComDuasMoedas();
		jogo.comprarMelhoria(1);
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void melhoriaDeDoisCavalosAumentaOsMovimentosDaColuna() throws JogadaInvalidaException {
		colocarMelhorias(Cor.vermelho, Coluna.vermelha, TipoMelhoria.doisCavalos, 1);
		jogo.iniciarTurno(Coluna.vermelha);
		assertEquals("1 cavalo impresso mais 2 da melhoria", 3, jogo.getMovimentosRestantes());
	}
}
