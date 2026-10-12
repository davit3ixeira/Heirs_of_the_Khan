package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ApuracaoFinal {
	private static final int CRITERIO_VOTOS = 0;
	private static final int CRITERIO_TESOUROS = 1;
	private static final int CRITERIO_PROXIMIDADE = 2;

	private ApuracaoFinal() {
	}

	static Map<Jogador, Integer> votosDeInfluencia(List<Jogador> jogadores, Mapa mapa) {
		Map<Jogador, Integer> votos = new HashMap<Jogador, Integer>();
		for (Jogador jogador : jogadores) {
			votos.put(jogador, 0);
		}
		for (Regiao regiao : Regiao.values()) {
			influenciaNaRegiao(jogadores, mapa, regiao, votos);
		}
		return votos;
	}

	private static void influenciaNaRegiao(List<Jogador> jogadores, Mapa mapa, Regiao regiao, Map<Jogador, Integer> votos) {
		Map<Jogador, Integer> yurts = new HashMap<Jogador, Integer>();
		int maior = 0;
		for (Jogador jogador : jogadores) {
			int quantidade = Contagem.yurtsNaRegiao(jogador, regiao, mapa);
			yurts.put(jogador, quantidade);
			maior = Math.max(maior, quantidade);
		}
		if (maior == 0) {
			return;
		}
		int primeiros = 0;
		for (Jogador jogador : jogadores) {
			if (yurts.get(jogador) == maior) {
				somar(votos, jogador, Regras.INFLUENCIA_PRIMEIRO[indiceDaTabela(jogador, regiao)]);
				primeiros++;
			}
		}
		if (primeiros > 1) {
			return;
		}
		int segundo = 0;
		for (Jogador jogador : jogadores) {
			int quantidade = yurts.get(jogador);
			if (quantidade < maior && quantidade > segundo) {
				segundo = quantidade;
			}
		}
		if (segundo == 0) {
			return;
		}
		for (Jogador jogador : jogadores) {
			if (yurts.get(jogador) == segundo) {
				somar(votos, jogador, Regras.INFLUENCIA_SEGUNDO[indiceDaTabela(jogador, regiao)]);
			}
		}
	}

	private static int indiceDaTabela(Jogador jogador, Regiao regiao) {
		return Math.min(Contagem.melhoriasNaRegiao(jogador, regiao), Regras.INFLUENCIA_PRIMEIRO.length - 1);
	}

	private static void somar(Map<Jogador, Integer> votos, Jogador jogador, int n) {
		votos.put(jogador, votos.get(jogador) + n);
	}

	static int votosDaCartaSecreta(Jogador jogador, Mapa mapa) {
		CartaConselheiroSecreto carta = jogador.getCartaSecreta();
		if (carta == null) {
			return 0;
		}
		return carta.votos(jogador, mapa);
	}

	static void aplicar(List<Jogador> jogadores, Mapa mapa) {
		Map<Jogador, Integer> influencia = votosDeInfluencia(jogadores, mapa);
		for (Jogador jogador : jogadores) {
			jogador.adicionarVotos(influencia.get(jogador) + votosDaCartaSecreta(jogador, mapa));
		}
	}

	static List<Jogador> vencedores(List<Jogador> jogadores, Mapa mapa) {
		List<Jogador> candidatos = new ArrayList<Jogador>(jogadores);
		int[] criterios = new int[] { CRITERIO_VOTOS, CRITERIO_TESOUROS, CRITERIO_PROXIMIDADE };
		for (int criterio : criterios) {
			candidatos = melhoresPorCriterio(candidatos, criterio, mapa);
		}
		return candidatos;
	}

	private static List<Jogador> melhoresPorCriterio(List<Jogador> candidatos, int criterio, Mapa mapa) {
		int melhor = Integer.MIN_VALUE;
		for (Jogador jogador : candidatos) {
			melhor = Math.max(melhor, valor(jogador, criterio, mapa));
		}
		List<Jogador> melhores = new ArrayList<Jogador>();
		for (Jogador jogador : candidatos) {
			if (valor(jogador, criterio, mapa) == melhor) {
				melhores.add(jogador);
			}
		}
		return melhores;
	}

	private static int valor(Jogador jogador, int criterio, Mapa mapa) {
		switch (criterio) {
		case CRITERIO_VOTOS:
			return jogador.getVotos();
		case CRITERIO_TESOUROS:
			return jogador.totalDeTesouros();
		default:
			return -mapa.distancia(jogador.getPosicao(), mapa.getKarakorum());
		}
	}
}
