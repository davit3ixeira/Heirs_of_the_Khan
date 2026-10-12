package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AcoesTurno {
	private int movimentos;
	private int punhos;
	private final Map<TipoTributo, Integer> virtuais = new HashMap<TipoTributo, Integer>();
	private final List<TipoTesouro> tesourosVirtuais = new ArrayList<TipoTesouro>();
	private boolean khan;
	private boolean khanUsado;
	private boolean recuperacao;

	AcoesTurno() {
		for (TipoTributo tipo : TipoTributo.values()) {
			virtuais.put(tipo, 0);
		}
	}

	int getMovimentos() {
		return movimentos;
	}

	void adicionarMovimentos(int n) {
		movimentos += n;
	}

	void consumirMovimento() {
		movimentos--;
	}

	int getPunhos() {
		return punhos;
	}

	void adicionarPunhos(int n) {
		punhos += n;
	}

	void consumirPunho() {
		punhos--;
	}

	int getVirtuais(TipoTributo tipo) {
		return virtuais.get(tipo);
	}

	void adicionarVirtuais(TipoTributo tipo, int n) {
		virtuais.put(tipo, virtuais.get(tipo) + n);
	}

	int consumirVirtuais(TipoTributo tipo, int n) {
		int consumidos = Math.min(n, virtuais.get(tipo));
		virtuais.put(tipo, virtuais.get(tipo) - consumidos);
		return consumidos;
	}

	List<TipoTesouro> getTesourosVirtuais() {
		return tesourosVirtuais;
	}

	void adicionarTesouroVirtual(TipoTesouro tipo) {
		tesourosVirtuais.add(tipo);
	}

	void usarTesouroVirtual(TipoTesouro tipo) {
		tesourosVirtuais.remove(tipo);
	}

	void exigirKhan() {
		khan = true;
	}

	boolean temKhan() {
		return khan;
	}

	boolean isKhanPendente() {
		return khan && !khanUsado;
	}

	void marcarKhanUsado() {
		khanUsado = true;
	}

	void marcarRecuperacao() {
		recuperacao = true;
	}

	boolean isRecuperacao() {
		return recuperacao;
	}
}
