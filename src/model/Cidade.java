package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class Cidade {
	private final String nome;
	private final Regiao regiao;
	private boolean revelada;
	private Jogador conquistador;
	private boolean yurtColocado;
	private final List<TipoTesouro> tesouros = new ArrayList<TipoTesouro>();
	private final List<Parada> paradas = new ArrayList<Parada>();

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

	boolean retirarTesouro(TipoTesouro tipo) {
		return tesouros.remove(tipo);
	}

	Jogador getConquistador() {
		return conquistador;
	}

	boolean isYurtColocado() {
		return yurtColocado;
	}

	void conquistar(Jogador jogador, boolean comYurt) {
		conquistador = jogador;
		yurtColocado = comYurt;
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
}
