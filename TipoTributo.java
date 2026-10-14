package model;

import java.util.Locale;

/** Tipos de tributo usados internamente pelo Model. */
enum TipoTributo {
    ESPADA,
    YURT,
    MOEDA;

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
