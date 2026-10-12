package model;

class CartaPrefeito extends CartaConselheiroSecreto {
	CartaPrefeito() {
		super(2, 3, 4);
	}

	int medir(Jogador jogador, Mapa mapa) {
		int cidades = 0;
		for (Cidade cidade : mapa.getCidades()) {
			if (Contagem.yurtsAdjacentes(jogador, cidade.getParadas()) >= 2) {
				cidades++;
			}
		}
		return cidades;
	}

	String nome() {
		return "Prefeito";
	}
}
