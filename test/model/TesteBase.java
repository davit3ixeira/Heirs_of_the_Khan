package model;

import org.junit.Before;

public abstract class TesteBase {
	protected static final int DEFAULT_TIMEOUT = 2000;
	protected static final String KARAKORUM = Mapa.KARAKORUM;

	protected Jogo jogo;
	protected Mapa mapa;

	@Before
	public void prepararPartidaDeDoisJogadores() throws JogadaInvalidaException {
		jogo = criarJogo(new Cor[] { Cor.vermelho, Cor.azul }, new Herdeiro[] { Herdeiro.altani, Herdeiro.chagatai });
	}

	protected Jogo criarJogo(Cor[] cores, Herdeiro[] herdeiros) throws JogadaInvalidaException {
		mapa = MapaDeTeste.criar();
		Jogo novo = new Jogo(mapa, cores, herdeiros, new SorteadorFixo());
		for (Cor cor : cores) {
			novo.escolherCartaSecreta(cor, 0);
		}
		return novo;
	}

	protected Jogador jogador(Cor cor) {
		return jogo.getJogador(cor);
	}

	protected void posicionar(Cor cor, String parada) {
		jogador(cor).setPosicao(mapa.getParada(parada));
	}

	protected void darTributos(Cor cor, TipoTributo tipo, int quantidade) {
		jogo.receberDoEstoque(jogador(cor), tipo, quantidade);
	}

	protected void darTesouros(Cor cor, TipoTesouro tipo, int quantidade) {
		jogador(cor).adicionarTesouros(tipo, quantidade);
	}

	protected void colocarYurt(Cor cor, String parada) {
		mapa.getParada(parada).colocarYurt(jogador(cor));
	}

	protected void conquistarCidade(Cor cor, String cidade) {
		mapa.getCidade(cidade).conquistar(jogador(cor), true);
	}

	protected void colocarMelhorias(Cor cor, Coluna coluna, TipoMelhoria tipo, int quantidade) throws JogadaInvalidaException {
		for (int i = 0; i < quantidade; i++) {
			jogador(cor).getTabuleiro().colocarMelhoria(new Melhoria(tipo, coluna));
		}
	}

	protected void jogarTurnoSemAcoes(Coluna coluna) throws JogadaInvalidaException {
		jogo.iniciarTurno(coluna);
		jogo.finalizarTurno();
	}
}
