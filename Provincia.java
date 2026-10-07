package model;

import java.util.HashMap;
import java.util.Map;

class Provincia {
    static final int MAX_PECAS = 3;

    private final String id;
    private final Map<TipoTributo, Integer> pecas = new HashMap<>();

    Provincia(String id) {
        this.id = id;
    }

    String getId() {
        return id;
    }

    int total() {
        int soma = 0;
        for (int qtd : pecas.values()) {
            soma += qtd;
        }
        return soma;
    }

    int quantidade(TipoTributo tipo) {
        return pecas.getOrDefault(tipo, 0);
    }

    boolean adicionarTributo(TipoTributo tipo) {
        if (total() >= MAX_PECAS) {
            return false;
        }
        pecas.merge(tipo, 1, Integer::sum);
        return true;
    }

    boolean removerTributo(TipoTributo tipo) {
        if (quantidade(tipo) == 0) {
            return false;
        }
        pecas.merge(tipo, -1, Integer::sum);
        return true;
    }
}
