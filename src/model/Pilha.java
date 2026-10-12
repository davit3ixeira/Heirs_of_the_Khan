package model;

import java.util.ArrayList;
import java.util.List;

class Pilha<X> {
	private final List<X> itens = new ArrayList<X>();

	void adicionar(X item) {
		itens.add(item);
	}

	X topo() {
		if (itens.isEmpty()) {
			return null;
		}
		return itens.get(0);
	}

	X retirar() {
		if (itens.isEmpty()) {
			return null;
		}
		return itens.remove(0);
	}

	boolean vazia() {
		return itens.isEmpty();
	}

	int tamanho() {
		return itens.size();
	}

	void embaralhar(Sorteador sorteador) {
		List<X> embaralhados = new ArrayList<X>();
		while (!itens.isEmpty()) {
			embaralhados.add(itens.remove(sorteador.sortear(itens.size())));
		}
		itens.addAll(embaralhados);
	}
}
