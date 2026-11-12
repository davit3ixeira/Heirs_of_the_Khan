package model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Jogador {
    static final int TOTAL_YURTS = 12;
    static final int MAX_MELHORIAS_POR_COLUNA = 4;

    private final String cor;
    private final Herdeiro herdeiro;
    private final Set<Integer> colunasOcupadas = new HashSet<>();
    private Parada paradaAtual;
    private final Map<TipoTributo, Integer> tributos = new HashMap<>();
    private final Set<String> paradasVisitadasNoTurno = new LinkedHashSet<>();

    private final Map<Tesouro, Integer> tesouros = new EnumMap<>(Tesouro.class);
    private final List<List<Melhoria>> melhorias = new ArrayList<>();
    private final List<Bonus> bonusDisponiveis = new ArrayList<>();
    private int bonusObtidos = 0;
    private int yurtsRestantes = TOTAL_YURTS;
    private int votos = 0;
    private int linhasPremiadas = 0;

    Jogador(String cor, Herdeiro herdeiro, Parada inicial) {
        this.cor = cor;
        this.herdeiro = herdeiro;
        this.paradaAtual = inicial;
        inicial.entrar(this);
        for (int i = 0; i < Herdeiro.NUM_COLUNAS; i++) {
            melhorias.add(new ArrayList<Melhoria>());
        }
    }

    String getCor() {
        return cor;
    }

    Herdeiro getHerdeiro() {
        return herdeiro;
    }

    boolean colunaOcupada(int coluna) {
        return colunasOcupadas.contains(coluna);
    }

    boolean ocuparColuna(int coluna) {
        if (coluna < 0 || coluna >= Herdeiro.NUM_COLUNAS) {
            return false;
        }
        return colunasOcupadas.add(coluna);
    }

    void recuperarPecasDeAtivacao() {
        colunasOcupadas.clear();
    }

    Parada getParadaAtual() {
        return paradaAtual;
    }

    void iniciarTurno() {
        paradasVisitadasNoTurno.clear();
        paradasVisitadasNoTurno.add(paradaAtual.getId());
    }

    void irPara(Parada destino, List<Parada> paradasPercorridas) {
        if (paradasVisitadasNoTurno.isEmpty()) {
            paradasVisitadasNoTurno.add(paradaAtual.getId());
        }
        paradaAtual.sair(this);
        paradaAtual = destino;
        destino.entrar(this);

        for (Parada parada : paradasPercorridas) {
            paradasVisitadasNoTurno.add(parada.getId());
        }
    }

    boolean visitouNoTurno(String paradaId) {
        return paradasVisitadasNoTurno.contains(paradaId);
    }

    Set<String> getParadasVisitadasNoTurno() {
        return new LinkedHashSet<>(paradasVisitadasNoTurno);
    }

    boolean esteveEmParadaAdjacenteA(String provinciaId, Tabuleiro tabuleiro) {
        for (String paradaId : paradasVisitadasNoTurno) {
            if (tabuleiro.paradaTemProvinciaAdjacente(paradaId, provinciaId)) {
                return true;
            }
        }
        return false;
    }

    void adicionarTributo(TipoTributo tipo) {
        tributos.put(tipo, quantidadeTributo(tipo) + 1);
    }

    int quantidadeTributo(TipoTributo tipo) {
        Integer quantidade = tributos.get(tipo);
        return quantidade == null ? 0 : quantidade;
    }

    boolean gastarTributos(TipoTributo tipo, int quantidade) {
        int atual = quantidadeTributo(tipo);
        if (tipo == null || quantidade <= 0 || atual < quantidade) {
            return false;
        }
        if (atual == quantidade) {
            tributos.remove(tipo);
        } else {
            tributos.put(tipo, atual - quantidade);
        }
        return true;
    }

    void adicionarTesouro(Tesouro tesouro) {
        tesouros.put(tesouro, quantidadeTesouro(tesouro) + 1);
    }

    int quantidadeTesouro(Tesouro tesouro) {
        Integer quantidade = tesouros.get(tesouro);
        return quantidade == null ? 0 : quantidade;
    }

    boolean removerTesouro(Tesouro tesouro) {
        int atual = quantidadeTesouro(tesouro);
        if (atual == 0) {
            return false;
        }
        if (atual == 1) {
            tesouros.remove(tesouro);
        } else {
            tesouros.put(tesouro, atual - 1);
        }
        return true;
    }

    int totalTesouros() {
        int soma = 0;
        for (int quantidade : tesouros.values()) {
            soma += quantidade;
        }
        return soma;
    }

    int tesourosDiferentes() {
        return tesouros.size();
    }

    void receberBonus(Bonus bonus) {
        bonusObtidos++;
        if (bonus != Bonus.VOTO) {
            bonusDisponiveis.add(bonus);
        }
    }

    List<Bonus> getBonusDisponiveis() {
        return new ArrayList<>(bonusDisponiveis);
    }

    Bonus gastarBonus(int indice) {
        if (indice < 0 || indice >= bonusDisponiveis.size()) {
            return null;
        }
        return bonusDisponiveis.remove(indice);
    }

    int getBonusObtidos() {
        return bonusObtidos;
    }

    List<Melhoria> getMelhorias(int coluna) {
        return melhorias.get(coluna);
    }

    boolean colunaDeMelhoriasCheia(int coluna) {
        return melhorias.get(coluna).size() >= MAX_MELHORIAS_POR_COLUNA;
    }

    void adicionarMelhoria(int coluna, Melhoria melhoria) {
        melhorias.get(coluna).add(melhoria);
    }

    int totalMelhorias() {
        int soma = 0;
        for (List<Melhoria> coluna : melhorias) {
            soma += coluna.size();
        }
        return soma;
    }

    List<Melhoria> todasMelhorias() {
        List<Melhoria> todas = new ArrayList<>();
        for (List<Melhoria> coluna : melhorias) {
            todas.addAll(coluna);
        }
        return todas;
    }

    int linhasCompletas() {
        int minimo = Integer.MAX_VALUE;
        for (int i = 0; i < Herdeiro.COLUNA_BRANCA; i++) {
            minimo = Math.min(minimo, melhorias.get(i).size());
        }
        return minimo;
    }

    int getLinhasPremiadas() {
        return linhasPremiadas;
    }

    void premiarLinha() {
        linhasPremiadas++;
    }

    int getYurtsRestantes() {
        return yurtsRestantes;
    }

    boolean gastarYurtDaReserva() {
        if (yurtsRestantes <= 0) {
            return false;
        }
        yurtsRestantes--;
        return true;
    }

    int getVotos() {
        return votos;
    }

    void adicionarVotos(int quantidade) {
        votos += quantidade;
    }
}
