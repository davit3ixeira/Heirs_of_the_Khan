package model;

import java.util.ArrayList;
import java.util.List;

public class Fachada implements Observado {
    public static final int CORES = 0;
    public static final int PARADAS = 1;
    public static final int VEZ = 2;
    public static final int TABULEIRO = 3;
    public static final int OCUPADAS = 4;
    public static final int COLUNA_ATIVADA = 5;
    public static final int MOVIMENTOS = 6;
    public static final int CARTELA_VISIVEL = 7;
    public static final int CARTAS_VISIVEIS = 8;
    public static final int CARTAS_ABERTAS = 9;
    public static final int CARTA_SECRETA = 10;
    public static final int CARTAS_OFERECIDAS = 11;
    public static final int ESCOLHENDO = 12;
    public static final int TRIBUTOS_PROVINCIAS = 13;
    public static final int TRIBUTOS_JOGADOR = 14;
    public static final int COLETAS = 15;
    public static final int PROVINCIAS_DETALHE = 16;
    public static final int YURTS = 17;
    public static final int CIDADES = 18;
    public static final int KHAN = 19;
    public static final int KHAN_PENDENTE = 20;
    public static final int FAIXA = 21;
    public static final int MELHORIAS_JOGADOR = 22;
    public static final int BONUS_CONSELHEIROS = 23;
    public static final int TESOUROS_JOGADOR = 24;
    public static final int BONUS_JOGADOR = 25;
    public static final int RECURSOS = 26;
    public static final int TESOUROS_VIRTUAIS = 27;
    public static final int ESCOLHA = 28;
    public static final int OPCOES_TESOURO = 29;
    public static final int VOTOS = 30;
    public static final int CONSELHO = 31;
    public static final int FIM = 32;
    public static final int PLACAR = 33;
    public static final int MENSAGEM = 34;
    public static final int PILHA_MELHORIAS = 35;

    public static final String ESCOLHA_TESOURO_BONUS = "TESOURO_BONUS";
    public static final String ESCOLHA_MELHORIA_GRATIS = "MELHORIA_GRATIS";

    private static final String[] TIPOS = {"ESPADA", "YURT", "MOEDA"};

    private static Fachada instancia;

    private final List<Observador> observadores = new ArrayList<>();
    private Jogo jogo = new Jogo();
    private boolean cartelaVisivel = true;
    private boolean cartasVisiveis = false;

    private Fachada() {
    }

    public static Fachada getInstance() {
        if (instancia == null) {
            instancia = new Fachada();
        }
        return instancia;
    }

    public void adicionarObservador(Observador o) {
        observadores.add(o);
    }

    public void removerObservador(Observador o) {
        observadores.remove(o);
    }

    public Object get(int i) {
        String vez = jogo.getJogadorDaVez();
        switch (i) {
        case CORES:
            return jogo.getCores().toArray(new String[0]);
        case PARADAS:
            String[] paradas = new String[jogo.getCores().size()];
            for (int j = 0; j < paradas.length; j++) {
                paradas[j] = jogo.paradaDe(jogo.getCores().get(j));
            }
            return paradas;
        case VEZ:
            return vez;
        case TABULEIRO:
            return jogo.getNumeroDoTabuleiro(vez);
        case OCUPADAS:
            boolean[] ocupadas = new boolean[4];
            for (int j = 0; j < ocupadas.length; j++) {
                ocupadas[j] = jogo.colunaOcupada(vez, j);
            }
            return ocupadas;
        case COLUNA_ATIVADA:
            return jogo.getColunaAtivada() >= 0;
        case MOVIMENTOS:
            return jogo.getMovimentosRestantes();
        case CARTELA_VISIVEL:
            return cartelaVisivel;
        case CARTAS_VISIVEIS:
            return cartasVisiveis;
        case CARTAS_ABERTAS:
            return jogo.getCartasAbertas().toArray(new String[0]);
        case CARTA_SECRETA:
            return jogo.getCartaSecreta(vez);
        case CARTAS_OFERECIDAS:
            return jogo.getCartasSecretasOferecidas(jogo.getJogadorEscolhendo()).toArray(new String[0]);
        case ESCOLHENDO:
            return jogo.getJogadorEscolhendo();
        case TRIBUTOS_PROVINCIAS:
            return jogo.totaisPorProvincia();
        case TRIBUTOS_JOGADOR:
            int[] tributos = new int[TIPOS.length];
            for (int j = 0; j < tributos.length; j++) {
                tributos[j] = jogo.quantidadeTributoDoJogador(vez, TIPOS[j]);
            }
            return tributos;
        case COLETAS:
            return jogo.getColetasRestantes();
        case PROVINCIAS_DETALHE:
            return jogo.detalheDasProvincias();
        case YURTS:
            return jogo.yurtsPorParada();
        case CIDADES:
            return jogo.descreverCidades();
        case KHAN:
            return jogo.getKhanLocal();
        case KHAN_PENDENTE:
            return jogo.isKhanPendente();
        case FAIXA:
            return jogo.faixaDeMelhorias();
        case MELHORIAS_JOGADOR:
            return jogo.melhoriasDoJogador(vez);
        case BONUS_CONSELHEIROS:
            return jogo.bonusDosConselheiros();
        case TESOUROS_JOGADOR:
            return jogo.tesourosDoJogador(vez);
        case BONUS_JOGADOR:
            return jogo.bonusDoJogador(vez).toArray(new String[0]);
        case RECURSOS:
            int[] virtuais = jogo.getRecursosVirtuais();
            return new int[] {jogo.getMovimentosRestantes(), jogo.getColetasRestantes(),
                virtuais[0], virtuais[1], virtuais[2], jogo.getYurtsRestantes(vez)};
        case TESOUROS_VIRTUAIS:
            return jogo.getTesourosVirtuais().toArray(new String[0]);
        case ESCOLHA:
            if (jogo.isMelhoriaGratisPendente()) {
                return ESCOLHA_MELHORIA_GRATIS;
            }
            return jogo.getOpcoesTesouroBonus().isEmpty() ? null : ESCOLHA_TESOURO_BONUS;
        case OPCOES_TESOURO:
            return jogo.getOpcoesTesouroBonus().toArray(new String[0]);
        case VOTOS:
            return jogo.votosDosJogadores();
        case CONSELHO:
            return jogo.getConselho();
        case FIM:
            return jogo.isFimDeJogo();
        case PLACAR:
            return jogo.getPlacar().toArray(new String[0]);
        case MENSAGEM:
            return jogo.getUltimaMensagem();
        case PILHA_MELHORIAS:
            return jogo.getMelhoriasNaPilha();
        default:
            return null;
        }
    }

