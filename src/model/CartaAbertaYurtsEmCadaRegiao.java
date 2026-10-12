package model;

class CartaAbertaYurtsEmCadaRegiao extends CartaConselheiroAberto {
	CartaAbertaYurtsEmCadaRegiao(int meta) {
		super(meta);
	}

	boolean cumprida(Jogador jogador, Mapa mapa) {
		for (Regiao regiao : Regiao.values()) {
			if (Contagem.yurtsNaRegiao(jogador, regiao, mapa) < meta) {
				return false;
			}
		}
		return true;
	}

	String descricao() {
		return "Ter " + meta + " yurts em cada regiao";
	}
}
