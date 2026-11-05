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

    public boolean coletarTributo(String provinciaId) {
        boolean ok = !escolhendo() && jogo.coletarTributoDaProvincia(provinciaId);
        if (ok) {
            notificarObservadores();
        }
        return ok;
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

    private boolean escolhendo() {
        return jogo.getJogadorEscolhendo() != null;
    }

    private void notificarObservadores() {
        for (Observador o : new ArrayList<>(observadores)) {
            o.notificar(this);
        }
    }
}
