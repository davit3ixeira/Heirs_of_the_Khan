package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Província do mapa e estoque de tributos ainda disponível nela.
 * A regra implementada limita o estoque total, somando todos os tipos, a três peças.
 */
class Provincia {
    /** Quantidade máxima de peças de tributo nesta província. */
    static final int MAX_PECAS = 3;

    /** Identificador usado para associar a província às paradas. */
    private final String id;

    /** Quantidade disponível de cada tipo de tributo. Tipos sem entrada têm quantidade zero. */
    private final Map<TipoTributo, Integer> pecas = new HashMap<>();

    /** Cria uma província sem tributos. */
    Provincia(String id) {
        // O estoque inicia vazio; as peças são colocadas durante a preparação.
        this.id = id;
    }

    /** @return identificador da província */
    String getId() {
        return id;
    }

    /**
     * Soma as peças de todos os tipos que ainda estão na província.
     * Usado para aplicar o limite total de três peças.
     */
    int total() {
        int soma = 0;
        // pecas guarda saldos separados por tipo; a regra limita a soma deles.
        for (int quantidade : pecas.values()) {
            soma += quantidade;
        }
        return soma;
    }

    /** @return quantidade do tipo pedido, ou zero se não houver peças desse tipo */
    int quantidade(TipoTributo tipo) {
        // Tipo nulo não representa uma peça válida e, portanto, tem quantidade zero.
        if (tipo == null) {
            return 0;
        }
        // getOrDefault evita criar uma entrada só para consultar um saldo inexistente.
        return pecas.getOrDefault(tipo, 0);
    }

    /** Atalho para adicionar exatamente uma peça de um tipo. */
    boolean adicionarTributo(TipoTributo tipo) {
        return adicionarTributos(tipo, 1);
    }

    /**
     * Adiciona várias peças de uma só vez, desde que o tipo, a quantidade e o limite permitam.
     *
     * @return true se todas as peças foram adicionadas; false sem alterar o estoque caso inválido
     */
    boolean adicionarTributos(TipoTributo tipo, int quantidade) {
        // Todas as condições são checadas antes de alterar o estoque.
        if (tipo == null || quantidade <= 0 || total() + quantidade > MAX_PECAS) {
            return false;
        }
        // Se o tipo já existe, soma; caso contrário, cria seu saldo.
        pecas.merge(tipo, quantidade, Integer::sum);
        return true;
    }

    /**
     * Retira uma peça do tipo indicado.
     * Remove a entrada do mapa quando a última peça daquele tipo é retirada.
     *
     * @return true se uma peça foi retirada; false se não havia nenhuma
     */
    boolean removerTributo(TipoTributo tipo) {
        int atual = quantidade(tipo);
        // Não é possível retirar uma peça que não existe no estoque.
        if (atual == 0) {
            return false;
        }
        if (atual == 1) {
            pecas.remove(tipo);
        } else {
            // Retira só uma unidade e preserva o saldo restante.
            pecas.put(tipo, atual - 1);
        }
        return true;
    }
}
