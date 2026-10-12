package model;

class CartaCavaleiro extends CartaConselheiroSecreto {
	CartaCavaleiro() {
		super(2, 4, 5);
	}

	int medir(Jogador jogador, Mapa mapa) {
		int melhorias = 0;
		for (Melhoria melhoria : jogador.getTabuleiro().todasAsMelhorias()) {
			if (melhoria.temCavaloOuPunho()) {
				melhorias++;
			}
		}
		return melhorias;
	}

	String nome() {
		return "Cavaleiro";
	}
}
