package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TesteCartasSecretas extends TesteBase {

	private void colocarCincoMelhorias() throws JogadaInvalidaException {
		colocarMelhorias(Cor.vermelho, Coluna.vermelha, TipoMelhoria.punho, 2);
		colocarMelhorias(Cor.vermelho, Coluna.verde, TipoMelhoria.doisCavalos, 2);
		colocarMelhorias(Cor.vermelho, Coluna.amarela, TipoMelhoria.moedaVirtual, 1);
	}

	private void colocarRedeDeTresYurtsEUmIsolado() {
		colocarYurt(Cor.vermelho, "A");
		colocarYurt(Cor.vermelho, "B");
		colocarYurt(Cor.vermelho, "D");
		colocarYurt(Cor.vermelho, "F");
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void soldadoMedeOTotalDeMelhorias() throws JogadaInvalidaException {
		colocarCincoMelhorias();
		assertEquals("O jogador tem 5 melhorias", 5, new CartaSoldado().medir(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void soldadoComCincoMelhoriasAtingeApenasOPrimeiroLimite() throws JogadaInvalidaException {
		colocarCincoMelhorias();
		assertEquals("Os limites sao 4, 7 e 10", 1, new CartaSoldado().votos(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void viajanteMedeOMaiorGrupoDeYurtsConectados() {
		colocarRedeDeTresYurtsEUmIsolado();
		assertEquals("A, B e D estao ligados; F esta isolado", 3, new CartaViajante().medir(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void viajanteContaOsDoisYurtsDeUmaParadaDupla() {
		colocarYurt(Cor.vermelho, "D");
		colocarYurt(Cor.vermelho, "D");
		colocarYurt(Cor.vermelho, "E");
		assertEquals("A parada dupla D tem 2 yurts ligados ao de E", 3, new CartaViajante().medir(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void viajanteNaoAtravessaYurtsDeOutroJogador() {
		colocarYurt(Cor.vermelho, "A");
		colocarYurt(Cor.azul, "B");
		colocarYurt(Cor.vermelho, "D");
		assertEquals("O yurt azul em B separa os yurts vermelhos", 1, new CartaViajante().medir(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void sacerdoteContaParesDeMelhoriasComOMesmoSimbolo() throws JogadaInvalidaException {
		colocarCincoMelhorias();
		assertEquals("Ha um par de punhos e um par de cavalos", 2, new CartaSacerdote().medir(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void sacerdoteNaoFormaParComTesourosVirtuaisDiferentes() throws JogadaInvalidaException {
		jogador(Cor.vermelho).getTabuleiro().colocarMelhoria(new Melhoria(TipoMelhoria.tesouroVirtual, Coluna.vermelha, TipoTesouro.pele));
		jogador(Cor.vermelho).getTabuleiro().colocarMelhoria(new Melhoria(TipoMelhoria.tesouroVirtual, Coluna.vermelha, TipoTesouro.seda));
		assertEquals("Tesouros diferentes contam como simbolos diferentes", 0, new CartaSacerdote().medir(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void colecionadorContaSimbolosDiferentes() throws JogadaInvalidaException {
		colocarCincoMelhorias();
		assertEquals("Punho, cavalos e moeda virtual", 3, new CartaColecionador().medir(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void cavaleiroContaMelhoriasComCavaloOuPunho() throws JogadaInvalidaException {
		colocarCincoMelhorias();
		assertEquals("2 punhos e 2 de cavalos", 4, new CartaCavaleiro().medir(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void mensageiroContaParadasDuplasComYurt() {
		colocarRedeDeTresYurtsEUmIsolado();
		assertEquals("Apenas D e parada dupla", 1, new CartaMensageiro().medir(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void cidadaoNaoContaYurtDentroDaCidade() {
		conquistarCidade(Cor.vermelho, "c1");
		assertEquals("O yurt no centro de c1 nao e adjacente a cidade", 0, new CartaCidadao().medir(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void bajuladorContaProvinciasDoKhanComDoisYurtsAdjacentes() {
		colocarYurt(Cor.vermelho, "D");
		colocarYurt(Cor.vermelho, "E");
		assertEquals("A provincia do Khan pk e adjacente a D e E", 1, new CartaBajulador().medir(jogador(Cor.vermelho), mapa));
	}
}
