package model;

class CartaAbertaColunaCompleta extends CartaConselheiroAberto {
	boolean cumprida(Jogador jogador, Mapa mapa) {
		for (Coluna coluna : Coluna.values()) {
			if (jogador.getTabuleiro().quantidade(coluna) >= Regras.MAX_MELHORIAS_POR_COLUNA) {
				return true;
			}
		}
		return false;
	}

	String descricao() {
		return "Ter 4 melhorias numa so coluna";
	}
}
