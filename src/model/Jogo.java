package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Jogo {
	static final int MIN_JOGADORES = 2;
	static final int MAX_JOGADORES = 5;
	static final int TESOUROS_POR_TIPO = 8;
	static final int TESOUROS_POR_CIDADE = 4;
	static final int CIDADES_REVELADAS = 3;

	private final Mapa mapa;
	private final boolean embaralhar;
	private final List<Jogador> jogadores = new ArrayList<Jogador>();
	private final Map<Cor, Jogador> jogadoresPorCor = new HashMap<Cor, Jogador>();
	private final List<TipoTesouro> pilhaDeTesouros = new ArrayList<TipoTesouro>();
	private final List<Cidade> pilhaDeCidades = new ArrayList<Cidade>();
	private int vez;
	private Turno turno;
	private int votosTotais;
	private boolean fimDisparado;

	public Jogo(Cor[] cores) throws JogadaInvalidaException {
		this(MapaPadrao.criar(), cores, true);
	}

	Jogo(Mapa mapa, Cor[] cores) throws JogadaInvalidaException {
		this(mapa, cores, false);
	}

	private Jogo(Mapa mapa, Cor[] cores, boolean embaralhar) throws JogadaInvalidaException {
		validar(cores);
		this.mapa = mapa;
		this.embaralhar = embaralhar;
		for (int i = 0; i < cores.length; i++) {
			Jogador jogador = new Jogador(cores[i], mapa.getKarakorum());
			jogador.adicionarTributos(TipoTributo.moeda, moedasIniciais(i));
			jogadores.add(jogador);
			jogadoresPorCor.put(cores[i], jogador);
		}
		for (TipoTesouro tipo : TipoTesouro.values()) {
			for (int i = 0; i < TESOUROS_POR_TIPO; i++) {
				pilhaDeTesouros.add(tipo);
			}
		}
		pilhaDeCidades.addAll(mapa.getCidades());
		for (int i = 0; i < CIDADES_REVELADAS; i++) {
			revelarProximaCidade();
		}
		for (Provincia provincia : mapa.getProvincias()) {
			provincia.adicionarPeca();
		}
	}

	private static void validar(Cor[] cores) throws JogadaInvalidaException {
		if (cores == null || cores.length < MIN_JOGADORES || cores.length > MAX_JOGADORES) {
			throw new JogadaInvalidaException("A partida precisa de " + MIN_JOGADORES + " a " + MAX_JOGADORES + " jogadores.");
		}
		Set<Cor> usadas = new HashSet<Cor>();
		for (Cor cor : cores) {
			if (cor == null || !usadas.add(cor)) {
				throw new JogadaInvalidaException("Cada jogador precisa de uma cor diferente.");
			}
		}
	}

	private static int moedasIniciais(int ordem) {
		if (ordem < 2) {
			return 1;
		}
		return 2;
	}

	private static int limiteDeVotos(int jogadores) {
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

	private int sortear(int n) {
		if (!embaralhar) {
			return 0;
		}
		return (int) (Math.random() * n);
	}

	private void revelarProximaCidade() {
		if (pilhaDeCidades.isEmpty()) {
			return;
		}
		Cidade cidade = pilhaDeCidades.remove(sortear(pilhaDeCidades.size()));
		List<TipoTesouro> tesouros = new ArrayList<TipoTesouro>();
		for (int i = 0; i < TESOUROS_POR_CIDADE && !pilhaDeTesouros.isEmpty(); i++) {
			tesouros.add(pilhaDeTesouros.remove(sortear(pilhaDeTesouros.size())));
		}
		cidade.revelar(tesouros);
	}

	public void iniciarTurno(Coluna coluna) throws JogadaInvalidaException {
		if (turno != null) {
			throw new JogadaInvalidaException("Ja existe um turno em andamento.");
		}
		Jogador jogador = jogadores.get(vez);
		if (!jogador.colunaDisponivel(coluna)) {
			throw new JogadaInvalidaException("A coluna " + coluna + " ja foi usada; ative a coluna branca para libera-la.");
		}
		jogador.usarColuna(coluna);
		turno = new Turno(jogador, coluna);
	}

	public void mover(String destino) throws JogadaInvalidaException {
		exigirTurno();
		Jogador jogador = turno.getJogador();
		Parada alvo = paradaExistente(destino);
		if (turno.getMovimentos() < 1) {
			throw new JogadaInvalidaException("Nao ha movimentos restantes neste turno.");
		}
		if (!mapa.alcancaveisComUmMovimento(jogador.getPosicao(), jogador).contains(alvo)) {
			throw new JogadaInvalidaException("A parada " + destino + " nao e alcancavel com um movimento.");
		}
		jogador.setPosicao(alvo);
		turno.registrarMovimento(alvo);
	}

	public void pegarTributo(String idProvincia) throws JogadaInvalidaException {
		exigirTurno();
		Provincia provincia = mapa.getProvincia(idProvincia);
		if (provincia == null) {
			throw new JogadaInvalidaException("Provincia inexistente: " + idProvincia + ".");
		}
		if (turno.getAcoesDeTributo() < 1) {
			throw new JogadaInvalidaException("Nao ha acoes de tributo restantes.");
		}
		if (!provincia.adjacenteA(turno.getVisitadas())) {
			throw new JogadaInvalidaException("A provincia " + idProvincia + " nao e adjacente a uma parada visitada.");
		}
		if (provincia.getPecas() < 1) {
			throw new JogadaInvalidaException("A provincia " + idProvincia + " nao tem tributos.");
		}
		provincia.retirarPeca();
		turno.consumirAcaoDeTributo();
		turno.getJogador().adicionarTributos(provincia.getIcone(), 1);
	}

	public void atacarCidade(String nome, TipoTesouro tesouro) throws JogadaInvalidaException {
		exigirTurno();
		Jogador jogador = turno.getJogador();
		Cidade cidade = mapa.getCidade(nome);
		if (cidade == null || !cidade.isRevelada() || cidade.getTesouros().isEmpty()) {
			throw new JogadaInvalidaException("A cidade " + nome + " nao tem tesouros para atacar.");
		}
		if (!cidade.adjacenteA(turno.getVisitadas())) {
			throw new JogadaInvalidaException("A cidade " + nome + " nao e adjacente a uma parada visitada.");
		}
		if (!cidade.getTesouros().contains(tesouro)) {
			throw new JogadaInvalidaException("A cidade " + nome + " nao tem o tesouro " + tesouro + ".");
		}
		int custo = 2;
		if (turno.ataquesEm(cidade) == 0) {
			custo = 1;
		}
		if (jogador.getTributos(TipoTributo.espada) < custo) {
			throw new JogadaInvalidaException("Sao necessarias " + custo + " espadas para este ataque.");
		}
		jogador.adicionarTributos(TipoTributo.espada, -custo);
		cidade.getTesouros().remove(tesouro);
		jogador.adicionarTesouros(tesouro, 1);
		turno.registrarAtaque(cidade);
		if (cidade.getTesouros().isEmpty()) {
			cidade.conquistar(jogador);
			jogador.retirarYurtDaReserva();
			revelarProximaCidade();
		}
	}

	public void construirYurt(String idParada) throws JogadaInvalidaException {
		exigirTurno();
		Jogador jogador = turno.getJogador();
		Parada parada = paradaExistente(idParada);
		if (parada.isKarakorum()) {
			throw new JogadaInvalidaException("Nao e permitido construir yurt em Karakorum.");
		}
		if (!turno.getVisitadas().contains(parada)) {
			throw new JogadaInvalidaException("A parada " + idParada + " nao foi visitada neste turno.");
		}
		if (!parada.cabeYurt()) {
			throw new JogadaInvalidaException("A parada " + idParada + " nao comporta mais yurts.");
		}
		if (jogador.getTributos(TipoTributo.yurt) < 1) {
			throw new JogadaInvalidaException("E necessario 1 tributo de yurt.");
		}
		if (!jogador.retirarYurtDaReserva()) {
			throw new JogadaInvalidaException("Nao ha yurts na reserva.");
		}
		jogador.adicionarTributos(TipoTributo.yurt, -1);
		parada.colocarYurt(jogador);
	}

	public void entregarTesouros(TipoTesouro[] tesouros) throws JogadaInvalidaException {
		exigirTurno();
		Jogador jogador = turno.getJogador();
		Demanda demanda = jogador.getPosicao().getDemanda();
		if (demanda == null) {
			throw new JogadaInvalidaException("O peao nao esta numa parada de conselheiro.");
		}
		Map<TipoTesouro, Integer> entregues = new HashMap<TipoTesouro, Integer>();
		for (TipoTesouro tesouro : tesouros) {
			entregues.put(tesouro, Demanda.quantidade(entregues, tesouro) + 1);
		}
		for (TipoTesouro tipo : entregues.keySet()) {
			if (jogador.getTesouros(tipo) < entregues.get(tipo)) {
				throw new JogadaInvalidaException("O jogador nao possui tesouros suficientes do tipo " + tipo + ".");
			}
		}
		if (!demanda.atende(entregues)) {
			throw new JogadaInvalidaException("Os tesouros entregues nao atendem ao pedido do conselheiro.");
		}
		for (TipoTesouro tipo : entregues.keySet()) {
			jogador.adicionarTesouros(tipo, -entregues.get(tipo));
		}
		ganharVotos(jogador, demanda.votos());
	}

	public void finalizarTurno() throws JogadaInvalidaException {
		exigirTurno();
		Parada posicao = turno.getJogador().getPosicao();
		if (peoesEm(posicao) > posicao.capacidadeDePeoes()) {
			throw new JogadaInvalidaException("O peao nao pode terminar o turno na parada ocupada " + posicao.getId() + ".");
		}
		turno = null;
		vez = (vez + 1) % jogadores.size();
	}

	void ganharVotos(Jogador jogador, int votos) {
		jogador.adicionarVotos(votos);
		votosTotais += votos;
		if (votosTotais >= limiteDeVotos(jogadores.size())) {
			fimDisparado = true;
		}
	}

	private int peoesEm(Parada parada) {
		int peoes = 0;
		for (Jogador jogador : jogadores) {
			if (jogador.getPosicao() == parada) {
				peoes++;
			}
		}
		return peoes;
	}

	private void exigirTurno() throws JogadaInvalidaException {
		if (turno == null) {
			throw new JogadaInvalidaException("Nenhum turno em andamento.");
		}
	}

	private Parada paradaExistente(String id) throws JogadaInvalidaException {
		Parada parada = mapa.getParada(id);
		if (parada == null) {
			throw new JogadaInvalidaException("Parada inexistente: " + id + ".");
		}
		return parada;
	}

	public Cor getJogadorDaVez() {
		return jogadores.get(vez).getCor();
	}

	public String getPosicao(Cor cor) {
		return jogadoresPorCor.get(cor).getPosicao().getId();
	}

	public int getMovimentosRestantes() {
		if (turno == null) {
			return 0;
		}
		return turno.getMovimentos();
	}

	public int getTributos(Cor cor, TipoTributo tipo) {
		return jogadoresPorCor.get(cor).getTributos(tipo);
	}

	public int getTesouros(Cor cor, TipoTesouro tipo) {
		return jogadoresPorCor.get(cor).getTesouros(tipo);
	}

	public int getVotos(Cor cor) {
		return jogadoresPorCor.get(cor).getVotos();
	}

	public int getYurtsNaReserva(Cor cor) {
		return jogadoresPorCor.get(cor).getYurtsNaReserva();
	}

	public boolean isColunaDisponivel(Cor cor, Coluna coluna) {
		return jogadoresPorCor.get(cor).colunaDisponivel(coluna);
	}

	public int getPecasNaProvincia(String id) {
		return mapa.getProvincia(id).getPecas();
	}

	public int getYurtsNaParada(String id) {
		return mapa.getParada(id).quantidadeDeYurts();
	}

	public int getTesourosNaCidade(String nome) {
		return mapa.getCidade(nome).getTesouros().size();
	}

	public boolean isCidadeRevelada(String nome) {
		return mapa.getCidade(nome).isRevelada();
	}

	public Cor getConquistador(String nome) {
		Jogador conquistador = mapa.getCidade(nome).getConquistador();
		if (conquistador == null) {
			return null;
		}
		return conquistador.getCor();
	}

	public boolean isFimDisparado() {
		return fimDisparado;
	}

	Jogador getJogador(Cor cor) {
		return jogadoresPorCor.get(cor);
	}

	Mapa getMapa() {
		return mapa;
	}
}
