package model;

import java.util.Locale;

/** Tipos de peças de tributo reconhecidos pelo Model. */
enum TipoTributo {
    /** Peça de espada. */
    ESPADA,
    /** Peça de yurt. */
    YURT,
    /** Peça de moeda. */
    MOEDA;

    /**
     * Converte texto da API para um valor do enum.
     * Espaços externos são removidos e diferenças entre maiúsculas/minúsculas ignoradas.
     *
     * @param tipo texto recebido do Controller ou da View
     * @return tipo correspondente, ou null se o texto for nulo ou desconhecido
     */
    static TipoTributo interpretar(String tipo) {
        // Nulo não pode ser convertido para um tipo do enum.
        if (tipo == null) {
            return null;
        }
        try {
            // trim remove espaços externos; uppercase torna a API indiferente à capitalização.
            return TipoTributo.valueOf(tipo.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            // valueOf lança esta exceção quando o texto não coincide com nenhum nome do enum.
            return null;
        }
    }
}
