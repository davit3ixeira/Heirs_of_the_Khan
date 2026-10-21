package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Jogo {
    private static final int MAX_JOGADORES = 5;

    private final Tabuleiro tabuleiro = new Tabuleiro();
    private final Map<String, Jogador> jogadores = new LinkedHashMap<>();
    private final List<String> ordemDosJogadores = new ArrayList<>();
    private int indiceDaVez = 0;

    private final Random random = new Random();
    private final int[] dados = new int[2];
    private boolean dadosLancados = false;

    public Jogo() {
        this(true);
    }

    private Jogo(boolean redeDemonstrativa) {
        if (!redeDemonstrativa) {
            MapaPadrao.montar(tabuleiro);
            return;
        }
        tabuleiro.adicionarParada("P1", 1);
        tabuleiro.adicionarParada("P2", 2);
        tabuleiro.adicionarParada("P3", 1);
        tabuleiro.ligarParadas(Tabuleiro.KARAKORUM, "P1");
        tabuleiro.ligarParadas(Tabuleiro.KARAKORUM, "P2");
        tabuleiro.ligarParadas("P1", "P3");
    }

    public void adicionarJogador(String cor) {
        if (cor == null || cor.isBlank()) {
            throw new IllegalArgumentException("A cor do jogador não pode ser vazia.");
        }
        if (jogadores.containsKey(cor)) {
            throw new IllegalArgumentException("Já existe um jogador com essa cor.");
        }
        if (jogadores.size() >= MAX_JOGADORES) {
            throw new IllegalStateException("O jogo aceita no máximo cinco jogadores.");
        }
        jogadores.put(cor, new Jogador(cor, tabuleiro.getParada(Tabuleiro.KARAKORUM)));
        ordemDosJogadores.add(cor);
    }

    public static Jogo novaPartida(List<String> cores) {
        Jogo jogo = new Jogo(false);
        for (String cor : cores) {
            jogo.adicionarJogador(cor);
        }
        jogo.iniciarTurno(jogo.getJogadorDaVez());
        return jogo;
    }

    public List<String> getCores() {
        return Collections.unmodifiableList(ordemDosJogadores);
    }

    public String getJogadorDaVez() {
        return ordemDosJogadores.isEmpty() ? null : ordemDosJogadores.get(indiceDaVez);
    }

    public void passarVez() {
        if (ordemDosJogadores.isEmpty()) {
            return;
        }
        indiceDaVez = (indiceDaVez + 1) % ordemDosJogadores.size();
        dadosLancados = false;
        iniciarTurno(getJogadorDaVez());
    }

    public boolean lancarDados() {
        return definirDados(random.nextInt(6) + 1, random.nextInt(6) + 1);
    }

    public boolean definirDados(int dado1, int dado2) {
        if (dadosLancados || dado1 < 1 || dado1 > 6 || dado2 < 1 || dado2 > 6) {
            return false;
        }
        dados[0] = dado1;
        dados[1] = dado2;
        dadosLancados = true;
        return true;
    }

    public boolean isDadosLancados() {
        return dadosLancados;
    }

    public int getDado(int indice) {
        return dados[indice];
    }

    public boolean moverJogadorDaVez(String destino) {
        String cor = getJogadorDaVez();
        if (!dadosLancados || cor == null) {
            return false;
        }
        List<String> percurso = tabuleiro.menorPercurso(paradaDe(cor), destino);
        if (percurso == null || !moverJogador(cor, percurso, dados[0] + dados[1])) {
            return false;
        }
        passarVez();
        return true;
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
            quantidadesSolicitadas.computeIfAbsent(provincia, p -> new HashMap<>())
                    .merge(tipo, 1, Integer::sum);
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

    public String paradaDe(String cor) {
        Jogador jogador = jogadores.get(cor);
        return jogador == null ? null : jogador.getParadaAtual().getId();
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
