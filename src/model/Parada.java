package model;

import java.util.ArrayList;
import java.util.List;

class Parada {
	private final String id;
	private final Regiao regiao;
	private final boolean dupla;
	private final boolean karakorum;
	private final List<Parada> vizinhas = new ArrayList<Parada>();
	private final List<Jogador> yurts = new ArrayList<Jogador>();
	private Demanda demanda;

	Parada(String id, Regiao regiao, boolean dupla, boolean karakorum) {
		this.id = id;
		this.regiao = regiao;
		this.dupla = dupla;
		this.karakorum = karakorum;
	}

	String getId() {
		return id;
	}

	Regiao getRegiao() {
		return regiao;
	}

	boolean isKarakorum() {
		return karakorum;
	}

	List<Parada> getVizinhas() {
		return vizinhas;
	}

	void adicionarVizinha(Parada parada) {
		if (!vizinhas.contains(parada)) {
			vizinhas.add(parada);
		}
	}

	Demanda getDemanda() {
		return demanda;
	}

	void setDemanda(Demanda demanda) {
		this.demanda = demanda;
	}

	int capacidadeDePeoes() {
		if (karakorum) {
			return Jogo.MAX_JOGADORES;
		}
		if (dupla) {
			return 2;
		}
		return 1;
	}

	int capacidadeDeYurts() {
		if (karakorum) {
			return 0;
		}
		if (dupla) {
			return 2;
		}
		return 1;
	}

	boolean cabeYurt() {
		return yurts.size() < capacidadeDeYurts();
	}

	void colocarYurt(Jogador jogador) {
		yurts.add(jogador);
	}

	boolean temYurtDe(Jogador jogador) {
		return yurts.contains(jogador);
	}

	int quantidadeDeYurts() {
		return yurts.size();
	}
}
