package model;

class CartaMensageiro extends CartaConselheiroSecreto {
	CartaMensageiro() {
		super(2, 4, 6);
	}

	int medir(Jogador jogador, Mapa mapa) {
		int paradas = 0;
		for (Parada parada : mapa.getParadas()) {
			if (parada.isDupla() && parada.temYurtDe(jogador)) {
				paradas++;
			}
		}
		return paradas;
	}

	String nome() {
		return "Mensageiro";
	}
}
