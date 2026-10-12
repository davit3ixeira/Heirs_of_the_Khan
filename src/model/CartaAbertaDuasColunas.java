package model;

class CartaAbertaDuasColunas extends CartaConselheiroAberto {
	boolean cumprida(Jogador jogador, Mapa mapa) {
		int colunas = 0;
		for (Coluna coluna : Coluna.values()) {
			if (jogador.getTabuleiro().quantidade(coluna) >= 2) {
				colunas++;
			}
		}
		return colunas >= 2;
	}

	String descricao() {
		return "Ter 2 colunas com 2 melhorias cada";
	}
}
