package model;

class CartaSoldado extends CartaConselheiroSecreto {
	CartaSoldado() {
		super(4, 7, 10);
	}

	int medir(Jogador jogador, Mapa mapa) {
		return jogador.getTabuleiro().todasAsMelhorias().size();
	}

	String nome() {
		return "Soldado";
	}
}
