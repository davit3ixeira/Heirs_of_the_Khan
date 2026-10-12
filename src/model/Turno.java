package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Turno {
	private final Jogador jogador;
	private final Set<Parada> visitadas = new HashSet<Parada>();
	private final Map<Cidade, Integer> ataques = new HashMap<Cidade, Integer>();
	private int movimentos;
	private int acoesDeTributo;

	Turno(Jogador jogador, Coluna coluna) {
		this.jogador = jogador;
		visitadas.add(jogador.getPosicao());
		switch (coluna) {
		case vermelha:
			movimentos = 1;
			acoesDeTributo = 1;
			break;
		case verde:
			movimentos = 2;
			acoesDeTributo = 1;
			break;
		case amarela:
			movimentos = 1;
			acoesDeTributo = 2;
			break;
		case branca:
			movimentos = 2;
			acoesDeTributo = 0;
			break;
		}
	}

	Jogador getJogador() {
		return jogador;
	}

	Set<Parada> getVisitadas() {
		return visitadas;
	}

	int getMovimentos() {
		return movimentos;
	}

	void registrarMovimento(Parada destino) {
		movimentos--;
		visitadas.add(destino);
	}

	int getAcoesDeTributo() {
		return acoesDeTributo;
	}

	void consumirAcaoDeTributo() {
		acoesDeTributo--;
	}

	int ataquesEm(Cidade cidade) {
		Integer quantidade = ataques.get(cidade);
		if (quantidade == null) {
			return 0;
		}
		return quantidade;
	}

	void registrarAtaque(Cidade cidade) {
		ataques.put(cidade, ataquesEm(cidade) + 1);
	}
}
