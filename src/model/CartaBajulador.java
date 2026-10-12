package model;

class CartaBajulador extends CartaConselheiroSecreto {
	CartaBajulador() {
		super(2, 3, 4);
	}

	int medir(Jogador jogador, Mapa mapa) {
		int provincias = 0;
		for (Provincia provincia : mapa.getProvincias()) {
			if (provincia.isDoKhan() && Contagem.yurtsAdjacentes(jogador, provincia.getParadas()) >= 2) {
				provincias++;
			}
		}
		return provincias;
	}

	String nome() {
		return "Bajulador";
	}
}
