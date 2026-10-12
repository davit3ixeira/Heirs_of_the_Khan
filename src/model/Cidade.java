package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class Cidade {
	private final String nome;
	private final Regiao regiao;
	private final List<Parada> paradas = new ArrayList<Parada>();
	private final List<TipoTesouro> tesouros = new ArrayList<TipoTesouro>();
	private boolean revelada;
	private Jogador conquistador;

	Cidade(String nome, Regiao regiao) {
		this.nome = nome;
		this.regiao = regiao;
	}

	String getNome() {
		return nome;
	}

	Regiao getRegiao() {
		return regiao;
	}

	boolean isRevelada() {
		return revelada;
	}

	void revelar(List<TipoTesouro> novosTesouros) {
		revelada = true;
		tesouros.addAll(novosTesouros);
	}

	List<TipoTesouro> getTesouros() {
		return tesouros;
	}

	Jogador getConquistador() {
		return conquistador;
	}

	void conquistar(Jogador jogador) {
		conquistador = jogador;
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
