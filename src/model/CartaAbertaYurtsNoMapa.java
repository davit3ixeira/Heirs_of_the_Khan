package model;

class CartaAbertaYurtsNoMapa extends CartaConselheiroAberto {
	CartaAbertaYurtsNoMapa(int meta) {
		super(meta);
	}

	boolean cumprida(Jogador jogador, Mapa mapa) {
		return Contagem.yurtsNoMapa(jogador, mapa) >= meta;
	}

	String descricao() {
		return "Ter " + meta + " yurts no mapa";
	}
}
