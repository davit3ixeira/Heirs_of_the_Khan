package model;

import java.util.Locale;

/**
 * Papel da classe: Enumera os três tipos de peça de tributo: ESPADA, YURT e MOEDA.
 *
 * Por que existe no jogo: Cada tipo permite uma ação diferente. Espadas atacam cidades, yurts são
 * colocados no tabuleiro e duas moedas compram uma melhoria.
 */
enum TipoTributo {
    ESPADA,
    YURT,
    MOEDA;

    /**
     * O que faz: Converte um texto (ignorando espaços e maiúsculas) no tipo correspondente, ou
     * devolve null se o texto for nulo ou não for um tipo válido.
     *
     * Por que importa no jogo: Os pedidos dos jogadores chegam como texto. Esta tradução protege o
     * jogo de tipos inexistentes: uma escolha inválida vira null e é recusada, em vez de quebrar a
     * partida.
     */
    static TipoTributo interpretar(String tipo) {
        if (tipo == null) {
            return null;
        }
        try {
            return TipoTributo.valueOf(tipo.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
