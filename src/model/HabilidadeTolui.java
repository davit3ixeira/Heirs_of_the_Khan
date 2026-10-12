package model;

import java.util.Set;

class HabilidadeTolui extends HabilidadeHerdeiro {
	boolean comecaComYurtEmKarakorum() {
		return true;
	}

	boolean podeConstruirEm(Parada alvo, Parada posicao, Set<Parada> visitadas) {
		return visitadas.contains(alvo) || posicao.ehVizinha(alvo);
	}
}
