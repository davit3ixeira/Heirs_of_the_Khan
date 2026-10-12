package model;

import java.util.HashSet;
import java.util.Set;

class CartaColecionador extends CartaConselheiroSecreto {
	CartaColecionador() {
		super(4, 6, 7);
	}

	int medir(Jogador jogador, Mapa mapa) {
		Set<String> simbolos = new HashSet<String>();
		for (Melhoria melhoria : jogador.getTabuleiro().todasAsMelhorias()) {
			simbolos.add(melhoria.simbolo());
		}
		return simbolos.size();
	}

	String nome() {
		return "Colecionador";
	}
}
