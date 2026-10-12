package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Jogador {
	private final Cor cor;
	private final Herdeiro herdeiro;
	private final HabilidadeHerdeiro habilidade;
	private final TabuleiroJogador tabuleiro;
	private Parada posicao;
	private final Map<TipoTributo, Integer> tributos = new HashMap<TipoTributo, Integer>();
	private final Map<TipoTesouro, Integer> tesouros = new HashMap<TipoTesouro, Integer>();
	private int yurtsNaReserva = Regras.YURTS_POR_JOGADOR;
	private int votos;
	private final List<Cidade> cidadesConquistadas = new ArrayList<Cidade>();
	private final List<TipoBonus> bonusObtidos = new ArrayList<TipoBonus>();
	private final List<TipoBonus> bonusDisponiveis = new ArrayList<TipoBonus>();
	private final List<CartaConselheiroSecreto> cartasSecretas = new ArrayList<CartaConselheiroSecreto>();
	private final List<CartaConselheiroAberto> cartasAbertas = new ArrayList<CartaConselheiroAberto>();

	Jogador(Cor cor, Herdeiro herdeiro) {
		this.cor = cor;
		this.herdeiro = herdeiro;
		habilidade = HabilidadeHerdeiro.para(herdeiro);
		tabuleiro = new TabuleiroJogador(habilidade.espacosImpressosNaBranca());
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

	Herdeiro getHerdeiro() {
		return herdeiro;
	}

	HabilidadeHerdeiro getHabilidade() {
		return habilidade;
	}

	TabuleiroJogador getTabuleiro() {
		return tabuleiro;
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

	void removerTributos(TipoTributo tipo, int n) {
		tributos.put(tipo, tributos.get(tipo) - n);
	}

	int getTesouros(TipoTesouro tipo) {
		return tesouros.get(tipo);
	}

	void adicionarTesouros(TipoTesouro tipo, int n) {
		tesouros.put(tipo, tesouros.get(tipo) + n);
	}

	void removerTesouros(TipoTesouro tipo, int n) {
		tesouros.put(tipo, tesouros.get(tipo) - n);
	}

	int totalDeTesouros() {
		int total = 0;
		for (Integer quantidade : tesouros.values()) {
			total += quantidade;
		}
		return total;
	}

	int tiposDeTesouro() {
		int tipos = 0;
		for (Integer quantidade : tesouros.values()) {
			if (quantidade > 0) {
				tipos++;
			}
		}
		return tipos;
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

	List<Cidade> getCidadesConquistadas() {
		return cidadesConquistadas;
	}

	void adicionarCidadeConquistada(Cidade cidade) {
		cidadesConquistadas.add(cidade);
	}

	List<TipoBonus> getBonusObtidos() {
		return bonusObtidos;
	}

	List<TipoBonus> getBonusDisponiveis() {
		return bonusDisponiveis;
	}

	void receberBonus(TipoBonus bonus, boolean imediato) {
		bonusObtidos.add(bonus);
		if (!imediato) {
			bonusDisponiveis.add(bonus);
		}
	}

	boolean usarBonus(TipoBonus bonus) {
		return bonusDisponiveis.remove(bonus);
	}

	List<CartaConselheiroSecreto> getCartasSecretas() {
		return cartasSecretas;
	}

	void receberCartaSecreta(CartaConselheiroSecreto carta) {
		cartasSecretas.add(carta);
	}

	boolean isCartaSecretaEscolhida() {
		return cartasSecretas.size() == 1;
	}

	void escolherCartaSecreta(int indice) {
		CartaConselheiroSecreto escolhida = cartasSecretas.get(indice);
		cartasSecretas.clear();
		cartasSecretas.add(escolhida);
	}

	CartaConselheiroSecreto getCartaSecreta() {
		if (!isCartaSecretaEscolhida()) {
			return null;
		}
		return cartasSecretas.get(0);
	}

	List<CartaConselheiroAberto> getCartasAbertas() {
		return cartasAbertas;
	}

	void adicionarCartaAberta(CartaConselheiroAberto carta) {
		cartasAbertas.add(carta);
	}
}
