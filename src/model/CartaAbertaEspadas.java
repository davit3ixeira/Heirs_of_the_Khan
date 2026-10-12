package model;

class CartaAbertaEspadas extends CartaConselheiroAberto {
	CartaAbertaEspadas(int meta) {
		super(meta);
	}

	boolean cumprida(Jogador jogador, Mapa mapa) {
		return jogador.getTributos(TipoTributo.espada) >= meta;
	}

	String descricao() {
		return "Ter " + meta + " espadas";
	}
}
