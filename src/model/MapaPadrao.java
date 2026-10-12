package model;

class MapaPadrao {
	private MapaPadrao() {
	}

	static Mapa criar() {
		Mapa mapa = new Mapa();
		mapa.adicionarKarakorum();
		criarRegiao(mapa, "R", Regiao.russia, new String[] { "Kiev", "Moscou", "Sarai" }, TipoTesouro.pele);
		criarRegiao(mapa, "C", Regiao.china, new String[] { "Pequim", "Kaesong", "Cantao" }, TipoTesouro.boi);
		criarRegiao(mapa, "P", Regiao.persia, new String[] { "Samarcanda", "Bagda", "Cabul" }, TipoTesouro.seda);
		mapa.ligar("R2", "C4");
		mapa.ligar("C2", "P4");
		mapa.ligar("P2", "R4");
		mapa.definirDemanda(Mapa.KARAKORUM, new DemandaTresEspecificos(TipoTesouro.ferro, TipoTesouro.especiaria, TipoTesouro.seda));
		return mapa;
	}

	private static void criarRegiao(Mapa mapa, String prefixo, Regiao regiao, String[] cidades, TipoTesouro pedido) {
		for (int i = 1; i <= 4; i++) {
			mapa.adicionarParada(prefixo + i, regiao, i == 3);
		}
		for (int i = 1; i <= 4; i++) {
			mapa.ligar(prefixo + i, prefixo + (i % 4 + 1));
		}
		mapa.ligar(Mapa.KARAKORUM, prefixo + 1);
		String nome = regiao.toString();
		mapa.adicionarProvincia(nome + "_espada", TipoTributo.espada, new String[] { prefixo + 1, prefixo + 2 });
		mapa.adicionarProvincia(nome + "_yurt", TipoTributo.yurt, new String[] { prefixo + 2, prefixo + 3 });
		mapa.adicionarProvincia(nome + "_moeda", TipoTributo.moeda, new String[] { prefixo + 3, prefixo + 4 });
		for (int i = 0; i < cidades.length; i++) {
			mapa.adicionarCidade(cidades[i], regiao, new String[] { prefixo + (i + 1), prefixo + (i + 2) });
		}
		mapa.definirDemanda(prefixo + 3, new DemandaDoisIguais(pedido));
	}
}
