package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class Provincia {
	static final int MAX_PECAS = 3;

	private final String id;
	private final TipoTributo icone;
	private final List<Parada> paradas = new ArrayList<Parada>();
	private int pecas;

	Provincia(String id, TipoTributo icone) {
		this.id = id;
		this.icone = icone;
	}

	String getId() {
		return id;
	}

	TipoTributo getIcone() {
		return icone;
	}

	int getPecas() {
		return pecas;
	}

	void adicionarPeca() {
		if (pecas < MAX_PECAS) {
			pecas++;
		}
	}

	void retirarPeca() {
		pecas--;
	}

	void adicionarParada(Parada parada) {
		paradas.add(parada);
	}

	boolean adjacenteA(Set<Parada> visitadas) {
		for (Parada parada : paradas) {
			if (visitadas.contains(parada)) {
				return true;
			}
		}
		return false;
	}
}
