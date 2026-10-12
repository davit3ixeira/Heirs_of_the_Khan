package model;

class CartaAbertaCidades extends CartaConselheiroAberto {
	CartaAbertaCidades(int meta) {
		super(meta);
	}

	boolean cumprida(Jogador jogador, Mapa mapa) {
		return jogador.getCidadesConquistadas().size() >= meta;
	}

	String descricao() {
		return "Conquistar " + meta + " cidades";
	}
}
