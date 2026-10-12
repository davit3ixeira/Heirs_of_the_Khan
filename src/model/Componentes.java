package model;

class Componentes {
	private static final Coluna[] CORES_DE_FUNDO = new Coluna[] { Coluna.vermelha, Coluna.verde, Coluna.amarela };

	private Componentes() {
	}

	static Pilha<TipoTesouro> criarTesouros() {
		Pilha<TipoTesouro> pilha = new Pilha<TipoTesouro>();
		for (TipoTesouro tipo : TipoTesouro.values()) {
			for (int i = 0; i < Regras.TESOUROS_POR_TIPO; i++) {
				pilha.adicionar(tipo);
			}
		}
		return pilha;
	}

	static Pilha<Melhoria> criarMelhorias() {
		Pilha<Melhoria> pilha = new Pilha<Melhoria>();
		int indice = 0;
		indice = adicionarMelhorias(pilha, TipoMelhoria.moedaVirtual, 8, indice);
		for (TipoTesouro tesouro : TipoTesouro.values()) {
			pilha.adicionar(new Melhoria(TipoMelhoria.tesouroVirtual, corDeFundo(indice), tesouro));
			indice++;
		}
		indice = adicionarMelhorias(pilha, TipoMelhoria.espadaVirtual, 8, indice);
		indice = adicionarMelhorias(pilha, TipoMelhoria.yurtVirtual, 8, indice);
		indice = adicionarMelhorias(pilha, TipoMelhoria.punho, 6, indice);
		adicionarMelhorias(pilha, TipoMelhoria.doisCavalos, 9, indice);
		return pilha;
	}

	private static int adicionarMelhorias(Pilha<Melhoria> pilha, TipoMelhoria tipo, int quantidade, int indice) {
		for (int i = 0; i < quantidade; i++) {
			pilha.adicionar(new Melhoria(tipo, corDeFundo(indice)));
			indice++;
		}
		return indice;
	}

	private static Coluna corDeFundo(int indice) {
		return CORES_DE_FUNDO[indice % CORES_DE_FUNDO.length];
	}

	static Pilha<TipoBonus> criarBonus() {
		Pilha<TipoBonus> pilha = new Pilha<TipoBonus>();
		pilha.adicionar(TipoBonus.duasEspadas);
		pilha.adicionar(TipoBonus.duasMoedas);
		pilha.adicionar(TipoBonus.doisYurts);
		pilha.adicionar(TipoBonus.tresMovimentosEUmPunho);
		pilha.adicionar(TipoBonus.doisPunhos);
		pilha.adicionar(TipoBonus.tresTesouros);
		pilha.adicionar(TipoBonus.voto);
		return pilha;
	}

	static Pilha<CartaConselheiroAberto> criarCartasAbertas() {
		Pilha<CartaConselheiroAberto> pilha = new Pilha<CartaConselheiroAberto>();
		pilha.adicionar(new CartaAbertaEspadas(4));
		pilha.adicionar(new CartaAbertaYurtsNoMapa(6));
		pilha.adicionar(new CartaAbertaYurtsNoMapa(10));
		pilha.adicionar(new CartaAbertaTesouros(4));
		pilha.adicionar(new CartaAbertaTesourosDiferentes(5));
		pilha.adicionar(new CartaAbertaYurtsEmCadaRegiao(1));
		pilha.adicionar(new CartaAbertaYurtsEmCadaRegiao(2));
		pilha.adicionar(new CartaAbertaBonus(2));
		pilha.adicionar(new CartaAbertaBonus(3));
		pilha.adicionar(new CartaAbertaCidades(2));
		pilha.adicionar(new CartaAbertaCidades(3));
		pilha.adicionar(new CartaAbertaLinhaDeMelhorias());
		pilha.adicionar(new CartaAbertaDuasColunas());
		pilha.adicionar(new CartaAbertaColunaCompleta());
		return pilha;
	}

	static Pilha<CartaConselheiroSecreto> criarCartasSecretas() {
		Pilha<CartaConselheiroSecreto> pilha = new Pilha<CartaConselheiroSecreto>();
		pilha.adicionar(new CartaViajante());
		pilha.adicionar(new CartaSoldado());
		pilha.adicionar(new CartaColecionador());
		pilha.adicionar(new CartaSacerdote());
		pilha.adicionar(new CartaGovernador());
		pilha.adicionar(new CartaBajulador());
		pilha.adicionar(new CartaPrefeito());
		pilha.adicionar(new CartaCidadao());
		pilha.adicionar(new CartaCavaleiro());
		pilha.adicionar(new CartaMensageiro());
		return pilha;
	}
}
