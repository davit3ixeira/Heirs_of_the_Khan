package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Jogo {
    private static final int MAX_JOGADORES = 5;
    private static final int[] LIMITE_DE_VOTOS = {0, 0, 10, 14, 16, 17};
    private static final int[] VOTOS_MAIS_YURTS = {1, 1, 2, 2, 3};
    private static final int[] VOTOS_SEGUNDO_YURTS = {0, 1, 1, 2, 2};
    private static final int TAMANHO_FAIXA = 5;
    private static final int INDICE_MOEDA_FAIXA = 1;
    private static final int INDICE_ESPADA_FAIXA = 3;

    private final Tabuleiro tabuleiro = new Tabuleiro();
    private final Map<String, Jogador> jogadores = new LinkedHashMap<>();
    private final List<String> ordemDosJogadores = new ArrayList<>();
    private int indiceDaVez = 0;

    private int colunaAtivada = -1;
    private int movimentosRestantes = 0;
    private int coletasRestantes = 0;
    private boolean khanPendente = false;
    private int espadasVirtuais = 0;
    private int yurtsVirtuais = 0;
    private int moedasVirtuais = 0;
    private final Map<Tesouro, Integer> tesourosVirtuais = new EnumMap<>(Tesouro.class);
    private final Map<String, Integer> tesourosTiradosNaCidade = new HashMap<>();
    private boolean melhoriaGratisPendente = false;
    private List<Tesouro> opcoesTesouroBonus = new ArrayList<>();

    private final Map<TipoTributo, Integer> estoque = new EnumMap<>(TipoTributo.class);
    private final List<Cidade> cidades = new ArrayList<>();
    private final List<Tesouro> pilhaDeTesouros = new ArrayList<>();
    private final Map<String, Conselheiro> conselheiros = new LinkedHashMap<>();
    private final Map<Integer, Melhoria> faixa = new HashMap<>();
    private final List<Melhoria> pilhaDeMelhorias = new ArrayList<>();
    private String khanLocal = null;
    private int fichaDoConselho = 0;
    private boolean fimDisparado = false;
    private int turnosRestantes = 0;
    private boolean fimDeJogo = false;
    private final List<String> placar = new ArrayList<>();
    private String ultimaMensagem = "";

    private final List<String> cartasAbertas = new ArrayList<>();
    private final Map<String, List<String>> cartasSecretasOferecidas = new HashMap<>();
    private final Map<String, String> cartasSecretas = new HashMap<>();

    public Jogo() {
        this(true);
    }

    private Jogo(boolean mapaReal) {
        if (mapaReal) {
            MapaPadrao.montar(tabuleiro);
        } else {
            tabuleiro.adicionarParada("P1", 1);
            tabuleiro.adicionarParada("P2", 2);
            tabuleiro.adicionarParada("P3", 1);
            tabuleiro.ligarParadas(Tabuleiro.KARAKORUM, "P1");
            tabuleiro.ligarParadas(Tabuleiro.KARAKORUM, "P2");
            tabuleiro.ligarParadas("P1", "P3");
        }
        estoque.put(TipoTributo.ESPADA, 10);
        estoque.put(TipoTributo.YURT, 10);
        estoque.put(TipoTributo.MOEDA, 15);
    }

    static Jogo demonstrativo() {
        return new Jogo(false);
    }

    public void adicionarJogador(String cor) {
        adicionarJogador(cor, jogadores.size() + 1);
    }

    public void adicionarJogador(String cor, int numeroDoTabuleiro) {
        if (cor == null || cor.isBlank()) {
            throw new IllegalArgumentException("A cor do jogador não pode ser vazia.");
        }
        if (jogadores.containsKey(cor)) {
            throw new IllegalArgumentException("Já existe um jogador com essa cor.");
        }
        if (jogadores.size() >= MAX_JOGADORES) {
            throw new IllegalStateException("O jogo aceita no máximo cinco jogadores.");
        }
        if (numeroDoTabuleiro < 1 || numeroDoTabuleiro > MAX_JOGADORES) {
            throw new IllegalArgumentException("Número de tabuleiro inválido.");
        }
        Herdeiro herdeiro = Herdeiro.values()[numeroDoTabuleiro - 1];
        for (Jogador existente : jogadores.values()) {
            if (existente.getHerdeiro() == herdeiro) {
                throw new IllegalArgumentException("Já existe um jogador com esse tabuleiro.");
            }
        }
        jogadores.put(cor, new Jogador(cor, herdeiro, tabuleiro.getParada(Tabuleiro.KARAKORUM)));
        ordemDosJogadores.add(cor);
    }

    public List<String> getCores() {
        return Collections.unmodifiableList(ordemDosJogadores);
    }

    public String getJogadorDaVez() {
        return ordemDosJogadores.isEmpty() ? null : ordemDosJogadores.get(indiceDaVez);
    }

    public int getNumeroDoTabuleiro(String cor) {
        Jogador jogador = jogadores.get(cor);
        return jogador == null ? -1 : jogador.getHerdeiro().ordinal() + 1;
    }

    public String paradaDe(String cor) {
        Jogador jogador = jogadores.get(cor);
        return jogador == null ? null : jogador.getParadaAtual().getId();
    }

    public void distribuirCartas() {
        List<String> abertas = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            abertas.add("1-" + i);
        }
        abertas.addAll(List.of("2-1", "2-2", "2-3", "3-1", "3-2"));
        Collections.shuffle(abertas);
        cartasAbertas.clear();
        cartasAbertas.addAll(abertas.subList(0, 4));

        List<String> secretas = new ArrayList<>();
        for (int grupo = 4; grupo <= 8; grupo++) {
            secretas.add(grupo + "-1");
            secretas.add(grupo + "-2");
        }
        Collections.shuffle(secretas);
        cartasSecretas.clear();
        cartasSecretasOferecidas.clear();
        for (int i = 0; i < ordemDosJogadores.size(); i++) {
            cartasSecretasOferecidas.put(ordemDosJogadores.get(i),
                    new ArrayList<>(secretas.subList(2 * i, 2 * i + 2)));
        }
    }

    public void prepararPartida() {
        for (TipoTributo tipo : TipoTributo.values()) {
            int noTabuleiro = 0;
            for (Provincia provincia : tabuleiro.todasProvincias()) {
                noTabuleiro += provincia.quantidade(tipo);
            }
            estoque.put(tipo, Math.max(0, totalDoTributo(tipo) - noTabuleiro));
        }
        for (int i = 0; i < ordemDosJogadores.size(); i++) {
            int moedas = i < 2 ? 1 : 2;
            for (int k = 0; k < moedas; k++) {
                if (retirarDoEstoque(TipoTributo.MOEDA)) {
                    jogadores.get(ordemDosJogadores.get(i)).adicionarTributo(TipoTributo.MOEDA);
                }
            }
        }

        pilhaDeTesouros.clear();
        for (Tesouro tesouro : Tesouro.values()) {
            for (int i = 0; i < 8; i++) {
                pilhaDeTesouros.add(tesouro);
            }
        }
        Collections.shuffle(pilhaDeTesouros);

        List<String> ids = new ArrayList<>();
        Collections.addAll(ids, Cidade.IDS);
        Collections.shuffle(ids);
        cidades.clear();
        for (String id : ids) {
            cidades.add(new Cidade(id));
        }
        for (int i = 0; i < 3; i++) {
            revelarCidade(cidades.get(i));
        }

        conselheiros.clear();
        List<Conselheiro> lista = new ArrayList<>();
        lista.add(Conselheiro.especifico("RUSSIA_O", Tesouro.SEDA));
        lista.add(Conselheiro.especifico("RUSSIA_L", Tesouro.FERRO));
        lista.add(Conselheiro.especifico("PERSIA_N", Tesouro.PELE));
        lista.add(Conselheiro.especifico("PERSIA_S", Tesouro.ESPECIARIA));
        lista.add(Conselheiro.especifico("CHINA_L", Tesouro.BOI));
        lista.add(Conselheiro.qualquer("CHINA_S"));
        lista.add(Conselheiro.karakorum(Tesouro.FERRO, Tesouro.PELE, Tesouro.BOI));
        List<Bonus> bonus = new ArrayList<>();
        Collections.addAll(bonus, Bonus.values());
        Collections.shuffle(bonus);
        for (int i = 0; i < lista.size(); i++) {
            lista.get(i).setBonus(bonus.get(i));
            conselheiros.put(lista.get(i).getId(), lista.get(i));
        }

        montarPilhaDeMelhorias();
        for (int i = 0; i < TAMANHO_FAIXA; i++) {
            colocarNaFaixa(i, comprarDaPilha());
        }
    }

    private void colocarNaFaixa(int indice, Melhoria melhoria) {
        if (melhoria == null) {
            faixa.remove(indice);
        } else {
            faixa.put(indice, melhoria);
        }
    }

    private int totalDoTributo(TipoTributo tipo) {
        return tipo == TipoTributo.MOEDA ? 15 : 10;
    }

    private boolean retirarDoEstoque(TipoTributo tipo) {
        int atual = estoque.get(tipo);
        if (atual <= 0) {
            return false;
        }
        estoque.put(tipo, atual - 1);
        return true;
    }

    private void devolverAoEstoque(TipoTributo tipo, int quantidade) {
        estoque.put(tipo, estoque.get(tipo) + quantidade);
    }

    private void revelarCidade(Cidade cidade) {
        cidade.revelar();
        for (int i = 0; i < 4 && !pilhaDeTesouros.isEmpty(); i++) {
            cidade.getTesouros().add(pilhaDeTesouros.remove(pilhaDeTesouros.size() - 1));
        }
    }

    private void montarPilhaDeMelhorias() {
        pilhaDeMelhorias.clear();
        int[][] porColuna = {
            {3, 3, 2}, {3, 3, 2}, {3, 3, 2}, {2, 2, 2}, {3, 3, 3}
        };
        TipoMelhoria[] tipos = {TipoMelhoria.MOEDA, TipoMelhoria.ESPADA, TipoMelhoria.YURT,
            TipoMelhoria.MAO, TipoMelhoria.CAVALOS};
        for (int t = 0; t < tipos.length; t++) {
            for (int coluna = 0; coluna < 3; coluna++) {
                for (int k = 0; k < porColuna[t][coluna]; k++) {
                    pilhaDeMelhorias.add(new Melhoria(tipos[t], coluna, null));
                }
            }
        }
        for (Tesouro tesouro : Tesouro.values()) {
            pilhaDeMelhorias.add(Melhoria.deTesouro(tesouro));
        }
        Collections.shuffle(pilhaDeMelhorias);
    }

    private Melhoria comprarDaPilha() {
        if (pilhaDeMelhorias.isEmpty()) {
            return null;
        }
        return pilhaDeMelhorias.remove(pilhaDeMelhorias.size() - 1);
    }

    public String getJogadorEscolhendo() {
        for (String cor : ordemDosJogadores) {
            if (cartasSecretasOferecidas.containsKey(cor)) {
                return cor;
            }
        }
        return null;
    }

    public List<String> getCartasSecretasOferecidas(String cor) {
        List<String> oferecidas = cartasSecretasOferecidas.get(cor);
        return oferecidas == null ? List.of() : Collections.unmodifiableList(oferecidas);
    }

    public boolean escolherCartaSecreta(String cor, String id) {
        List<String> oferecidas = cartasSecretasOferecidas.get(cor);
        if (oferecidas == null || !oferecidas.contains(id)) {
            return false;
        }
        cartasSecretas.put(cor, id);
        cartasSecretasOferecidas.remove(cor);
        return true;
    }

    public List<String> getCartasAbertas() {
        return Collections.unmodifiableList(cartasAbertas);
    }

    public String getCartaSecreta(String cor) {
        return cartasSecretas.get(cor);
    }

    public boolean colunaOcupada(String cor, int coluna) {
        Jogador jogador = jogadores.get(cor);
        return jogador != null && coluna >= 0 && coluna < Herdeiro.NUM_COLUNAS
                && jogador.colunaOcupada(coluna);
    }

    public int getColunaAtivada() {
        return colunaAtivada;
    }

    public int getMovimentosRestantes() {
        return movimentosRestantes;
    }

    public int getColetasRestantes() {
        return coletasRestantes;
    }

    public boolean isKhanPendente() {
        return khanPendente;
    }

    public String getKhanLocal() {
        return khanLocal;
    }

    public boolean isFimDeJogo() {
        return fimDeJogo;
    }

    public boolean isFimDisparado() {
        return fimDisparado;
    }

    public List<String> getPlacar() {
        return Collections.unmodifiableList(placar);
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public int[] getRecursosVirtuais() {
        return new int[] {espadasVirtuais, yurtsVirtuais, moedasVirtuais};
    }

    public List<String> getTesourosVirtuais() {
        List<String> nomes = new ArrayList<>();
        for (Map.Entry<Tesouro, Integer> e : tesourosVirtuais.entrySet()) {
            for (int i = 0; i < e.getValue(); i++) {
                nomes.add(e.getKey().name());
            }
        }
        return nomes;
    }

    public boolean isMelhoriaGratisPendente() {
        return melhoriaGratisPendente;
    }

    public List<String> getOpcoesTesouroBonus() {
        List<String> nomes = new ArrayList<>();
        for (Tesouro t : opcoesTesouroBonus) {
            nomes.add(t.name());
        }
        return nomes;
    }

    private boolean bloqueado() {
        return fimDeJogo || melhoriaGratisPendente || !opcoesTesouroBonus.isEmpty()
                || getJogadorEscolhendo() != null;
    }

    public boolean temEscolhaPendente() {
        return melhoriaGratisPendente || !opcoesTesouroBonus.isEmpty();
    }

    public Map<String, Integer> totaisPorProvincia() {
        return tabuleiro.totaisPorProvincia();
    }

    public Map<String, int[]> detalheDasProvincias() {
        Map<String, int[]> detalhe = new LinkedHashMap<>();
        for (Provincia p : tabuleiro.todasProvincias()) {
            detalhe.put(p.getId(), new int[] {p.quantidade(TipoTributo.ESPADA),
                p.quantidade(TipoTributo.YURT), p.quantidade(TipoTributo.MOEDA)});
        }
        return detalhe;
    }

    public Map<String, String[]> yurtsPorParada() {
        Map<String, String[]> mapa = new LinkedHashMap<>();
        for (Parada p : tabuleiro.todasParadas()) {
            List<String> yurts = p.getYurts();
            if (!yurts.isEmpty()) {
                mapa.put(p.getId(), yurts.toArray(new String[0]));
            }
        }
        return mapa;
    }

    public String[][] descreverCidades() {
        String[][] linhas = new String[cidades.size()][];
        for (int i = 0; i < linhas.length; i++) {
            Cidade c = cidades.get(i);
            String estado = !c.isRevelada() ? "OCULTA" : c.isConquistada() ? "CONQUISTADA" : "ATIVA";
            StringBuilder tesouros = new StringBuilder();
            for (Tesouro t : c.getTesouros()) {
                if (tesouros.length() > 0) {
                    tesouros.append(',');
                }
                tesouros.append(t.name());
            }
            linhas[i] = new String[] {c.getId(), estado, c.getDono() == null ? "" : c.getDono(),
                tesouros.toString()};
        }
        return linhas;
    }

    public String[] faixaDeMelhorias() {
        String[] ids = new String[TAMANHO_FAIXA];
        for (int i = 0; i < ids.length; i++) {
            Melhoria m = faixa.get(i);
            ids[i] = m == null ? "" : m.getId();
        }
        return ids;
    }

    public int getMelhoriasNaPilha() {
        return pilhaDeMelhorias.size();
    }

    public String[][] melhoriasDoJogador(String cor) {
        Jogador j = jogadores.get(cor);
        String[][] colunas = new String[Herdeiro.NUM_COLUNAS][];
        for (int c = 0; c < colunas.length; c++) {
            List<Melhoria> lista = j == null ? new ArrayList<Melhoria>() : j.getMelhorias(c);
            colunas[c] = new String[lista.size()];
            for (int i = 0; i < colunas[c].length; i++) {
                colunas[c][i] = lista.get(i).getId();
            }
        }
        return colunas;
    }

    public String[][] bonusDosConselheiros() {
        String[][] linhas = new String[conselheiros.size()][];
        int i = 0;
        for (Conselheiro c : conselheiros.values()) {
            linhas[i++] = new String[] {c.getId(), c.getBonus() == null ? "" : c.getBonus().name()};
        }
        return linhas;
    }

    public int[] tesourosDoJogador(String cor) {
        Jogador j = jogadores.get(cor);
        int[] q = new int[Tesouro.values().length];
        for (Tesouro t : Tesouro.values()) {
            q[t.ordinal()] = j == null ? 0 : j.quantidadeTesouro(t);
        }
        return q;
    }

    public List<String> bonusDoJogador(String cor) {
        Jogador j = jogadores.get(cor);
        List<String> nomes = new ArrayList<>();
        if (j != null) {
            for (Bonus b : j.getBonusDisponiveis()) {
                nomes.add(b.name());
            }
        }
        return nomes;
    }

    public int[] votosDosJogadores() {
        int[] votos = new int[ordemDosJogadores.size()];
        for (int i = 0; i < votos.length; i++) {
            votos[i] = jogadores.get(ordemDosJogadores.get(i)).getVotos();
        }
        return votos;
    }

    public int[] getConselho() {
        int limite = LIMITE_DE_VOTOS[Math.max(2, Math.min(5, ordemDosJogadores.size()))];
        return new int[] {fichaDoConselho, limite};
    }

    public int getYurtsRestantes(String cor) {
        Jogador j = jogadores.get(cor);
        return j == null ? 0 : j.getYurtsRestantes();
    }

    public boolean ativarColuna(int coluna) {
        Jogador jogador = jogadores.get(getJogadorDaVez());
        if (jogador == null || bloqueado() || colunaAtivada >= 0 || !jogador.ocuparColuna(coluna)) {
            return false;
        }
        colunaAtivada = coluna;
        Herdeiro h = jogador.getHerdeiro();
        movimentosRestantes = h.cavalos(coluna);
        coletasRestantes = h.maos(coluna);
        khanPendente = h.khan(coluna);
        espadasVirtuais = 0;
        yurtsVirtuais = 0;
        moedasVirtuais = h.moedaVirtual(coluna) ? 1 : 0;
        tesourosVirtuais.clear();
        tesourosTiradosNaCidade.clear();
        for (Melhoria m : jogador.getMelhorias(coluna)) {
            switch (m.getTipo()) {
            case MOEDA:
                moedasVirtuais++;
                break;
            case ESPADA:
                espadasVirtuais++;
                break;
            case YURT:
                yurtsVirtuais++;
                break;
            case MAO:
                coletasRestantes++;
                break;
            case CAVALOS:
                movimentosRestantes += 2;
                break;
            default:
                Integer atual = tesourosVirtuais.get(m.getTesouro());
                tesourosVirtuais.put(m.getTesouro(), atual == null ? 1 : atual + 1);
                break;
            }
        }
        registrar(jogador.getCor() + " ativou uma coluna.");
        return true;
    }

    public boolean moverJogadorDaVez(String destino) {
        String cor = getJogadorDaVez();
        if (cor == null || colunaAtivada < 0 || bloqueado() || destino == null) {
            return false;
        }
        List<String> pousos = tabuleiro.menorPercursoComSaltos(paradaDe(cor), destino, cor);
        if (pousos == null || pousos.isEmpty() || pousos.size() > movimentosRestantes) {
            return false;
        }
        if (!tabuleiro.moverComSaltos(jogadores.get(cor), pousos, movimentosRestantes)) {
            return false;
        }
        movimentosRestantes -= pousos.size();
        verificarCartasAbertas(cor);
        return true;
    }

    public boolean coletarTributoDaProvincia(String provinciaId, String tipo) {
        String cor = getJogadorDaVez();
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        if (cor == null || colunaAtivada < 0 || bloqueado() || coletasRestantes < 1 || tributo == null) {
            return false;
        }
        boolean coletou = coletarTributos(cor, coletasRestantes,
                List.of(new EscolhaTributo(provinciaId, tributo.name())));
        if (coletou) {
            coletasRestantes--;
            verificarCartasAbertas(cor);
        }
        return coletou;
    }

    public boolean atacarCidade(String cidadeId, int indice) {
        String cor = getJogadorDaVez();
        Cidade cidade = cidade(cidadeId);
        if (cor == null || cidade == null || colunaAtivada < 0 || bloqueado() || !cidade.isRevelada()
                || cidade.isConquistada() || indice < 0 || indice >= cidade.getTesouros().size()) {
            return false;
        }
        Jogador jogador = jogadores.get(cor);
        boolean adjacente = false;
        for (String paradaId : jogador.getParadasVisitadasNoTurno()) {
            if (cidade.eAdjacente(paradaId)) {
                adjacente = true;
            }
        }
        if (!adjacente) {
            return false;
        }
        Integer ja = tesourosTiradosNaCidade.get(cidadeId);
        int custo = ja == null || ja == 0 ? 1 : 2;
        if (!gastarEspadas(jogador, custo)) {
            return false;
        }
        Tesouro tesouro = cidade.getTesouros().remove(indice);
        jogador.adicionarTesouro(tesouro);
        tesourosTiradosNaCidade.put(cidadeId, (ja == null ? 0 : ja) + 1);
        if (cidade.getTesouros().isEmpty()) {
            conquistar(cidade, jogador);
        }
        verificarCartasAbertas(cor);
        return true;
    }

    private boolean gastarEspadas(Jogador jogador, int custo) {
        int reais = jogador.quantidadeTributo(TipoTributo.ESPADA);
        if (espadasVirtuais + reais < custo) {
            return false;
        }
        int deVirtuais = Math.min(espadasVirtuais, custo);
        espadasVirtuais -= deVirtuais;
        int deReais = custo - deVirtuais;
        if (deReais > 0) {
            jogador.gastarTributos(TipoTributo.ESPADA, deReais);
            devolverAoEstoque(TipoTributo.ESPADA, deReais);
        }
        return true;
    }

    private void conquistar(Cidade cidade, Jogador jogador) {
        cidade.setDono(jogador.getCor());
        jogador.gastarYurtDaReserva();
        registrar(jogador.getCor() + " conquistou " + cidade.getId() + ".");
        for (Cidade proxima : cidades) {
            if (!proxima.isRevelada()) {
                revelarCidade(proxima);
                break;
            }
        }
        boolean todas = true;
        for (Cidade c : cidades) {
            if (!c.isConquistada()) {
                todas = false;
            }
        }
        if (todas) {
            dispararFim();
        }
    }

    public boolean construirYurt(String paradaId) {
        String cor = getJogadorDaVez();
        Parada parada = tabuleiro.getParada(paradaId);
        if (cor == null || parada == null || colunaAtivada < 0 || bloqueado()) {
            return false;
        }
        Jogador jogador = jogadores.get(cor);
        if (!jogador.visitouNoTurno(paradaId) || !parada.podeReceberYurt()
                || jogador.getYurtsRestantes() <= 0) {
            return false;
        }
        if (yurtsVirtuais > 0) {
            yurtsVirtuais--;
        } else if (jogador.quantidadeTributo(TipoTributo.YURT) > 0) {
            jogador.gastarTributos(TipoTributo.YURT, 1);
            devolverAoEstoque(TipoTributo.YURT, 1);
        } else {
            return false;
        }
        jogador.gastarYurtDaReserva();
        parada.adicionarYurt(cor);
        verificarCartasAbertas(cor);
        return true;
    }

    public boolean comprarMelhoria(int indice) {
        String cor = getJogadorDaVez();
        if (cor == null || colunaAtivada < 0 || bloqueado()) {
            return false;
        }
        Jogador jogador = jogadores.get(cor);
        Melhoria alvo;
        if (indice < 0) {
            alvo = pilhaDeMelhorias.isEmpty() ? null : pilhaDeMelhorias.get(pilhaDeMelhorias.size() - 1);
        } else if (indice < TAMANHO_FAIXA) {
            alvo = faixa.get(indice);
        } else {
            alvo = null;
        }
        if (alvo == null || jogador.colunaDeMelhoriasCheia(alvo.getColuna())) {
            return false;
        }
        if (moedasVirtuais + jogador.quantidadeTributo(TipoTributo.MOEDA) < 2) {
            return false;
        }
        int deVirtuais = Math.min(moedasVirtuais, 2);
        moedasVirtuais -= deVirtuais;
        int deReais = 2 - deVirtuais;
        if (deReais > 0) {
            jogador.gastarTributos(TipoTributo.MOEDA, deReais);
            devolverAoEstoque(TipoTributo.MOEDA, deReais);
        }
        if (indice < 0) {
            comprarDaPilha();
        } else {
            retirarDaFaixa(indice);
        }
        jogador.adicionarMelhoria(alvo.getColuna(), alvo);
        registrar(cor + " comprou uma melhoria.");
        verificarLinhaCompleta(jogador);
        verificarCartasAbertas(cor);
        return true;
    }

    private void retirarDaFaixa(int indice) {
        for (int i = indice; i > 0; i--) {
            colocarNaFaixa(i, faixa.get(i - 1));
        }
        faixa.remove(0);
    }

    private void verificarLinhaCompleta(Jogador jogador) {
        if (jogador.linhasCompletas() > jogador.getLinhasPremiadas()) {
            jogador.premiarLinha();
            if (!jogador.colunaDeMelhoriasCheia(Herdeiro.COLUNA_BRANCA) && haMelhoriaDisponivel()) {
                melhoriaGratisPendente = true;
            }
        }
    }

    private boolean haMelhoriaDisponivel() {
        return !pilhaDeMelhorias.isEmpty() || !faixa.isEmpty();
    }

    public boolean escolherMelhoriaGratis(int indice) {
        String cor = getJogadorDaVez();
        if (cor == null || !melhoriaGratisPendente || fimDeJogo) {
            return false;
        }
        Jogador jogador = jogadores.get(cor);
        Melhoria escolhida;
        if (indice < 0) {
            escolhida = comprarDaPilha();
        } else if (indice < TAMANHO_FAIXA && faixa.get(indice) != null) {
            escolhida = faixa.get(indice);
            retirarDaFaixa(indice);
        } else {
            return false;
        }
        if (escolhida == null) {
            return false;
        }
        jogador.adicionarMelhoria(Herdeiro.COLUNA_BRANCA, escolhida);
        melhoriaGratisPendente = false;
        registrar(cor + " recebeu uma melhoria gratuita.");
        verificarCartasAbertas(cor);
        return true;
    }

    public boolean entregarTesouros(String conselheiroId) {
        String cor = getJogadorDaVez();
        Conselheiro conselheiro = conselheiros.get(conselheiroId);
        if (cor == null || conselheiro == null || colunaAtivada < 0 || bloqueado()) {
            return false;
        }
        Jogador jogador = jogadores.get(cor);
        if (!jogador.visitouNoTurno(conselheiroId)) {
            return false;
        }
        Map<Tesouro, Integer> reais = new EnumMap<>(Tesouro.class);
        Map<Tesouro, Integer> virtuais = new EnumMap<>(Tesouro.class);
        for (Tesouro t : Tesouro.values()) {
            reais.put(t, jogador.quantidadeTesouro(t));
            Integer v = tesourosVirtuais.get(t);
            virtuais.put(t, v == null ? 0 : v);
        }
        Map<Tesouro, Integer> usarReais = new EnumMap<>(Tesouro.class);
        Map<Tesouro, Integer> usarVirtuais = new EnumMap<>(Tesouro.class);
        for (Tesouro t : Tesouro.values()) {
            usarReais.put(t, 0);
            usarVirtuais.put(t, 0);
        }
        boolean ok = true;
        if (conselheiro.getExigidos() != null) {
            for (Tesouro t : conselheiro.getExigidos()) {
                ok = ok && separar(t, reais, virtuais, usarReais, usarVirtuais);
            }
        } else if (conselheiro.getTipo() != null) {
            for (int i = 0; i < conselheiro.getQuantidade(); i++) {
                ok = ok && separar(conselheiro.getTipo(), reais, virtuais, usarReais, usarVirtuais);
            }
        } else {
            for (int i = 0; i < conselheiro.getQuantidade() && ok; i++) {
                Tesouro escolhido = null;
                for (Tesouro t : Tesouro.values()) {
                    if (virtuais.get(t) > 0) {
                        escolhido = t;
                    }
                }
                if (escolhido == null) {
                    for (Tesouro t : Tesouro.values()) {
                        if (reais.get(t) > 0 && (escolhido == null || reais.get(t) > reais.get(escolhido))) {
                            escolhido = t;
                        }
                    }
                }
                ok = escolhido != null && separar(escolhido, reais, virtuais, usarReais, usarVirtuais);
            }
        }
        if (!ok) {
            return false;
        }
        for (Tesouro t : Tesouro.values()) {
            for (int i = 0; i < usarReais.get(t); i++) {
                jogador.removerTesouro(t);
            }
            int restante = tesourosVirtuais.containsKey(t) ? tesourosVirtuais.get(t) - usarVirtuais.get(t) : 0;
            if (restante > 0) {
                tesourosVirtuais.put(t, restante);
            } else {
                tesourosVirtuais.remove(t);
            }
        }
        registrar(cor + " entregou tesouros ao conselheiro.");
        ganharVotos(jogador, conselheiro.getVotos());
        if (conselheiro.getBonus() != null) {
            Bonus bonus = conselheiro.getBonus();
            conselheiro.setBonus(null);
            jogador.receberBonus(bonus);
            if (bonus == Bonus.VOTO) {
                ganharVotos(jogador, 1);
            }
        }
        verificarCartasAbertas(cor);
        return true;
    }

    private boolean separar(Tesouro t, Map<Tesouro, Integer> reais, Map<Tesouro, Integer> virtuais,
            Map<Tesouro, Integer> usarReais, Map<Tesouro, Integer> usarVirtuais) {
        if (virtuais.get(t) > 0) {
            virtuais.put(t, virtuais.get(t) - 1);
            usarVirtuais.put(t, usarVirtuais.get(t) + 1);
            return true;
        }
        if (reais.get(t) > 0) {
            reais.put(t, reais.get(t) - 1);
            usarReais.put(t, usarReais.get(t) + 1);
            return true;
        }
        return false;
    }

    public boolean colocarKhan(String local) {
        String cor = getJogadorDaVez();
        if (cor == null || !khanPendente || bloqueado() || local == null || local.equals(khanLocal)) {
            return false;
        }
        int indice = -1;
        for (int i = 0; i < MapaPadrao.KHAN_LOCAIS.length; i++) {
            if (MapaPadrao.KHAN_LOCAIS[i].equals(local)) {
                indice = i;
            }
        }
        if (indice < 0) {
            return false;
        }
        Jogador jogador = jogadores.get(cor);
        if (indice < MapaPadrao.KHAN_PROVINCIAS.length) {
            for (String provinciaId : MapaPadrao.KHAN_PROVINCIAS[indice]) {
                Provincia p = tabuleiro.getProvincia(provinciaId);
                if (p != null && p.getSimbolo() != null && p.total() < Provincia.MAX_PECAS
                        && retirarDoEstoque(p.getSimbolo())) {
                    p.adicionarTributo(p.getSimbolo());
                }
            }
        } else {
            if (faixa.get(INDICE_MOEDA_FAIXA) == null && retirarDoEstoque(TipoTributo.MOEDA)) {
                jogador.adicionarTributo(TipoTributo.MOEDA);
            }
            if (faixa.get(INDICE_ESPADA_FAIXA) == null && retirarDoEstoque(TipoTributo.ESPADA)) {
                jogador.adicionarTributo(TipoTributo.ESPADA);
            }
            for (int i = 0; i < TAMANHO_FAIXA; i++) {
                if (faixa.get(i) == null) {
                    colocarNaFaixa(i, comprarDaPilha());
                }
            }
        }
        khanLocal = local;
        khanPendente = false;
        registrar(cor + " colocou o Khan.");
        verificarCartasAbertas(cor);
        return true;
    }

    public boolean usarBonus(int indice) {
        String cor = getJogadorDaVez();
        if (cor == null || colunaAtivada < 0 || bloqueado()) {
            return false;
        }
        Jogador jogador = jogadores.get(cor);
        List<Bonus> disponiveis = jogador.getBonusDisponiveis();
        if (indice < 0 || indice >= disponiveis.size()) {
            return false;
        }
        Bonus bonus = disponiveis.get(indice);
        switch (bonus) {
        case ESPADAS:
            darTributos(jogador, TipoTributo.ESPADA, 2);
            break;
        case MOEDAS:
            darTributos(jogador, TipoTributo.MOEDA, 2);
            break;
        case YURTS:
            darTributos(jogador, TipoTributo.YURT, 2);
            break;
        case MOVIMENTOS:
            movimentosRestantes += 3;
            coletasRestantes += 1;
            break;
        case MAOS:
            coletasRestantes += 2;
            break;
        case TESOUROS:
            if (pilhaDeTesouros.isEmpty()) {
                return false;
            }
            opcoesTesouroBonus = new ArrayList<>();
            for (int i = 0; i < 3 && !pilhaDeTesouros.isEmpty(); i++) {
                opcoesTesouroBonus.add(pilhaDeTesouros.remove(pilhaDeTesouros.size() - 1));
            }
            break;
        default:
            return false;
        }
        jogador.gastarBonus(indice);
        verificarCartasAbertas(cor);
        return true;
    }

    private void darTributos(Jogador jogador, TipoTributo tipo, int quantidade) {
        for (int i = 0; i < quantidade; i++) {
            if (retirarDoEstoque(tipo)) {
                jogador.adicionarTributo(tipo);
            }
        }
    }

    public boolean escolherTesouroBonus(int indice) {
        String cor = getJogadorDaVez();
        if (cor == null || opcoesTesouroBonus.isEmpty() || indice < 0 || indice >= opcoesTesouroBonus.size()) {
            return false;
        }
        jogadores.get(cor).adicionarTesouro(opcoesTesouroBonus.get(indice));
        opcoesTesouroBonus = new ArrayList<>();
        verificarCartasAbertas(cor);
        return true;
    }

    public boolean encerrarTurno() {
        Jogador jogador = jogadores.get(getJogadorDaVez());
        if (jogador == null || colunaAtivada < 0 || khanPendente || bloqueado()) {
            return false;
        }
        if (jogador.getHerdeiro().recupera(colunaAtivada)) {
            jogador.recuperarPecasDeAtivacao();
        }
        colunaAtivada = -1;
        movimentosRestantes = 0;
        coletasRestantes = 0;
        espadasVirtuais = 0;
        yurtsVirtuais = 0;
        moedasVirtuais = 0;
        tesourosVirtuais.clear();
        tesourosTiradosNaCidade.clear();
        if (fimDisparado) {
            if (turnosRestantes <= 0) {
                finalizarPartida();
                return true;
            }
            turnosRestantes--;
        }
        indiceDaVez = (indiceDaVez + 1) % ordemDosJogadores.size();
        iniciarTurno(getJogadorDaVez());
        return true;
    }

    private void ganharVotos(Jogador jogador, int quantidade) {
        jogador.adicionarVotos(quantidade);
        fichaDoConselho += quantidade;
        if (fichaDoConselho >= getConselho()[1]) {
            dispararFim();
        }
    }

    private void dispararFim() {
        if (fimDisparado) {
            return;
        }
        fimDisparado = true;
        int n = ordemDosJogadores.size();
        turnosRestantes = (n - 1 - indiceDaVez) + n;
        registrar("O conselho Kurultai está completo: o fim da partida foi disparado.");
    }

    private void registrar(String mensagem) {
        ultimaMensagem = mensagem;
    }

    private void verificarCartasAbertas(String cor) {
        Jogador jogador = jogadores.get(cor);
        for (String id : new ArrayList<>(cartasAbertas)) {
            if (cumpreCartaAberta(id, jogador)) {
                cartasAbertas.remove(id);
                registrar(cor + " cumpriu uma carta de conselheiro aberto.");
                ganharVotos(jogador, 1);
            }
        }
    }

    private boolean cumpreCartaAberta(String id, Jogador j) {
        String cor = j.getCor();
        switch (id) {
        case "1-1":
            return j.quantidadeTributo(TipoTributo.ESPADA) >= 4;
        case "1-2":
            return totalDeYurts(cor) >= 6;
        case "1-3":
            return totalDeYurts(cor) >= 10;
        case "1-4":
            return j.totalTesouros() >= 4;
        case "1-5":
            return yurtsNaRegiao(cor, Regiao.CHINA) >= 2 && yurtsNaRegiao(cor, Regiao.RUSSIA) >= 2
                    && yurtsNaRegiao(cor, Regiao.PERSIA) >= 2;
        case "1-6":
            return yurtsNaRegiao(cor, Regiao.CHINA) >= 1 && yurtsNaRegiao(cor, Regiao.RUSSIA) >= 1
                    && yurtsNaRegiao(cor, Regiao.PERSIA) >= 1;
        case "1-7":
            return j.getBonusObtidos() >= 2;
        case "1-8":
            return j.getBonusObtidos() >= 3;
        case "1-9":
            return j.tesourosDiferentes() >= 5;
        case "2-1":
            return cidadesConquistadas(cor) >= 3;
        case "2-2":
            return cidadesConquistadas(cor) >= 2;
        case "2-3":
            return j.linhasCompletas() >= 1;
        case "3-1":
            return maiorColuna(j) >= 4;
        case "3-2":
            return colunasComDuas(j) >= 2;
        default:
            return false;
        }
    }

    private int maiorColuna(Jogador j) {
        int maior = 0;
        for (int c = 0; c < Herdeiro.NUM_COLUNAS; c++) {
            maior = Math.max(maior, j.getMelhorias(c).size());
        }
        return maior;
    }

    private int colunasComDuas(Jogador j) {
        int total = 0;
        for (int c = 0; c < Herdeiro.NUM_COLUNAS; c++) {
            if (j.getMelhorias(c).size() >= 2) {
                total++;
            }
        }
        return total;
    }

    private Cidade cidade(String id) {
        for (Cidade c : cidades) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    private int cidadesConquistadas(String cor) {
        int total = 0;
        for (Cidade c : cidades) {
            if (cor.equals(c.getDono())) {
                total++;
            }
        }
        return total;
    }

    private int yurtsNaRegiao(String cor, Regiao regiao) {
        int total = 0;
        for (Parada p : tabuleiro.todasParadas()) {
            if (Regiao.daParada(p.getId()) == regiao) {
                total += p.yurtsDe(cor);
            }
        }
        for (Cidade c : cidades) {
            if (cor.equals(c.getDono()) && c.getRegiao() == regiao) {
                total++;
            }
        }
        return total;
    }

    private int totalDeYurts(String cor) {
        return yurtsNaRegiao(cor, Regiao.CHINA) + yurtsNaRegiao(cor, Regiao.RUSSIA)
                + yurtsNaRegiao(cor, Regiao.PERSIA);
    }

    private void finalizarPartida() {
        fimDeJogo = true;
        placar.clear();
        Map<String, Integer> regioes = votosDeInfluencia();
        Map<String, Integer> total = new LinkedHashMap<>();
        for (String cor : ordemDosJogadores) {
            Jogador j = jogadores.get(cor);
            int secreta = votosDaCartaSecreta(j);
            int soma = j.getVotos() + regioes.get(cor) + secreta;
            total.put(cor, soma);
            placar.add(cor + ": " + j.getVotos() + " (jogo) + " + regioes.get(cor) + " (regiões) + "
                    + secreta + " (conselheiro secreto) = " + soma + " votos");
        }
        String vencedor = null;
        boolean empate = false;
        for (String cor : ordemDosJogadores) {
            if (vencedor == null) {
                vencedor = cor;
                continue;
            }
            int comparacao = compararDesempate(cor, vencedor, total);
            if (comparacao > 0) {
                vencedor = cor;
                empate = false;
            } else if (comparacao == 0) {
                empate = true;
            }
        }
        placar.add(empate ? "Empate: os jogadores empatados governam juntos o Império Mongol."
                : "Vencedor: " + vencedor);
        registrar("Fim de jogo.");
    }

    private int compararDesempate(String a, String b, Map<String, Integer> total) {
        int dif = total.get(a) - total.get(b);
        if (dif != 0) {
            return dif;
        }
        dif = jogadores.get(a).totalTesouros() - jogadores.get(b).totalTesouros();
        if (dif != 0) {
            return dif;
        }
        return distanciaAteKarakorum(b) - distanciaAteKarakorum(a);
    }

    private int distanciaAteKarakorum(String cor) {
        Parada origem = jogadores.get(cor).getParadaAtual();
        Map<Parada, Integer> distancia = new HashMap<>();
        List<Parada> fila = new ArrayList<>();
        distancia.put(origem, 0);
        fila.add(origem);
        for (int i = 0; i < fila.size(); i++) {
            Parada atual = fila.get(i);
            if (Tabuleiro.KARAKORUM.equals(atual.getId())) {
                return distancia.get(atual);
            }
            for (Parada v : atual.getVizinhas()) {
                if (!distancia.containsKey(v)) {
                    distancia.put(v, distancia.get(atual) + 1);
                    fila.add(v);
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    private Map<String, Integer> votosDeInfluencia() {
        Map<String, Integer> votos = new LinkedHashMap<>();
        for (String cor : ordemDosJogadores) {
            votos.put(cor, 0);
        }
        for (Regiao regiao : Regiao.values()) {
            int maior = 0;
            for (String cor : ordemDosJogadores) {
                maior = Math.max(maior, yurtsNaRegiao(cor, regiao));
            }
            if (maior == 0) {
                continue;
            }
            int segundo = 0;
            int empatadosNoPrimeiro = 0;
            for (String cor : ordemDosJogadores) {
                int y = yurtsNaRegiao(cor, regiao);
                if (y == maior) {
                    empatadosNoPrimeiro++;
                } else if (y > segundo) {
                    segundo = y;
                }
            }
            for (String cor : ordemDosJogadores) {
                int y = yurtsNaRegiao(cor, regiao);
                int melhorias = Math.min(4, jogadores.get(cor).getMelhorias(regiao.coluna).size());
                if (y == maior) {
                    votos.put(cor, votos.get(cor) + VOTOS_MAIS_YURTS[melhorias]);
                } else if (empatadosNoPrimeiro == 1 && y == segundo && y > 0) {
                    votos.put(cor, votos.get(cor) + VOTOS_SEGUNDO_YURTS[melhorias]);
                }
            }
        }
        return votos;
    }

    private int faixa(int valor, int a, int b, int c) {
        return valor >= c ? 3 : valor >= b ? 2 : valor >= a ? 1 : 0;
    }

    private int votosDaCartaSecreta(Jogador j) {
        String cor = j.getCor();
        String id = cartasSecretas.get(cor);
        if (id == null) {
            return 0;
        }
        switch (id) {
        case "4-1":
            return faixa(paresDeMelhorias(j), 1, 2, 4);
        case "4-2":
            return faixa(simbolosDiferentes(j), 4, 6, 7);
        case "5-1":
            return faixa(maiorRedeDeYurts(cor), 3, 6, 8);
        case "5-2":
            return faixa(j.totalMelhorias(), 4, 7, 10);
        case "6-1":
            return faixa(cidadesComYurtsAdjacentes(cor, 1), 3, 6, 8);
        case "6-2":
            return faixa(melhoriasDeCavalosOuPunhos(j), 2, 4, 5);
        case "7-1":
            return faixa(paradasDuplasComYurt(cor), 2, 4, 6);
        case "7-2":
            return faixa(regioesGovernadas(j), 1, 2, 3);
        case "8-1":
            return faixa(cidadesComYurtsAdjacentes(cor, 2), 2, 3, 4);
        case "8-2":
            return faixa(provinciasDeKhanComYurts(cor), 2, 3, 4);
        default:
            return 0;
        }
    }

    private int paresDeMelhorias(Jogador j) {
        Map<String, Integer> contagem = new HashMap<>();
        for (Melhoria m : j.todasMelhorias()) {
            if (m.getTipo() != TipoMelhoria.TESOURO) {
                Integer atual = contagem.get(m.getSimbolo());
                contagem.put(m.getSimbolo(), atual == null ? 1 : atual + 1);
            }
        }
        int pares = 0;
        for (int quantidade : contagem.values()) {
            pares += quantidade / 2;
        }
        return pares;
    }

    private int simbolosDiferentes(Jogador j) {
        Set<String> simbolos = new LinkedHashSet<>();
        for (Melhoria m : j.todasMelhorias()) {
            simbolos.add(m.getSimbolo());
        }
        return simbolos.size();
    }

    private int melhoriasDeCavalosOuPunhos(Jogador j) {
        int total = 0;
        for (Melhoria m : j.todasMelhorias()) {
            if (m.getTipo() == TipoMelhoria.CAVALOS || m.getTipo() == TipoMelhoria.MAO) {
                total++;
            }
        }
        return total;
    }

    private int maiorRedeDeYurts(String cor) {
        Set<Parada> vistas = new LinkedHashSet<>();
        int melhor = 0;
        for (Parada inicio : tabuleiro.todasParadas()) {
            if (inicio.yurtsDe(cor) == 0 || vistas.contains(inicio)) {
                continue;
            }
            int soma = 0;
            List<Parada> fila = new ArrayList<>();
            fila.add(inicio);
            vistas.add(inicio);
            for (int i = 0; i < fila.size(); i++) {
                Parada atual = fila.get(i);
                soma += atual.yurtsDe(cor);
                for (Parada v : atual.getVizinhas()) {
                    if (v.yurtsDe(cor) > 0 && !vistas.contains(v)) {
                        vistas.add(v);
                        fila.add(v);
                    }
                }
            }
            melhor = Math.max(melhor, soma);
        }
        return melhor;
    }

    private int cidadesComYurtsAdjacentes(String cor, int minimo) {
        int total = 0;
        for (Cidade c : cidades) {
            int yurts = 0;
            for (Parada p : tabuleiro.todasParadas()) {
                if (c.eAdjacente(p.getId())) {
                    yurts += p.yurtsDe(cor);
                }
            }
            if (yurts >= minimo) {
                total++;
            }
        }
        return total;
    }

    private int paradasDuplasComYurt(String cor) {
        int total = 0;
        for (Parada p : tabuleiro.todasParadas()) {
            if (p.eDupla() && p.yurtsDe(cor) >= 1) {
                total++;
            }
        }
        return total;
    }

    private int regioesGovernadas(Jogador j) {
        int total = 0;
        for (Regiao regiao : Regiao.values()) {
            if (j.getMelhorias(regiao.coluna).size() >= 2 && yurtsNaRegiao(j.getCor(), regiao) >= 2) {
                total++;
            }
        }
        return total;
    }

    private int provinciasDeKhanComYurts(String cor) {
        int total = 0;
        for (String[] paradas : MapaPadrao.KHAN_PARADAS) {
            int yurts = 0;
            for (String id : paradas) {
                Parada p = tabuleiro.getParada(id);
                if (p != null) {
                    yurts += p.yurtsDe(cor);
                }
            }
            if (yurts >= 2) {
                total++;
            }
        }
        return total;
    }

    public boolean adicionarParada(String id, int capacidade) {
        return tabuleiro.adicionarParada(id, capacidade);
    }

    public boolean ligarParadas(String idA, String idB) {
        return tabuleiro.ligarParadas(idA, idB);
    }

    public boolean adicionarProvincia(String id) {
        return tabuleiro.adicionarProvincia(id);
    }

    public boolean associarProvincia(String paradaId, String provinciaId) {
        return tabuleiro.associarProvincia(paradaId, provinciaId);
    }

    public boolean iniciarTurno(String cor) {
        Jogador jogador = jogadores.get(cor);
        if (jogador == null) {
            return false;
        }
        jogador.iniciarTurno();
        return true;
    }

    public boolean moverJogador(String cor, String destino) {
        if (destino == null) {
            return false;
        }
        return moverJogador(cor, List.of(destino), 1);
    }

    public boolean moverJogador(String cor, List<String> percurso, int limiteMovimentos) {
        Jogador jogador = jogadores.get(cor);
        if (jogador == null) {
            return false;
        }
        return tabuleiro.moverJogador(jogador, percurso, limiteMovimentos);
    }

    public boolean coletarTributos(String cor, int limiteDePecas, List<EscolhaTributo> escolhas) {
        Jogador jogador = jogadores.get(cor);
        if (jogador == null || escolhas == null || limiteDePecas < 0
                || escolhas.size() > limiteDePecas) {
            return false;
        }

        Map<Provincia, Map<TipoTributo, Integer>> quantidadesSolicitadas = new HashMap<>();
        for (EscolhaTributo escolha : escolhas) {
            if (escolha == null) {
                return false;
            }
            Provincia provincia = tabuleiro.getProvincia(escolha.getProvinciaId());
            TipoTributo tipo = TipoTributo.interpretar(escolha.getTipo());
            if (provincia == null || tipo == null
                    || !jogador.esteveEmParadaAdjacenteA(provincia.getId(), tabuleiro)) {
                return false;
            }
            Map<TipoTributo, Integer> porTipo = quantidadesSolicitadas.get(provincia);
            if (porTipo == null) {
                porTipo = new HashMap<>();
                quantidadesSolicitadas.put(provincia, porTipo);
            }
            Integer atual = porTipo.get(tipo);
            porTipo.put(tipo, atual == null ? 1 : atual + 1);
        }

        for (Map.Entry<Provincia, Map<TipoTributo, Integer>> entrada
                : quantidadesSolicitadas.entrySet()) {
            for (Map.Entry<TipoTributo, Integer> tipo : entrada.getValue().entrySet()) {
                if (entrada.getKey().quantidade(tipo.getKey()) < tipo.getValue()) {
                    return false;
                }
            }
        }

        for (EscolhaTributo escolha : escolhas) {
            Provincia provincia = tabuleiro.getProvincia(escolha.getProvinciaId());
            TipoTributo tipo = TipoTributo.interpretar(escolha.getTipo());
            provincia.removerTributo(tipo);
            jogador.adicionarTributo(tipo);
        }
        return true;
    }

    public boolean colocarTributosIniciais(String provinciaId, String tipo, int quantidade) {
        Provincia provincia = tabuleiro.getProvincia(provinciaId);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return provincia != null && provincia.adicionarTributos(tributo, quantidade);
    }

    public int quantidadeTributoNaProvincia(String provinciaId, String tipo) {
        Provincia provincia = tabuleiro.getProvincia(provinciaId);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return provincia == null || tributo == null ? -1 : provincia.quantidade(tributo);
    }

    public int quantidadeTributoDoJogador(String cor, String tipo) {
        Jogador jogador = jogadores.get(cor);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return jogador == null || tributo == null ? -1 : jogador.quantidadeTributo(tributo);
    }

    public boolean gastarTributos(String cor, String tipo, int quantidade) {
        Jogador jogador = jogadores.get(cor);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return jogador != null && tributo != null && jogador.gastarTributos(tributo, quantidade);
    }

    public static final class EscolhaTributo {
        private final String provinciaId;
        private final String tipo;

        public EscolhaTributo(String provinciaId, String tipo) {
            this.provinciaId = provinciaId;
            this.tipo = tipo;
        }

        public String getProvinciaId() {
            return provinciaId;
        }

        public String getTipo() {
            return tipo;
        }
    }
}
