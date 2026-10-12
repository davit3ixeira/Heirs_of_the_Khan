package model;

class CartaAbertaBonus extends CartaConselheiroAberto {
	CartaAbertaBonus(int meta) {
		super(meta);
	}

	boolean cumprida(Jogador jogador, Mapa mapa) {
		return jogador.getBonusObtidos().size() >= meta;
	}

	String descricao() {
		return "Conseguir " + meta + " pecas de bonus";
	}
}
