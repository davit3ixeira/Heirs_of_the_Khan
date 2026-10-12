package model;

class CartaGovernador extends CartaConselheiroSecreto {
	CartaGovernador() {
		super(1, 2, 3);
	}

	int medir(Jogador jogador, Mapa mapa) {
		int regioes = 0;
		for (Regiao regiao : Regiao.values()) {
			if (Contagem.yurtsNaRegiao(jogador, regiao, mapa) >= 2 && Contagem.melhoriasNaRegiao(jogador, regiao) >= 2) {
				regioes++;
			}
		}
		return regioes;
	}

	String nome() {
		return "Governador";
	}
}
