package model;

import java.util.HashMap;
import java.util.Map;

/** Representa uma província e as peças de tributo que ainda estão nela. */
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
        for (int quantidade : pecas.values()) {
            soma += quantidade;
        }
        return soma;
    }

    int quantidade(TipoTributo tipo) {
        if (tipo == null) {
            return 0;
        }
        return pecas.getOrDefault(tipo, 0);
    }

    boolean adicionarTributo(TipoTributo tipo) {
        return adicionarTributos(tipo, 1);
    }

    boolean adicionarTributos(TipoTributo tipo, int quantidade) {
        if (tipo == null || quantidade <= 0 || total() + quantidade > MAX_PECAS) {
            return false;
        }
        pecas.merge(tipo, quantidade, Integer::sum);
        return true;
    }

    boolean removerTributo(TipoTributo tipo) {
        int atual = quantidade(tipo);
        if (atual == 0) {
            return false;
        }
        if (atual == 1) {
            pecas.remove(tipo);
        } else {
            pecas.put(tipo, atual - 1);
        }
        return true;
    }
}
