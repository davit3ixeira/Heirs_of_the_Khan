package model;

class CartaCidadao extends CartaConselheiroSecreto {
	CartaCidadao() {
		super(3, 6, 8);
	}

	int medir(Jogador jogador, Mapa mapa) {
		int cidades = 0;
		for (Cidade cidade : mapa.getCidades()) {
			if (Contagem.yurtsAdjacentes(jogador, cidade.getParadas()) >= 1) {
				cidades++;
			}
		}
		return cidades;
	}

	String nome() {
		return "Cidadao";
	}
}