    public void iniciarPartida(String[] cores, int[] tabuleiros) {
        jogo = new Jogo();
        for (int i = 0; i < cores.length; i++) {
            jogo.adicionarJogador(cores[i], tabuleiros[i]);
        }
        jogo.distribuirCartas();
        jogo.prepararPartida();
        jogo.iniciarTurno(jogo.getJogadorDaVez());
        cartasVisiveis = true;
        notificarObservadores();
    }

    public boolean escolherCartaSecreta(String id) {
        String cor = jogo.getJogadorEscolhendo();
        if (cor == null || !jogo.escolherCartaSecreta(cor, id)) {
            return false;
        }
        if (jogo.getJogadorEscolhendo() == null) {
            cartasVisiveis = false;
        }
        notificarObservadores();
        return true;
    }

    public boolean ativarColuna(int coluna) {
        boolean ok = !escolhendo() && jogo.ativarColuna(coluna);
        if (ok) {
            notificarObservadores();
        }
        return ok;
    }

    public boolean moverJogadorDaVez(String destino) {
        boolean ok = !escolhendo() && jogo.moverJogadorDaVez(destino);
        if (ok) {
            notificarObservadores();
        }
        return ok;
    }

    public boolean coletarTributo(String provinciaId, String tipo) {
        return notificarSe(!escolhendo() && jogo.coletarTributoDaProvincia(provinciaId, tipo));
    }

    public boolean atacarCidade(String cidadeId, int indiceDoTesouro) {
        return notificarSe(!escolhendo() && jogo.atacarCidade(cidadeId, indiceDoTesouro));
    }

    public boolean construirYurt(String paradaId) {
        return notificarSe(!escolhendo() && jogo.construirYurt(paradaId));
    }

    public boolean comprarMelhoria(int indice) {
        return notificarSe(!escolhendo() && jogo.comprarMelhoria(indice));
    }

    public boolean escolherMelhoriaGratis(int indice) {
        return notificarSe(!escolhendo() && jogo.escolherMelhoriaGratis(indice));
    }

    public boolean entregarTesouros(String conselheiroId) {
        return notificarSe(!escolhendo() && jogo.entregarTesouros(conselheiroId));
    }

    public boolean colocarKhan(String local) {
        return notificarSe(!escolhendo() && jogo.colocarKhan(local));
    }

    public boolean usarBonus(int indice) {
        return notificarSe(!escolhendo() && jogo.usarBonus(indice));
    }

    public boolean escolherTesouroBonus(int indice) {
        return notificarSe(!escolhendo() && jogo.escolherTesouroBonus(indice));
    }

    public boolean encerrarTurno() {
        boolean ok = !escolhendo() && jogo.encerrarTurno();
        if (ok) {
            notificarObservadores();
        }
        return ok;
    }

    public void alternarCartela() {
        cartelaVisivel = !cartelaVisivel;
        notificarObservadores();
    }

    public void alternarCartas() {
        if (escolhendo()) {
            return;
        }
        cartasVisiveis = !cartasVisiveis;
        notificarObservadores();
    }

    private boolean notificarSe(boolean ok) {
        if (ok) {
            notificarObservadores();
        }
        return ok;
    }

    private boolean escolhendo() {
        return jogo.getJogadorEscolhendo() != null;
    }

    private void notificarObservadores() {
        for (Observador o : new ArrayList<>(observadores)) {
            o.notificar(this);
        }
    }
}
