package model;

import java.util.HashMap;
import java.util.Map;

class CartaSacerdote extends CartaConselheiroSecreto {
	CartaSacerdote() {
		super(1, 2, 4);
	}

	int medir(Jogador jogador, Mapa mapa) {
		Map<String, Integer> porSimbolo = new HashMap<String, Integer>();
		for (Melhoria melhoria : jogador.getTabuleiro().todasAsMelhorias()) {
			Integer atual = porSimbolo.get(melhoria.simbolo());
			if (atual == null) {
				atual = 0;
			}
			porSimbolo.put(melhoria.simbolo(), atual + 1);
		}
		int pares = 0;
		for (Integer quantidade : porSimbolo.values()) {
			pares += quantidade / 2;
		}
		return pares;
	}

	String nome() {
		return "Sacerdote";
	}
}
