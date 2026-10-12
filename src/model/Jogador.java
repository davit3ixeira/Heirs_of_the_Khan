package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Jogador {
	static final int YURTS_INICIAIS = 12;

	private final Cor cor;
	private final Map<TipoTributo, Integer> tributos = new HashMap<TipoTributo, Integer>();
	private final Map<TipoTesouro, Integer> tesouros = new HashMap<TipoTesouro, Integer>();
	private final Set<Coluna> colunasUsadas = new HashSet<Coluna>();
	private Parada posicao;
	private int yurtsNaReserva = YURTS_INICIAIS;
	private int votos;

	Jogador(Cor cor, Parada posicao) {
		this.cor = cor;
		this.posicao = posicao;
		for (TipoTributo tipo : TipoTributo.values()) {
			tributos.put(tipo, 0);
		}
		for (TipoTesouro tipo : TipoTesouro.values()) {
			tesouros.put(tipo, 0);
		}
	}

	Cor getCor() {
		return cor;
	}

	Parada getPosicao() {
		return posicao;
	}

	void setPosicao(Parada posicao) {
		this.posicao = posicao;
	}

	int getTributos(TipoTributo tipo) {
		return tributos.get(tipo);
	}

	void adicionarTributos(TipoTributo tipo, int n) {
		tributos.put(tipo, tributos.get(tipo) + n);
	}

	int getTesouros(TipoTesouro tipo) {
		return tesouros.get(tipo);
	}

	void adicionarTesouros(TipoTesouro tipo, int n) {
		tesouros.put(tipo, tesouros.get(tipo) + n);
	}

	int getYurtsNaReserva() {
		return yurtsNaReserva;
	}

	boolean retirarYurtDaReserva() {
		if (yurtsNaReserva < 1) {
			return false;
		}
		yurtsNaReserva--;
		return true;
	}

	int getVotos() {
		return votos;
	}

	void adicionarVotos(int n) {
		votos += n;
	}

	boolean colunaDisponivel(Coluna coluna) {
		return !colunasUsadas.contains(coluna);
	}

	void usarColuna(Coluna coluna) {
		if (coluna == Coluna.branca) {
			colunasUsadas.clear();
		} else {
			colunasUsadas.add(coluna);
		}
	}
}
