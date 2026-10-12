package model;

class MapaPadrao {
	private MapaPadrao() {
	}

	static Mapa criar() {
		Mapa mapa = new Mapa();
		mapa.adicionarKarakorum();
		criarRegiao(mapa, "R", Regiao.russia, TipoTributo.espada);
		criarRegiao(mapa, "C", Regiao.china, TipoTributo.moeda);
		criarRegiao(mapa, "P", Regiao.persia, TipoTributo.yurt);
		mapa.ligar(Mapa.KARAKORUM, "R1");
		mapa.ligar(Mapa.KARAKORUM, "C1");
		mapa.ligar(Mapa.KARAKORUM, "P1");
		mapa.ligar("R4", "C2");
		mapa.ligar("C5", "P2");
		mapa.ligar("P5", "R2");
		mapa.adicionarProvincia("khan_central", TipoTributo.moeda, true, new String[] { Mapa.KARAKORUM, "R1", "C1", "P1" });
		mapa.definirVizinhasDoKhan("khan_central", "china_espada", "persia_moeda");
		criarCidades(mapa, "R", Regiao.russia, new String[] { "Kiev", "Moscou", "Sarai" });
		criarCidades(mapa, "C", Regiao.china, new String[] { "Pequim", "Kaesong", "Cantao" });
		criarCidades(mapa, "P", Regiao.persia, new String[] { "Samarcanda", "Bagda", "Cabul" });
		mapa.adicionarConselheiro("R3", new DemandaDoisIguais(TipoTesouro.pele));
		mapa.adicionarConselheiro("R6", new DemandaDoisIguais(TipoTesouro.ferro));
		mapa.adicionarConselheiro("C3", new DemandaDoisIguais(TipoTesouro.boi));
		mapa.adicionarConselheiro("C6", new DemandaDoisIguais(TipoTesouro.especiaria));
		mapa.adicionarConselheiro("P3", new DemandaDoisIguais(TipoTesouro.seda));
		mapa.adicionarConselheiro("P6", new DemandaTresQuaisquer());
		mapa.adicionarConselheiro(Mapa.KARAKORUM, new DemandaTresEspecificos(TipoTesouro.pele, TipoTesouro.especiaria, TipoTesouro.seda));
		return mapa;
	}

	private static void criarRegiao(Mapa mapa, String prefixo, Regiao regiao, TipoTributo iconeDoKhan) {
		for (int i = 1; i <= 6; i++) {
			mapa.adicionarParada(prefixo + i, regiao, i == 3 || i == 6);
		}
		for (int i = 1; i <= 6; i++) {
			mapa.ligar(prefixo + i, prefixo + (i % 6 + 1));
		}
		String nome = regiao.toString();
		mapa.adicionarProvincia(nome + "_espada", TipoTributo.espada, false, new String[] { prefixo + 1, prefixo + 2 });
		mapa.adicionarProvincia(nome + "_yurt", TipoTributo.yurt, false, new String[] { prefixo + 3, prefixo + 4 });
		mapa.adicionarProvincia(nome + "_moeda", TipoTributo.moeda, false, new String[] { prefixo + 5, prefixo + 6 });
		mapa.adicionarProvincia(nome + "_khan", iconeDoKhan, true, new String[] { prefixo + 1, prefixo + 3, prefixo + 5 });
		mapa.definirVizinhasDoKhan(nome + "_khan", nome + "_espada", nome + "_yurt");
	}

	private static void criarCidades(Mapa mapa, String prefixo, Regiao regiao, String[] nomes) {
		for (int i = 0; i < nomes.length; i++) {
			mapa.adicionarCidade(nomes[i], regiao, new String[] { prefixo + (2 * i + 1), prefixo + (2 * i + 2) });
		}
	}
}
