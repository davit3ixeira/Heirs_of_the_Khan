package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Jogo {
    private static final int MAX_JOGADORES = 5;

    private final Tabuleiro tabuleiro = new Tabuleiro();
    private final Map<String, Jogador> jogadores = new LinkedHashMap<>();
    private final List<String> ordemDosJogadores = new ArrayList<>();
    private int indiceDaVez = 0;
    private int colunaAtivada = -1;
    private int movimentosRestantes = 0;

    public Jogo() {
        MapaPadrao.montar(tabuleiro);
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
        Herdeiro herdeiro = Herdeiro.values()[jogadores.size()];
        jogadores.put(cor, new Jogador(cor, herdeiro, tabuleiro.getParada(Tabuleiro.KARAKORUM)));
        ordemDosJogadores.add(cor);
    }

    public List<String> getCores() {
        return Collections.unmodifiableList(ordemDosJogadores);
    }

    public String getJogadorDaVez() {
        return ordemDosJogadores.isEmpty() ? null : ordemDosJogadores.get(indiceDaVez);
    }

    /** Número (1 a 5) do tabuleiro de jogador recebido pelo jogador. */
    public int getNumeroDoTabuleiro(String cor) {
        Jogador jogador = jogadores.get(cor);
        return jogador == null ? -1 : jogador.getHerdeiro().ordinal() + 1;
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

    /** Coloca uma peça de ativação do jogador da vez numa coluna livre. */
    public boolean ativarColuna(int coluna) {
        Jogador jogador = jogadores.get(getJogadorDaVez());
        if (jogador == null || colunaAtivada >= 0 || !jogador.ocuparColuna(coluna)) {
            return false;
        }
        colunaAtivada = coluna;
        movimentosRestantes = jogador.getHerdeiro().cavalos(coluna);
        return true;
    }

    /** Move o jogador da vez até o destino gastando um movimento por parada. */
    public boolean moverJogadorDaVez(String destino) {
        String cor = getJogadorDaVez();
        if (cor == null || colunaAtivada < 0) {
            return false;
        }
        List<String> percurso = tabuleiro.menorPercurso(paradaDe(cor), destino);
        if (percurso == null || !moverJogador(cor, percurso, movimentosRestantes)) {
            return false;
        }
        movimentosRestantes -= percurso.size();
        return true;
    }

    /** Encerra o turno; só é possível depois de ativar uma coluna. */
    public boolean encerrarTurno() {
        Jogador jogador = jogadores.get(getJogadorDaVez());
        if (jogador == null || colunaAtivada < 0) {
            return false;
        }
        if (colunaAtivada == Herdeiro.COLUNA_BRANCA) {
            jogador.recuperarPecasDeAtivacao();
        }
        colunaAtivada = -1;
        movimentosRestantes = 0;
        indiceDaVez = (indiceDaVez + 1) % ordemDosJogadores.size();
        iniciarTurno(getJogadorDaVez());
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
