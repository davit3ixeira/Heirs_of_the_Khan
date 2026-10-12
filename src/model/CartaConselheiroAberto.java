package model;

abstract class CartaConselheiroAberto {
	protected final int meta;

	CartaConselheiroAberto() {
		this(0);
	}

	CartaConselheiroAberto(int meta) {
		this.meta = meta;
	}

	int getMeta() {
		return meta;
	}

	abstract boolean cumprida(Jogador jogador, Mapa mapa);

	abstract String descricao();
}
