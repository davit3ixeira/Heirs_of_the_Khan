package model;

import java.util.ArrayList;
import java.util.List;

class AreaMelhorias {
	static final int ESPACOS = 5;
	static final int ESPACO_MOEDA = 0;
	static final int ESPACO_ESPADA = 2;

	private final List<Melhoria> espacos = new ArrayList<Melhoria>();
	private final Pilha<Melhoria> pilha;

	AreaMelhorias(Pilha<Melhoria> pilha) {
		this.pilha = pilha;
		for (int i = 0; i < ESPACOS; i++) {
			espacos.add(null);
		}
		reabastecer();
	}

	boolean posicaoValida(int posicao) {
		return posicao >= 0 && posicao < ESPACOS;
	}

	Melhoria get(int posicao) {
		return espacos.get(posicao);
	}

	boolean vazio(int posicao) {
		return espacos.get(posicao) == null;
	}

	Melhoria retirar(int posicao) {
		Melhoria retirada = espacos.get(posicao);
		for (int i = posicao; i > 0; i--) {
			espacos.set(i, espacos.get(i - 1));
		}
		espacos.set(0, null);
		return retirada;
	}

	Melhoria topoDaPilha() {
		return pilha.topo();
	}

	Melhoria retirarDaPilha() {
		return pilha.retirar();
	}

	int tamanhoDaPilha() {
		return pilha.tamanho();
	}

	void reabastecer() {
		for (int i = 0; i < ESPACOS; i++) {
			if (espacos.get(i) == null && !pilha.vazia()) {
				espacos.set(i, pilha.retirar());
			}
		}
	}
}
