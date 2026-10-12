package model;

class CartaAbertaTesouros extends CartaConselheiroAberto {
	CartaAbertaTesouros(int meta) {
		super(meta);
	}

	boolean cumprida(Jogador jogador, Mapa mapa) {
		return jogador.totalDeTesouros() >= meta;
	}

	String descricao() {
		return "Ter " + meta + " tesouros";
	}
}
