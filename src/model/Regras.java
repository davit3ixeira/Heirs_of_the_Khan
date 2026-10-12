package model;

class Regras {
	static final int MIN_JOGADORES = 2;
	static final int MAX_JOGADORES = 5;
	static final int YURTS_POR_JOGADOR = 12;
	static final int CARTAS_SECRETAS_POR_JOGADOR = 2;
	static final int TESOUROS_POR_TIPO = 8;
	static final int TESOUROS_POR_CIDADE = 4;
	static final int CIDADES_REVELADAS = 3;
	static final int CARTAS_ABERTAS_REVELADAS = 4;
	static final int MAX_PECAS_POR_PROVINCIA = 3;
	static final int MAX_MELHORIAS_POR_COLUNA = 4;
	static final int CUSTO_MELHORIA = 2;
	static final int CUSTO_PRIMEIRO_ATAQUE = 1;
	static final int CUSTO_ATAQUE_SEGUINTE = 2;
	static final int TESOUROS_DO_BONUS = 3;
	static final int[] INFLUENCIA_PRIMEIRO = new int[] { 1, 1, 2, 2, 3 };
	static final int[] INFLUENCIA_SEGUNDO = new int[] { 0, 1, 1, 2, 2 };

	private Regras() {
	}

	static int limiteDeVotos(int jogadores) {
		switch (jogadores) {
		case 2:
			return 10;
		case 3:
			return 14;
		case 4:
			return 16;
		default:
			return 17;
		}
	}

	static int moedasIniciais(int ordem) {
		if (ordem < 2) {
			return 1;
		}
		return 2;
	}

	static int estoqueInicial(TipoTributo tipo) {
		switch (tipo) {
		case espada:
			return 10;
		case yurt:
			return 10;
		default:
			return 15;
		}
	}

	static Coluna colunaDaRegiao(Regiao regiao) {
		switch (regiao) {
		case china:
			return Coluna.vermelha;
		case persia:
			return Coluna.verde;
		default:
			return Coluna.amarela;
		}
	}

	static AcoesTurno acoesImpressas(Coluna coluna) {
		AcoesTurno acoes = new AcoesTurno();
		switch (coluna) {
		case vermelha:
			acoes.adicionarMovimentos(1);
			acoes.adicionarPunhos(1);
			acoes.adicionarVirtuais(TipoTributo.moeda, 1);
			break;
		case verde:
			acoes.adicionarMovimentos(2);
			acoes.adicionarPunhos(1);
			break;
		case amarela:
			acoes.adicionarMovimentos(1);
			acoes.adicionarPunhos(2);
			acoes.exigirKhan();
			break;
		case branca:
			acoes.adicionarMovimentos(2);
			acoes.exigirKhan();
			acoes.marcarRecuperacao();
			break;
		}
		return acoes;
	}
}
