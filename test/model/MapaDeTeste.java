package model;

class MapaDeTeste {
	private MapaDeTeste() {
	}

	static Mapa criar() {
		Mapa mapa = new Mapa();
		mapa.adicionarKarakorum();
		mapa.adicionarParada("A", Regiao.russia, false);
		mapa.adicionarParada("B", Regiao.russia, false);
		mapa.adicionarParada("D", Regiao.china, true);
		mapa.adicionarParada("E", Regiao.china, false);
		mapa.adicionarParada("F", Regiao.persia, false);
		mapa.ligar(Mapa.KARAKORUM, "A");
		mapa.ligar("A", "B");
		mapa.ligar("B", "D");
		mapa.ligar("D", "E");
		mapa.ligar("E", "F");
		mapa.ligar("F", Mapa.KARAKORUM);
		mapa.adicionarProvincia("pa", TipoTributo.espada, false, new String[] { Mapa.KARAKORUM, "A" });
		mapa.adicionarProvincia("pb", TipoTributo.yurt, false, new String[] { "A" });
		mapa.adicionarProvincia("pc", TipoTributo.moeda, false, new String[] { "A", "B" });
		mapa.adicionarProvincia("pd", TipoTributo.espada, false, new String[] { "B", "D" });
		mapa.adicionarProvincia("pk", TipoTributo.moeda, true, new String[] { "D", "E" });
		mapa.adicionarProvincia("pe", TipoTributo.yurt, false, new String[] { "E", "F" });
		mapa.adicionarProvincia("pf", TipoTributo.moeda, false, new String[] { "F", Mapa.KARAKORUM });
		mapa.definirVizinhasDoKhan("pk", "pd", "pe");
		mapa.adicionarCidade("c1", Regiao.russia, new String[] { "A" });
		mapa.adicionarCidade("c2", Regiao.china, new String[] { "D" });
		mapa.adicionarCidade("c3", Regiao.china, new String[] { "E" });
		mapa.adicionarCidade("c4", Regiao.persia, new String[] { "F" });
		mapa.adicionarConselheiro("D", new DemandaDoisIguais(TipoTesouro.pele));
		mapa.adicionarConselheiro(Mapa.KARAKORUM, new DemandaTresEspecificos(TipoTesouro.pele, TipoTesouro.ferro, TipoTesouro.boi));
		return mapa;
	}
}
