package model;

class CartaAbertaTesourosDiferentes extends CartaConselheiroAberto {
	CartaAbertaTesourosDiferentes(int meta) {
		super(meta);
	}

	boolean cumprida(Jogador jogador, Mapa mapa) {
		return jogador.tiposDeTesouro() >= meta;
	}

	String descricao() {
		return "Ter " + meta + " tesouros diferentes";
	}
}
