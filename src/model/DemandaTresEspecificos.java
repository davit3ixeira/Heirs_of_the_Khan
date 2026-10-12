package model;

import java.util.Map;

class DemandaTresEspecificos extends Demanda {
	private final TipoTesouro[] tipos;

	DemandaTresEspecificos(TipoTesouro primeiro, TipoTesouro segundo, TipoTesouro terceiro) {
		tipos = new TipoTesouro[] { primeiro, segundo, terceiro };
	}

	boolean atende(Map<TipoTesouro, Integer> entregues) {
		if (total(entregues) != tipos.length) {
			return false;
		}
		for (TipoTesouro tipo : tipos) {
			if (quantidade(entregues, tipo) < 1) {
				return false;
			}
		}
		return true;
	}

	int votos() {
		return 2;
	}
}
