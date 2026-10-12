package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Jogo {
	static final String AREA_DE_MELHORIAS = "areaDeMelhorias";
	static final int MELHORIA_DA_PILHA = -1;

	private final Mapa mapa;
	private final Sorteador sorteador;
	private final List<Jogador> jogadores = new ArrayList<Jogador>();
	private final Map<Cor, Jogador> jogadoresPorCor = new HashMap<Cor, Jogador>();
	private final Map<TipoTributo, Integer> estoque = new HashMap<TipoTributo, Integer>();
	private final Pilha<TipoTesouro> pilhaDeTesouros;
	private final Pilha<Cidade> pilhaDeCidades = new Pilha<Cidade>();
	private final AreaMelhorias areaMelhorias;
	private final List<CartaConselheiroAberto> cartasAbertas = new ArrayList<CartaConselheiroAberto>();
	private final Set<Parada> visitadas = new HashSet<Parada>();
	private final Map<Cidade, Integer> ataquesNoTurno = new HashMap<Cidade, Integer>();
	private final List<TipoTesouro> tesourosDoBonus = new ArrayList<TipoTesouro>();
	private String posicaoKhan;
	private int votosNeutros;
	private int vez;
	private AcoesTurno acoes;
	private int melhoriasGratuitasPendentes;
	private boolean fimDisparado;
	private int voltasAposDisparo;
	private boolean terminado;
	private Cor[] vencedores;

	public Jogo(Cor[] cores, Herdeiro[] herdeiros) throws JogadaInvalidaException {
		this(MapaPadrao.criar(), cores, herdeiros, new SorteadorAleatorio());
	}

	Jogo(Mapa mapa, Cor[] cores, Herdeiro[] herdeiros, Sorteador sorteador) throws JogadaInvalidaException {
		validar(cores, herdeiros);
		this.mapa = mapa;
		this.sorteador = sorteador;
		for (TipoTributo tipo : TipoTributo.values()) {
			estoque.put(tipo, Regras.estoqueInicial(tipo));
		}
		pilhaDeTesouros = Componentes.criarTesouros();
		pilhaDeTesouros.embaralhar(sorteador);
		Pilha<Melhoria> melhorias = Componentes.criarMelhorias();
		melhorias.embaralhar(sorteador);
		areaMelhorias = new AreaMelhorias(melhorias);
		criarJogadores(cores, herdeiros);
		prepararCidades();
		distribuirBonus();
		revelarCartasAbertas();
		colocarTributosIniciais();
		distribuirMoedasIniciais();
		distribuirCartasSecretas();
	}

	private static void validar(Cor[] cores, Herdeiro[] herdeiros) throws JogadaInvalidaException {
		if (cores == null || herdeiros == null) {
			throw new JogadaInvalidaException("Cores e herdeiros sao obrigatorios.");
		}
		if (cores.length < Regras.MIN_JOGADORES || cores.length > Regras.MAX_JOGADORES) {
			throw new JogadaInvalidaException("O jogo aceita de " + Regras.MIN_JOGADORES + " a " + Regras.MAX_JOGADORES + " jogadores.");
		}
		if (cores.length != herdeiros.length) {
			throw new JogadaInvalidaException("O numero de cores e de herdeiros deve ser o mesmo.");
		}
		Set<Cor> coresUsadas = new HashSet<Cor>();
		Set<Herdeiro> herdeirosUsados = new HashSet<Herdeiro>();
		for (int i = 0; i < cores.length; i++) {
			if (cores[i] == null || !coresUsadas.add(cores[i])) {
				throw new JogadaInvalidaException("Cada jogador precisa de uma cor diferente.");
			}
			if (herdeiros[i] == null || !herdeirosUsados.add(herdeiros[i])) {
				throw new JogadaInvalidaException("Cada jogador precisa de um herdeiro diferente.");
			}
		}
	}

	private void criarJogadores(Cor[] cores, Herdeiro[] herdeiros) {
		for (int i = 0; i < cores.length; i++) {
			Jogador jogador = new Jogador(cores[i], herdeiros[i]);
			jogador.setPosicao(mapa.getKarakorum());
			if (jogador.getHabilidade().comecaComYurtEmKarakorum() && jogador.retirarYurtDaReserva()) {
				mapa.getKarakorum().colocarYurt(jogador);
			}
			jogadores.add(jogador);
			jogadoresPorCor.put(cores[i], jogador);
		}
	}

	private void prepararCidades() {
		for (Cidade cidade : mapa.getCidades()) {
			pilhaDeCidades.adicionar(cidade);
		}
		pilhaDeCidades.embaralhar(sorteador);
		for (int i = 0; i < Regras.CIDADES_REVELADAS; i++) {
			revelarProximaCidade();
		}
	}

	private void revelarProximaCidade() {
		Cidade cidade = pilhaDeCidades.retirar();
		if (cidade == null) {
			return;
		}
		List<TipoTesouro> tesouros = new ArrayList<TipoTesouro>();
		for (int i = 0; i < Regras.TESOUROS_POR_CIDADE && !pilhaDeTesouros.vazia(); i++) {
			tesouros.add(pilhaDeTesouros.retirar());
		}
		cidade.revelar(tesouros);
	}

	private void distribuirBonus() {
		Pilha<TipoBonus> bonus = Componentes.criarBonus();
		bonus.embaralhar(sorteador);
		for (Conselheiro conselheiro : mapa.getConselheiros()) {
			conselheiro.setBonus(bonus.retirar());
		}
	}

	private void revelarCartasAbertas() {
		Pilha<CartaConselheiroAberto> cartas = Componentes.criarCartasAbertas();
		cartas.embaralhar(sorteador);
		for (int i = 0; i < Regras.CARTAS_ABERTAS_REVELADAS && !cartas.vazia(); i++) {
			cartasAbertas.add(cartas.retirar());
		}
	}

	private void colocarTributosIniciais() {
		for (Provincia provincia : mapa.getProvincias()) {
			abastecer(provincia);
		}
	}

	private void distribuirMoedasIniciais() {
		for (int i = 0; i < jogadores.size(); i++) {
			receberDoEstoque(jogadores.get(i), TipoTributo.moeda, Regras.moedasIniciais(i));
		}
	}

	private void distribuirCartasSecretas() {
		Pilha<CartaConselheiroSecreto> cartas = Componentes.criarCartasSecretas();
		cartas.embaralhar(sorteador);
		for (Jogador jogador : jogadores) {
			for (int i = 0; i < Regras.CARTAS_SECRETAS_POR_JOGADOR && !cartas.vazia(); i++) {
				jogador.receberCartaSecreta(cartas.retirar());
			}
		}
	}

	public void escolherCartaSecreta(Cor cor, int indice) throws JogadaInvalidaException {
		Jogador jogador = jogadorExistente(cor);
		if (jogador.isCartaSecretaEscolhida()) {
			throw new JogadaInvalidaException("O jogador " + cor + " ja escolheu sua carta secreta.");
		}
		if (indice < 0 || indice >= jogador.getCartasSecretas().size()) {
			throw new JogadaInvalidaException("Indice de carta secreta invalido: " + indice + ".");
		}
		jogador.escolherCartaSecreta(indice);
	}

	public void iniciarTurno(Coluna coluna) throws JogadaInvalidaException {
		if (terminado) {
			throw new JogadaInvalidaException("A partida ja terminou.");
		}
		if (acoes != null) {
			throw new JogadaInvalidaException("Ja existe um turno em andamento.");
		}
		for (Jogador jogador : jogadores) {
			if (!jogador.isCartaSecretaEscolhida()) {
				throw new JogadaInvalidaException("O jogador " + jogador.getCor() + " ainda nao escolheu sua carta secreta.");
			}
		}
		Jogador jogador = jogadorDaVez();
		acoes = jogador.getTabuleiro().ativar(coluna);
		visitadas.clear();
		visitadas.add(jogador.getPosicao());
		ataquesNoTurno.clear();
		verificarCartasAbertas();
	}

	public void finalizarTurno() throws JogadaInvalidaException {
		exigirTurno();
		if (acoes.isKhanPendente()) {
			throw new JogadaInvalidaException("O Khan precisa ser movido neste turno.");
		}
		if (melhoriasGratuitasPendentes > 0) {
			throw new JogadaInvalidaException("Ha uma melhoria gratuita a ser escolhida.");
		}
		if (!tesourosDoBonus.isEmpty()) {
			throw new JogadaInvalidaException("Escolha um dos tesouros da peca de bonus.");
		}
		Parada posicao = jogadorDaVez().getPosicao();
		if (peoesEm(posicao) > posicao.capacidadeDePeoes()) {
			throw new JogadaInvalidaException("O peao nao pode terminar o turno na parada ocupada " + posicao.getId() + ".");
		}
		acoes = null;
		visitadas.clear();
		ataquesNoTurno.clear();
		vez = (vez + 1) % jogadores.size();
		if (vez == 0 && fimDisparado) {
			voltasAposDisparo++;
			if (voltasAposDisparo == 2) {
				terminado = true;
			}
		}
	}

	public void mover(String destino) throws JogadaInvalidaException {
		exigirTurno();
		Jogador jogador = jogadorDaVez();
		Parada alvo = paradaExistente(destino);
		if (acoes.getMovimentos() < 1) {
			throw new JogadaInvalidaException("Nao ha movimentos restantes neste turno.");
		}
		if (!mapa.alcancaveisComUmMovimento(jogador.getPosicao(), jogador).contains(alvo)) {
			throw new JogadaInvalidaException("A parada " + destino + " nao e alcancavel com um movimento.");
		}
		acoes.consumirMovimento();
		jogador.setPosicao(alvo);
		visitadas.add(alvo);
		verificarCartasAbertas();
	}

	public void pegarTributo(String idProvincia) throws JogadaInvalidaException {
		exigirTurno();
		Provincia provincia = provinciaExistente(idProvincia);
		if (acoes.getPunhos() < 1) {
			throw new JogadaInvalidaException("Nao ha acoes de pegar tributo restantes.");
		}
		if (!provincia.adjacenteA(visitadas)) {
			throw new JogadaInvalidaException("A provincia " + idProvincia + " nao e adjacente a uma parada visitada.");
		}
		if (provincia.getPecas() < 1) {
			throw new JogadaInvalidaException("A provincia " + idProvincia + " nao tem tributos.");
		}
		provincia.retirarPeca();
		acoes.consumirPunho();
		jogadorDaVez().adicionarTributos(provincia.getIcone(), 1);
		verificarCartasAbertas();
	}

	public void moverKhanParaProvincia(String idProvincia) throws JogadaInvalidaException {
		exigirKhanDisponivel();
		Provincia provincia = provinciaExistente(idProvincia);
		if (!provincia.isDoKhan()) {
			throw new JogadaInvalidaException("A provincia " + idProvincia + " nao e uma provincia do Khan.");
		}
		if (idProvincia.equals(posicaoKhan)) {
			throw new JogadaInvalidaException("O Khan nao pode permanecer onde esta.");
		}
		posicaoKhan = idProvincia;
		abastecer(provincia);
		for (Provincia vizinha : provincia.getVizinhasDoKhan()) {
			abastecer(vizinha);
		}
		acoes.marcarKhanUsado();
		verificarCartasAbertas();
	}

	public void moverKhanParaAreaDeMelhorias() throws JogadaInvalidaException {
		exigirKhanDisponivel();
		if (AREA_DE_MELHORIAS.equals(posicaoKhan)) {
			throw new JogadaInvalidaException("O Khan nao pode permanecer onde esta.");
		}
		posicaoKhan = AREA_DE_MELHORIAS;
		Jogador jogador = jogadorDaVez();
		if (areaMelhorias.vazio(AreaMelhorias.ESPACO_MOEDA)) {
			receberDoEstoque(jogador, TipoTributo.moeda, 1);
		}
		if (areaMelhorias.vazio(AreaMelhorias.ESPACO_ESPADA)) {
			receberDoEstoque(jogador, TipoTributo.espada, 1);
		}
		areaMelhorias.reabastecer();
		acoes.marcarKhanUsado();
		verificarCartasAbertas();
	}

	public void atacarCidade(String nome, TipoTesouro tesouro) throws JogadaInvalidaException {
		exigirTurno();
		Cidade cidade = cidadeExistente(nome);
		if (!cidade.isRevelada() || cidade.getTesouros().isEmpty()) {
			throw new JogadaInvalidaException("A cidade " + nome + " nao tem tesouros.");
		}
		if (!cidade.adjacenteA(visitadas)) {
			throw new JogadaInvalidaException("A cidade " + nome + " nao e adjacente a uma parada visitada.");
		}
		if (!cidade.getTesouros().contains(tesouro)) {
			throw new JogadaInvalidaException("A cidade " + nome + " nao tem o tesouro " + tesouro + ".");
		}
		int ataques = quantidadeDeAtaques(cidade);
		int custo = Regras.CUSTO_ATAQUE_SEGUINTE;
		if (ataques == 0) {
			custo = Regras.CUSTO_PRIMEIRO_ATAQUE;
		}
		gastar(TipoTributo.espada, custo);
		Jogador jogador = jogadorDaVez();
		cidade.retirarTesouro(tesouro);
		jogador.adicionarTesouros(tesouro, 1);
		ataquesNoTurno.put(cidade, ataques + 1);
		if (cidade.getTesouros().isEmpty()) {
			conquistar(jogador, cidade);
		}
		verificarCartasAbertas();
	}

	private void conquistar(Jogador jogador, Cidade cidade) {
		cidade.conquistar(jogador, jogador.retirarYurtDaReserva());
		jogador.adicionarCidadeConquistada(cidade);
		revelarProximaCidade();
		if (todasAsCidadesConquistadas()) {
			fimDisparado = true;
		}
	}

	public void construirYurt(String idParada) throws JogadaInvalidaException {
		exigirTurno();
		Jogador jogador = jogadorDaVez();
		Parada parada = paradaExistente(idParada);
		if (parada.isKarakorum()) {
			throw new JogadaInvalidaException("Nao e permitido construir yurt em Karakorum.");
		}
		if (!jogador.getHabilidade().podeConstruirEm(parada, jogador.getPosicao(), visitadas)) {
			throw new JogadaInvalidaException("A parada " + idParada + " nao foi visitada neste turno.");
		}
		if (!parada.cabeYurt()) {
			throw new JogadaInvalidaException("A parada " + idParada + " nao comporta mais yurts.");
		}
		if (jogador.getYurtsNaReserva() < 1) {
			throw new JogadaInvalidaException("Nao ha yurts na reserva.");
		}
		gastar(TipoTributo.yurt, 1);
		jogador.retirarYurtDaReserva();
		parada.colocarYurt(jogador);
		verificarCartasAbertas();
	}

	public void comprarMelhoria(int posicao) throws JogadaInvalidaException {
		exigirTurno();
		if (!areaMelhorias.posicaoValida(posicao) || areaMelhorias.vazio(posicao)) {
			throw new JogadaInvalidaException("Nao ha melhoria na posicao " + posicao + ".");
		}
		Melhoria melhoria = areaMelhorias.get(posicao);
		exigirEspaco(melhoria.getCor());
		gastar(TipoTributo.moeda, Regras.CUSTO_MELHORIA);
		areaMelhorias.retirar(posicao);
		instalar(melhoria);
	}

	public void comprarMelhoriaDaPilha() throws JogadaInvalidaException {
		exigirTurno();
		Melhoria melhoria = areaMelhorias.topoDaPilha();
		if (melhoria == null) {
			throw new JogadaInvalidaException("A pilha de melhorias esta vazia.");
		}
		exigirEspaco(melhoria.getCor());
		gastar(TipoTributo.moeda, Regras.CUSTO_MELHORIA);
		areaMelhorias.retirarDaPilha();
		instalar(melhoria);
	}

	private void instalar(Melhoria melhoria) throws JogadaInvalidaException {
		if (jogadorDaVez().getTabuleiro().colocarMelhoria(melhoria)) {
			melhoriasGratuitasPendentes++;
		}
		verificarCartasAbertas();
	}

	public void escolherMelhoriaGratuita(int posicao) throws JogadaInvalidaException {
		exigirTurno();
		if (melhoriasGratuitasPendentes < 1) {
			throw new JogadaInvalidaException("Nao ha melhoria gratuita pendente.");
		}
		TabuleiroJogador tabuleiro = jogadorDaVez().getTabuleiro();
		if (!tabuleiro.cabe(Coluna.branca)) {
			throw new JogadaInvalidaException("A coluna branca ja esta completa.");
		}
		Melhoria melhoria;
		if (posicao == MELHORIA_DA_PILHA) {
			if (areaMelhorias.topoDaPilha() == null) {
				throw new JogadaInvalidaException("A pilha de melhorias esta vazia.");
			}
			melhoria = areaMelhorias.retirarDaPilha();
		} else {
			if (!areaMelhorias.posicaoValida(posicao) || areaMelhorias.vazio(posicao)) {
				throw new JogadaInvalidaException("Nao ha melhoria na posicao " + posicao + ".");
			}
			melhoria = areaMelhorias.retirar(posicao);
		}
		tabuleiro.colocarNaBranca(melhoria);
		melhoriasGratuitasPendentes--;
		verificarCartasAbertas();
	}

	public void entregarTesouros(TipoTesouro[] tesouros) throws JogadaInvalidaException {
		exigirTurno();
		Jogador jogador = jogadorDaVez();
		Conselheiro conselheiro = jogador.getPosicao().getConselheiro();
		if (conselheiro == null) {
			throw new JogadaInvalidaException("O peao nao esta numa parada de conselheiro.");
		}
		if (tesouros == null || tesouros.length == 0) {
			throw new JogadaInvalidaException("Nenhum tesouro foi informado.");
		}
		Map<TipoTesouro, Integer> entregues = contar(tesouros);
		for (Map.Entry<TipoTesouro, Integer> entrada : entregues.entrySet()) {
			if (jogador.getTesouros(entrada.getKey()) < entrada.getValue()) {
				throw new JogadaInvalidaException("O jogador nao possui tesouros suficientes do tipo " + entrada.getKey() + ".");
			}
		}
		Demanda demanda = conselheiro.getDemanda();
		if (!demanda.atende(entregues)) {
			TipoTesouro virtual = tesouroVirtualQueCompleta(demanda, entregues);
			if (virtual == null) {
				throw new JogadaInvalidaException("Os tesouros entregues nao atendem ao pedido do conselheiro.");
			}
			acoes.usarTesouroVirtual(virtual);
		}
		for (Map.Entry<TipoTesouro, Integer> entrada : entregues.entrySet()) {
			jogador.removerTesouros(entrada.getKey(), entrada.getValue());
		}
		ganharVotos(jogador, demanda.votos());
		TipoBonus bonus = conselheiro.retirarBonus();
		if (bonus != null) {
			receberBonus(jogador, bonus);
		}
		verificarCartasAbertas();
	}

	private Map<TipoTesouro, Integer> contar(TipoTesouro[] tesouros) {
		Map<TipoTesouro, Integer> contagem = new HashMap<TipoTesouro, Integer>();
		for (TipoTesouro tesouro : tesouros) {
			contagem.put(tesouro, Demanda.quantidade(contagem, tesouro) + 1);
		}
		return contagem;
	}

	private TipoTesouro tesouroVirtualQueCompleta(Demanda demanda, Map<TipoTesouro, Integer> entregues) {
		for (TipoTesouro virtual : acoes.getTesourosVirtuais()) {
			Map<TipoTesouro, Integer> comVirtual = new HashMap<TipoTesouro, Integer>(entregues);
			comVirtual.put(virtual, Demanda.quantidade(comVirtual, virtual) + 1);
			if (demanda.atende(comVirtual)) {
				return virtual;
			}
		}
		return null;
	}

	private void receberBonus(Jogador jogador, TipoBonus bonus) {
		boolean imediato = bonus == TipoBonus.voto;
		jogador.receberBonus(bonus, imediato);
		if (imediato) {
			ganharVotos(jogador, 1);
		}
	}

	public void usarBonus(TipoBonus bonus) throws JogadaInvalidaException {
		exigirTurno();
		Jogador jogador = jogadorDaVez();
		if (!jogador.getBonusDisponiveis().contains(bonus)) {
			throw new JogadaInvalidaException("O jogador nao tem a peca de bonus " + bonus + " disponivel.");
		}
		if (bonus == TipoBonus.tresTesouros && pilhaDeTesouros.vazia()) {
			throw new JogadaInvalidaException("A pilha de tesouros esta vazia.");
		}
		jogador.usarBonus(bonus);
		switch (bonus) {
		case duasEspadas:
			receberDoEstoque(jogador, TipoTributo.espada, 2);
			break;
		case duasMoedas:
			receberDoEstoque(jogador, TipoTributo.moeda, 2);
			break;
		case doisYurts:
			receberDoEstoque(jogador, TipoTributo.yurt, 2);
			break;
		case tresMovimentosEUmPunho:
			acoes.adicionarMovimentos(3);
			acoes.adicionarPunhos(1);
			break;
		case doisPunhos:
			acoes.adicionarPunhos(2);
			break;
		case tresTesouros:
			for (int i = 0; i < Regras.TESOUROS_DO_BONUS && !pilhaDeTesouros.vazia(); i++) {
				tesourosDoBonus.add(pilhaDeTesouros.retirar());
			}
			break;
		default:
			break;
		}
		verificarCartasAbertas();
	}

	public void escolherTesouroDoBonus(TipoTesouro tesouro) throws JogadaInvalidaException {
		exigirTurno();
		if (tesourosDoBonus.isEmpty()) {
			throw new JogadaInvalidaException("Nao ha tesouros do bonus para escolher.");
		}
		if (!tesourosDoBonus.contains(tesouro)) {
			throw new JogadaInvalidaException("O tesouro " + tesouro + " nao esta entre os sorteados.");
		}
		jogadorDaVez().adicionarTesouros(tesouro, 1);
		tesourosDoBonus.clear();
		verificarCartasAbertas();
	}

	public Cor[] apurarResultado() throws JogadaInvalidaException {
		if (!terminado) {
			throw new JogadaInvalidaException("A partida ainda nao terminou.");
		}
		if (vencedores == null) {
			ApuracaoFinal.aplicar(jogadores, mapa);
			List<Jogador> melhores = ApuracaoFinal.vencedores(jogadores, mapa);
			vencedores = new Cor[melhores.size()];
			for (int i = 0; i < melhores.size(); i++) {
				vencedores[i] = melhores.get(i).getCor();
			}
		}
		Cor[] copia = new Cor[vencedores.length];
		for (int i = 0; i < vencedores.length; i++) {
			copia[i] = vencedores[i];
		}
		return copia;
	}

	private void exigirTurno() throws JogadaInvalidaException {
		if (acoes == null) {
			throw new JogadaInvalidaException("Nenhum turno em andamento.");
		}
	}

	private void exigirKhanDisponivel() throws JogadaInvalidaException {
		exigirTurno();
		if (!acoes.isKhanPendente()) {
			throw new JogadaInvalidaException("A coluna ativada nao permite mover o Khan.");
		}
	}

	private void exigirEspaco(Coluna coluna) throws JogadaInvalidaException {
		if (!jogadorDaVez().getTabuleiro().cabe(coluna)) {
			throw new JogadaInvalidaException("A coluna " + coluna + " ja esta completa.");
		}
	}

	private void gastar(TipoTributo tipo, int quantidade) throws JogadaInvalidaException {
		Jogador jogador = jogadorDaVez();
		if (jogador.getTributos(tipo) + acoes.getVirtuais(tipo) < quantidade) {
			throw new JogadaInvalidaException("Sao necessarias " + quantidade + " pecas de " + tipo + ".");
		}
		int pecas = quantidade - acoes.consumirVirtuais(tipo, quantidade);
		jogador.removerTributos(tipo, pecas);
		estoque.put(tipo, estoque.get(tipo) + pecas);
	}

	private int retirarDoEstoque(TipoTributo tipo, int quantidade) {
		int retiradas = Math.min(quantidade, estoque.get(tipo));
		estoque.put(tipo, estoque.get(tipo) - retiradas);
		return retiradas;
	}

	void receberDoEstoque(Jogador jogador, TipoTributo tipo, int quantidade) {
		jogador.adicionarTributos(tipo, retirarDoEstoque(tipo, quantidade));
	}

	private void abastecer(Provincia provincia) {
		if (!provincia.cheia()) {
			provincia.adicionarPecas(retirarDoEstoque(provincia.getIcone(), 1));
		}
	}

	private int quantidadeDeAtaques(Cidade cidade) {
		Integer ataques = ataquesNoTurno.get(cidade);
		if (ataques == null) {
			return 0;
		}
		return ataques;
	}

	private boolean todasAsCidadesConquistadas() {
		for (Cidade cidade : mapa.getCidades()) {
			if (cidade.getConquistador() == null) {
				return false;
			}
		}
		return true;
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

	void ganharVotos(Jogador jogador, int votos) {
		jogador.adicionarVotos(votos);
		votosNeutros += votos;
		if (votosNeutros >= Regras.limiteDeVotos(jogadores.size())) {
			fimDisparado = true;
		}
	}

	private void verificarCartasAbertas() {
		Jogador jogador = jogadorDaVez();
		Iterator<CartaConselheiroAberto> iterador = cartasAbertas.iterator();
		while (iterador.hasNext()) {
			CartaConselheiroAberto carta = iterador.next();
			if (carta.cumprida(jogador, mapa)) {
				iterador.remove();
				jogador.adicionarCartaAberta(carta);
				ganharVotos(jogador, 1);
			}
		}
	}

	private Jogador jogadorDaVez() {
		return jogadores.get(vez);
	}

	private Jogador jogadorExistente(Cor cor) throws JogadaInvalidaException {
		Jogador jogador = jogadoresPorCor.get(cor);
		if (jogador == null) {
			throw new JogadaInvalidaException("Nao ha jogador com a cor " + cor + ".");
		}
		return jogador;
	}

	private Parada paradaExistente(String id) throws JogadaInvalidaException {
		Parada parada = mapa.getParada(id);
		if (parada == null) {
			throw new JogadaInvalidaException("Parada inexistente: " + id + ".");
		}
		return parada;
	}

	private Provincia provinciaExistente(String id) throws JogadaInvalidaException {
		Provincia provincia = mapa.getProvincia(id);
		if (provincia == null) {
			throw new JogadaInvalidaException("Provincia inexistente: " + id + ".");
		}
		return provincia;
	}

	private Cidade cidadeExistente(String nome) throws JogadaInvalidaException {
		Cidade cidade = mapa.getCidade(nome);
		if (cidade == null) {
			throw new JogadaInvalidaException("Cidade inexistente: " + nome + ".");
		}
		return cidade;
	}

	public int getQuantidadeDeJogadores() {
		return jogadores.size();
	}

	public Cor getCorDoJogador(int ordem) {
		return jogadores.get(ordem).getCor();
	}

	public Cor getJogadorDaVez() {
		return jogadorDaVez().getCor();
	}

	public Herdeiro getHerdeiro(Cor cor) {
		return jogadoresPorCor.get(cor).getHerdeiro();
	}

	public boolean isTurnoEmAndamento() {
		return acoes != null;
	}

	public int getMovimentosRestantes() {
		if (acoes == null) {
			return 0;
		}
		return acoes.getMovimentos();
	}

	public int getPunhosRestantes() {
		if (acoes == null) {
			return 0;
		}
		return acoes.getPunhos();
	}

	public int getVotos(Cor cor) {
		return jogadoresPorCor.get(cor).getVotos();
	}

	public int getVotosNeutros() {
		return votosNeutros;
	}

	public String getPosicao(Cor cor) {
		return jogadoresPorCor.get(cor).getPosicao().getId();
	}

	public int getTributos(Cor cor, TipoTributo tipo) {
		return jogadoresPorCor.get(cor).getTributos(tipo);
	}

	public int getTesouros(Cor cor, TipoTesouro tipo) {
		return jogadoresPorCor.get(cor).getTesouros(tipo);
	}

	public int getYurtsNaReserva(Cor cor) {
		return jogadoresPorCor.get(cor).getYurtsNaReserva();
	}

	public int getQuantidadeDeMelhorias(Cor cor, Coluna coluna) {
		return jogadoresPorCor.get(cor).getTabuleiro().quantidade(coluna);
	}

	public TipoMelhoria getMelhoria(Cor cor, Coluna coluna, int nivel) {
		return jogadoresPorCor.get(cor).getTabuleiro().getMelhorias(coluna).get(nivel).getTipo();
	}

	public boolean isColunaDisponivel(Cor cor, Coluna coluna) {
		return jogadoresPorCor.get(cor).getTabuleiro().colunaDisponivel(coluna);
	}

	public int getBonusDisponiveis(Cor cor, TipoBonus bonus) {
		int quantidade = 0;
		for (TipoBonus disponivel : jogadoresPorCor.get(cor).getBonusDisponiveis()) {
			if (disponivel == bonus) {
				quantidade++;
			}
		}
		return quantidade;
	}

	public int getEstoque(TipoTributo tipo) {
		return estoque.get(tipo);
	}

	public int getPecasNaProvincia(String idProvincia) {
		return mapa.getProvincia(idProvincia).getPecas();
	}

	public int getYurtsNaParada(String idParada) {
		return mapa.getParada(idParada).getYurts().size();
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

	public String getPosicaoDoKhan() {
		return posicaoKhan;
	}

	public TipoMelhoria getMelhoriaNaArea(int posicao) {
		Melhoria melhoria = areaMelhorias.get(posicao);
		if (melhoria == null) {
			return null;
		}
		return melhoria.getTipo();
	}

	public Coluna getCorDaMelhoriaNaArea(int posicao) {
		Melhoria melhoria = areaMelhorias.get(posicao);
		if (melhoria == null) {
			return null;
		}
		return melhoria.getCor();
	}

	public int getMelhoriasGratuitasPendentes() {
		return melhoriasGratuitasPendentes;
	}

	public int getQuantidadeDeCartasAbertas() {
		return cartasAbertas.size();
	}

	public String getDescricaoDaCartaAberta(int indice) {
		return cartasAbertas.get(indice).descricao();
	}

	public int getQuantidadeDeCartasSecretas(Cor cor) {
		return jogadoresPorCor.get(cor).getCartasSecretas().size();
	}

	public String getNomeDaCartaSecreta(Cor cor, int indice) {
		return jogadoresPorCor.get(cor).getCartasSecretas().get(indice).nome();
	}

	public boolean isFimDisparado() {
		return fimDisparado;
	}

	public boolean isTerminado() {
		return terminado;
	}

	Mapa getMapa() {
		return mapa;
	}

	Jogador getJogador(Cor cor) {
		return jogadoresPorCor.get(cor);
	}

	List<Jogador> getJogadores() {
		return jogadores;
	}

	AreaMelhorias getAreaMelhorias() {
		return areaMelhorias;
	}

	AcoesTurno getAcoes() {
		return acoes;
	}

	List<CartaConselheiroAberto> getCartasAbertas() {
		return cartasAbertas;
	}
}
