package model;

import java.util.ArrayList;
import java.util.List;

class Parada {
	private final String id;
	private final Regiao regiao;
	private final boolean dupla;
	private final boolean karakorum;
	private final List<Parada> vizinhas = new ArrayList<Parada>();
	private final List<Provincia> provincias = new ArrayList<Provincia>();
	private final List<Cidade> cidades = new ArrayList<Cidade>();
	private final List<Jogador> yurts = new ArrayList<Jogador>();
	private Conselheiro conselheiro;

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

	boolean isDupla() {
		return dupla;
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

	boolean ehVizinha(Parada parada) {
		return vizinhas.contains(parada);
	}

	List<Provincia> getProvincias() {
		return provincias;
	}

	void adicionarProvincia(Provincia provincia) {
		provincias.add(provincia);
	}

	List<Cidade> getCidades() {
		return cidades;
	}

	void adicionarCidade(Cidade cidade) {
		cidades.add(cidade);
	}

	Conselheiro getConselheiro() {
		return conselheiro;
	}

	void setConselheiro(Conselheiro conselheiro) {
		this.conselheiro = conselheiro;
	}

	List<Jogador> getYurts() {
		return yurts;
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

	int capacidadeDePeoes() {
		if (karakorum) {
			return Regras.MAX_JOGADORES;
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

	int yurtsDe(Jogador jogador) {
		int total = 0;
		for (Jogador dono : yurts) {
			if (dono == jogador) {
				total++;
			}
		}
		return total;
	}
}
