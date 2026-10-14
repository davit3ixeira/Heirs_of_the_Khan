package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Papel da classe: Representa uma província do mapa e as peças de tributo (espadas, yurts e moedas)
 * disponíveis nela.
 *
 * Por que existe no jogo: Províncias são as áreas de onde se coletam os recursos que alimentam o
 * jogo. O jogador precisa cavalgar até as paradas vizinhas e pegar as peças para progredir.
 */
class Provincia {
    /**
     * O que é: Número máximo de peças de tributo que uma província pode ter ao mesmo tempo.
     *
     * Por que importa no jogo: O manual limita as províncias a 3 peças. Isso mantém os recursos
     * escassos e valoriza chegar antes dos adversários.
     */
    static final int MAX_PECAS = 3;

    private final String id;
    private final Map<TipoTributo, Integer> pecas = new HashMap<>();

    /**
     * O que faz: Cria uma província vazia com o identificador dado.
     *
     * Por que importa no jogo: As províncias existem no mapa desde o início. As peças iniciais são
     * colocadas depois, uma por província conforme os símbolos impressos no tabuleiro.
     */
    Provincia(String id) {
        this.id = id;
    }

    /**
     * O que faz: Devolve o identificador da província.
     *
     * Por que importa no jogo: Identifica a província nas associações com paradas e nas escolhas de
     * coleta do jogador.
     */
    String getId() {
        return id;
    }

    /**
     * O que faz: Soma todas as peças de tributo presentes na província, de qualquer tipo.
     *
     * Por que importa no jogo: Permite verificar o teto de 3 peças antes de repor tributos, seja na
     * preparação, seja quando o Khan entrega peças às províncias adjacentes.
     */
    int total() {
        int soma = 0;
        for (int quantidade : pecas.values()) {
            soma += quantidade;
        }
        return soma;
    }

    /**
     * O que faz: Devolve quantas peças de um tipo existem na província (0 se o tipo for nulo ou
     * inexistente).
     *
     * Por que importa no jogo: Antes de pegar, é preciso saber se a peça está lá. Só se pode pegar
     * o que a província tem, e é possível pegar mais de uma peça do mesmo tipo da mesma província.
     */
    int quantidade(TipoTributo tipo) {
        if (tipo == null) {
            return 0;
        }
        return pecas.getOrDefault(tipo, 0);
    }

    /**
     * O que faz: Adiciona uma única peça de tributo do tipo informado, respeitando o limite de 3.
     *
     * Por que importa no jogo: É o caso mais comum de reposição: o Khan coloca 1 peça por província
     * adjacente. Também serve para a colocação inicial de 1 peça em cada província.
     */
    boolean adicionarTributo(TipoTributo tipo) {
        return adicionarTributos(tipo, 1);
    }

    /**
     * O que faz: Adiciona várias peças de um tipo de uma vez. Recusa (devolve false) se o tipo for
     * nulo, a quantidade não for positiva ou o total passaria de 3.
     *
     * Por que importa no jogo: Aplica a regra de máximo de 3 peças por província. A recusa quando a
     * província já está cheia faz com que a peça simplesmente não seja colocada.
     */
    boolean adicionarTributos(TipoTributo tipo, int quantidade) {
        if (tipo == null || quantidade <= 0 || total() + quantidade > MAX_PECAS) {
            return false;
        }
        pecas.merge(tipo, quantidade, Integer::sum);
        return true;
    }

    /**
     * O que faz: Retira uma peça do tipo informado, se houver, e devolve se conseguiu.
     *
     * Por que importa no jogo: É o momento em que o jogador pega o tributo: a peça sai da província
     * e vai para o estoque do jogador. Falha se a província estiver sem aquele tipo ('você não pode
     * pegar yurts desta província, pois ela está vazia').
     */
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
