package model;

import java.util.Map;

class DemandaDoisIguais extends Demanda {
	private final TipoTesouro tipo;

	DemandaDoisIguais(TipoTesouro tipo) {
		this.tipo = tipo;
	}

	boolean atende(Map<TipoTesouro, Integer> entregues) {
		return total(entregues) == 2 && quantidade(entregues, tipo) == 2;
	}

	int votos() {
		return 1;
	}
}
