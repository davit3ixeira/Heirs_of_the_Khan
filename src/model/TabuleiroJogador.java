package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class TabuleiroJogador {
	private static final Coluna[] COLORIDAS = new Coluna[] { Coluna.vermelha, Coluna.verde, Coluna.amarela };

	private final Map<Coluna, List<Melhoria>> colunas = new HashMap<Coluna, List<Melhoria>>();
	private final Set<Coluna> ativadas = new HashSet<Coluna>();
	private final int espacosImpressosNaBranca;

	TabuleiroJogador(int espacosImpressosNaBranca) {
		this.espacosImpressosNaBranca = espacosImpressosNaBranca;
		for (Coluna coluna : Coluna.values()) {
			colunas.put(coluna, new ArrayList<Melhoria>());
		}
	}

	boolean colunaDisponivel(Coluna coluna) {
		return !ativadas.contains(coluna);
	}

	AcoesTurno ativar(Coluna coluna) throws JogadaInvalidaException {
		if (ativadas.contains(coluna)) {
			throw new JogadaInvalidaException("A coluna " + coluna + " ja tem uma peca de ativacao.");
		}
		AcoesTurno acoes = Regras.acoesImpressas(coluna);
		for (Melhoria melhoria : colunas.get(coluna)) {
			melhoria.aplicar(acoes);
		}
		ativadas.add(coluna);
		if (acoes.isRecuperacao()) {
			ativadas.clear();
		}
		return acoes;
	}

	int capacidade(Coluna coluna) {
		if (coluna == Coluna.branca) {
			return Regras.MAX_MELHORIAS_POR_COLUNA - espacosImpressosNaBranca;
		}
		return Regras.MAX_MELHORIAS_POR_COLUNA;
	}

	boolean cabe(Coluna coluna) {
		return colunas.get(coluna).size() < capacidade(coluna);
	}

	boolean colocarMelhoria(Melhoria melhoria) throws JogadaInvalidaException {
		Coluna coluna = melhoria.getCor();
		if (!cabe(coluna)) {
			throw new JogadaInvalidaException("A coluna " + coluna + " ja esta completa.");
		}
		int linhasAntes = linhasCompletas();
		colunas.get(coluna).add(melhoria);
		int linhasDepois = linhasCompletas();
		return linhasDepois > linhasAntes && linhasDepois > espacosImpressosNaBranca;
	}

	void colocarNaBranca(Melhoria melhoria) throws JogadaInvalidaException {
		if (!cabe(Coluna.branca)) {
			throw new JogadaInvalidaException("A coluna branca ja esta completa.");
		}
		colunas.get(Coluna.branca).add(melhoria);
	}

	int linhasCompletas() {
		int linhas = Regras.MAX_MELHORIAS_POR_COLUNA;
		for (Coluna coluna : COLORIDAS) {
			linhas = Math.min(linhas, colunas.get(coluna).size());
		}
		return linhas;
	}

	List<Melhoria> getMelhorias(Coluna coluna) {
		return colunas.get(coluna);
	}

	int quantidade(Coluna coluna) {
		return colunas.get(coluna).size();
	}

	List<Melhoria> todasAsMelhorias() {
		List<Melhoria> todas = new ArrayList<Melhoria>();
		for (Coluna coluna : Coluna.values()) {
			todas.addAll(colunas.get(coluna));
		}
		return todas;
	}
}
