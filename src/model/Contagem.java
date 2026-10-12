package model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

class Contagem {
	private Contagem() {
	}

	static int yurtsNaRegiao(Jogador jogador, Regiao regiao, Mapa mapa) {
		int total = 0;
		for (Parada parada : mapa.getParadas()) {
			if (parada.getRegiao() == regiao) {
				total += parada.yurtsDe(jogador);
			}
		}
		for (Cidade cidade : mapa.getCidades()) {
			if (cidade.getRegiao() == regiao && temYurtNaCidade(jogador, cidade)) {
				total++;
			}
		}
		return total;
	}

	static int yurtsNoMapa(Jogador jogador, Mapa mapa) {
		int total = 0;
		for (Parada parada : mapa.getParadas()) {
			total += parada.yurtsDe(jogador);
		}
		for (Cidade cidade : mapa.getCidades()) {
			if (temYurtNaCidade(jogador, cidade)) {
				total++;
			}
		}
		return total;
	}

	static boolean temYurtNaCidade(Jogador jogador, Cidade cidade) {
		return cidade.getConquistador() == jogador && cidade.isYurtColocado();
	}

	static int yurtsAdjacentes(Jogador jogador, List<Parada> paradas) {
		int total = 0;
		for (Parada parada : paradas) {
			total += parada.yurtsDe(jogador);
		}
		return total;
	}

	static int melhoriasNaRegiao(Jogador jogador, Regiao regiao) {
		return jogador.getTabuleiro().quantidade(Regras.colunaDaRegiao(regiao));
	}

	static int maiorRedeDeYurts(Jogador jogador, Mapa mapa) {
		Set<Parada> visitadas = new HashSet<Parada>();
		int maior = 0;
		for (Parada inicio : mapa.getParadas()) {
			if (inicio.temYurtDe(jogador) && !visitadas.contains(inicio)) {
				maior = Math.max(maior, tamanhoDaRede(jogador, inicio, visitadas));
			}
		}
		return maior;
	}

	private static int tamanhoDaRede(Jogador jogador, Parada inicio, Set<Parada> visitadas) {
		int yurts = 0;
		Queue<Parada> fila = new LinkedList<Parada>();
		fila.offer(inicio);
		visitadas.add(inicio);
		while (!fila.isEmpty()) {
			Parada atual = fila.poll();
			yurts += atual.yurtsDe(jogador);
			for (Parada vizinha : atual.getVizinhas()) {
				if (vizinha.temYurtDe(jogador) && !visitadas.contains(vizinha)) {
					visitadas.add(vizinha);
					fila.offer(vizinha);
				}
			}
		}
		return yurts;
	}
}
