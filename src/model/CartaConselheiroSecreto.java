package model;

abstract class CartaConselheiroSecreto {
	private final int[] limites;

	CartaConselheiroSecreto(int primeiro, int segundo, int terceiro) {
		limites = new int[] { primeiro, segundo, terceiro };
	}

	int getLimite(int indice) {
		return limites[indice];
	}

	int votos(Jogador jogador, Mapa mapa) {
		int medida = medir(jogador, mapa);
		int votos = 0;
		for (int limite : limites) {
			if (medida >= limite) {
				votos++;
			}
		}
		return votos;
	}

	abstract int medir(Jogador jogador, Mapa mapa);

	abstract String nome();
}
