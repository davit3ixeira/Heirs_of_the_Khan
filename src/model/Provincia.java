package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class Provincia {
	private final String id;
	private final TipoTributo icone;
	private final boolean doKhan;
	private int pecas;
	private final List<Parada> paradas = new ArrayList<Parada>();
	private final List<Provincia> vizinhasDoKhan = new ArrayList<Provincia>();

	Provincia(String id, TipoTributo icone, boolean doKhan) {
		this.id = id;
		this.icone = icone;
		this.doKhan = doKhan;
	}

	String getId() {
		return id;
	}

	TipoTributo getIcone() {
		return icone;
	}

	boolean isDoKhan() {
		return doKhan;
	}

	int getPecas() {
		return pecas;
	}

	boolean cheia() {
		return pecas >= Regras.MAX_PECAS_POR_PROVINCIA;
	}

	int adicionarPecas(int n) {
		int adicionadas = Math.min(n, Regras.MAX_PECAS_POR_PROVINCIA - pecas);
		pecas += adicionadas;
		return adicionadas;
	}

	void retirarPeca() {
		pecas--;
	}

	List<Parada> getParadas() {
		return paradas;
	}

	void adicionarParada(Parada parada) {
		paradas.add(parada);
	}

	boolean adjacenteA(Set<Parada> conjunto) {
		for (Parada parada : paradas) {
			if (conjunto.contains(parada)) {
				return true;
			}
		}
		return false;
	}

	List<Provincia> getVizinhasDoKhan() {
		return vizinhasDoKhan;
	}

	void adicionarVizinhaDoKhan(Provincia provincia) {
		vizinhasDoKhan.add(provincia);
	}
}
