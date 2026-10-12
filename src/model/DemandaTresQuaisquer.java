package model;

import java.util.Map;

class DemandaTresQuaisquer extends Demanda {
	boolean atende(Map<TipoTesouro, Integer> entregues) {
		return total(entregues) == 3;
	}

	int votos() {
		return 1;
	}
}
