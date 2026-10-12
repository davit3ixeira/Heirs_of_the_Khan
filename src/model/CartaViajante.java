package model;

class CartaViajante extends CartaConselheiroSecreto {
	CartaViajante() {
		super(3, 6, 8);
	}

	int medir(Jogador jogador, Mapa mapa) {
		return Contagem.maiorRedeDeYurts(jogador, mapa);
	}

	String nome() {
		return "Viajante";
	}
}
