package model;

class Melhoria {
	private final TipoMelhoria tipo;
	private final Coluna cor;
	private final TipoTesouro tesouro;

	Melhoria(TipoMelhoria tipo, Coluna cor) {
		this(tipo, cor, null);
	}

	Melhoria(TipoMelhoria tipo, Coluna cor, TipoTesouro tesouro) {
		this.tipo = tipo;
		this.cor = cor;
		this.tesouro = tesouro;
	}

	TipoMelhoria getTipo() {
		return tipo;
	}

	Coluna getCor() {
		return cor;
	}

	TipoTesouro getTesouro() {
		return tesouro;
	}

	String simbolo() {
		if (tipo == TipoMelhoria.tesouroVirtual) {
			return tipo + "_" + tesouro;
		}
		return tipo.toString();
	}

	boolean temCavaloOuPunho() {
		return tipo == TipoMelhoria.punho || tipo == TipoMelhoria.doisCavalos;
	}

	void aplicar(AcoesTurno acoes) {
		switch (tipo) {
		case moedaVirtual:
			acoes.adicionarVirtuais(TipoTributo.moeda, 1);
			break;
		case espadaVirtual:
			acoes.adicionarVirtuais(TipoTributo.espada, 1);
			break;
		case yurtVirtual:
			acoes.adicionarVirtuais(TipoTributo.yurt, 1);
			break;
		case tesouroVirtual:
			acoes.adicionarTesouroVirtual(tesouro);
			break;
		case punho:
			acoes.adicionarPunhos(1);
			break;
		case doisCavalos:
			acoes.adicionarMovimentos(2);
			break;
		}
	}
}
