package model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Jogador {
    private final String cor;
    private Parada paradaAtual;
    private final Map<TipoTributo, Integer> tributos = new HashMap<>();
    private final Set<String> paradasVisitadasNoTurno = new LinkedHashSet<>();

    Jogador(String cor, Parada inicial) {
        this.cor = cor;
        this.paradaAtual = inicial;
        inicial.entrar(this);
    }

    String getCor() {
        return cor;
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

    boolean esteveEmParadaAdjacenteA(String provinciaId, Tabuleiro tabuleiro) {
        for (String paradaId : paradasVisitadasNoTurno) {
            if (tabuleiro.paradaTemProvinciaAdjacente(paradaId, provinciaId)) {
                return true;
            }
        }
        return false;
    }

    void adicionarTributo(TipoTributo tipo) {
        tributos.merge(tipo, 1, Integer::sum);
    }

    int quantidadeTributo(TipoTributo tipo) {
        return tributos.getOrDefault(tipo, 0);
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
}
