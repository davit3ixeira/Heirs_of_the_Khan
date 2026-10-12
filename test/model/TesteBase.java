package model;

import org.junit.Before;

public abstract class TesteBase {
	protected static final int DEFAULT_TIMEOUT = 2000;

	protected Jogo jogo;
	protected Mapa mapa;

	@Before
	public void prepararPartidaDeDoisJogadores() throws JogadaInvalidaException {
		mapa = criarMapaDeTeste();
		jogo = new Jogo(mapa, new Cor[] { Cor.vermelho, Cor.azul });
	}

	static Mapa criarMapaDeTeste() {
		Mapa mapa = new Mapa();
		mapa.adicionarKarakorum();
		mapa.adicionarParada("A", Regiao.russia, false);
		mapa.adicionarParada("B", Regiao.russia, false);
		mapa.adicionarParada("D", Regiao.china, true);
		mapa.adicionarParada("E", Regiao.china, false);
		mapa.adicionarParada("F", Regiao.persia, false);
		mapa.ligar(Mapa.KARAKORUM, "A");
		mapa.ligar("A", "B");
		mapa.ligar("B", "D");
		mapa.ligar("D", "E");
		mapa.ligar("E", "F");
		mapa.ligar("F", Mapa.KARAKORUM);
		mapa.adicionarProvincia("pa", TipoTributo.espada, new String[] { Mapa.KARAKORUM, "A" });
		mapa.adicionarProvincia("pb", TipoTributo.yurt, new String[] { "A" });
		mapa.adicionarProvincia("pd", TipoTributo.espada, new String[] { "B", "D" });
		mapa.adicionarProvincia("pf", TipoTributo.moeda, new String[] { "F", Mapa.KARAKORUM });
		mapa.adicionarCidade("c1", Regiao.russia, new String[] { "A" });
		mapa.adicionarCidade("c2", Regiao.china, new String[] { "D" });
		mapa.adicionarCidade("c3", Regiao.china, new String[] { "E" });
		mapa.adicionarCidade("c4", Regiao.persia, new String[] { "F" });
		mapa.definirDemanda("D", new DemandaDoisIguais(TipoTesouro.pele));
		mapa.definirDemanda(Mapa.KARAKORUM, new DemandaTresEspecificos(TipoTesouro.pele, TipoTesouro.ferro, TipoTesouro.boi));
		return mapa;
	}

	protected void posicionar(Cor cor, String parada) {
		jogo.getJogador(cor).setPosicao(mapa.getParada(parada));
	}

	protected void darTributos(Cor cor, TipoTributo tipo, int quantidade) {
		jogo.getJogador(cor).adicionarTributos(tipo, quantidade);
	}

	protected void darTesouros(Cor cor, TipoTesouro tipo, int quantidade) {
		jogo.getJogador(cor).adicionarTesouros(tipo, quantidade);
	}

	protected void colocarYurt(Cor cor, String parada) {
		mapa.getParada(parada).colocarYurt(jogo.getJogador(cor));
	}

	protected void jogarTurnoSemAcoes(Coluna coluna) throws JogadaInvalidaException {
		jogo.iniciarTurno(coluna);
		jogo.finalizarTurno();
	}
}
