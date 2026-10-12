package model;

class CartaAbertaLinhaDeMelhorias extends CartaConselheiroAberto {
	boolean cumprida(Jogador jogador, Mapa mapa) {
		return jogador.getTabuleiro().linhasCompletas() >= 1;
	}

	String descricao() {
		return "Completar uma linha de melhorias";
	}
}
