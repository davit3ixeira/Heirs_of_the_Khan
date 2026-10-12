package model;

import java.util.Set;

class HabilidadeHerdeiro {
	int espacosImpressosNaBranca() {
		return 0;
	}

	boolean comecaComYurtEmKarakorum() {
		return false;
	}

	boolean podeConstruirEm(Parada alvo, Parada posicao, Set<Parada> visitadas) {
		return visitadas.contains(alvo);
	}

	static HabilidadeHerdeiro para(Herdeiro herdeiro) {
		switch (herdeiro) {
		case altani:
			return new HabilidadeAltani();
		case chagatai:
			return new HabilidadeChagatai();
		case jochi:
			return new HabilidadeJochi();
		case ogedei:
			return new HabilidadeOgedei();
		default:
			return new HabilidadeTolui();
		}
	}
}
