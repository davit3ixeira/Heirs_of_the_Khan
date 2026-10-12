package model;

import java.util.Map;

abstract class Demanda {
	abstract boolean atende(Map<TipoTesouro, Integer> entregues);

	abstract int votos();

	static int total(Map<TipoTesouro, Integer> entregues) {
		int soma = 0;
		for (Integer quantidade : entregues.values()) {
			soma += quantidade;
		}
		return soma;
	}

	static int quantidade(Map<TipoTesouro, Integer> entregues, TipoTesouro tipo) {
		Integer quantidade = entregues.get(tipo);
		if (quantidade == null) {
			return 0;
		}
		return quantidade;
	}
}
