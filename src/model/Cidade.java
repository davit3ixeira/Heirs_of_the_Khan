package model;

import java.util.ArrayList;
import java.util.List;

class Cidade {
    static final String[] IDS = {"KIEV", "MOSCOU", "SARAI", "SAMARCANDA", "BAGDA", "CABUL",
            "PEQUIM", "CANTAO", "KAESONG"};

    private final String id;
    private final Regiao regiao;
    private final List<Tesouro> tesouros = new ArrayList<>();
    private boolean revelada;
    private String dono;

    Cidade(String id) {
        this.id = id;
        this.regiao = Regiao.daCidade(id);
    }

    String getId() {
        return id;
    }

    Regiao getRegiao() {
        return regiao;
    }

    List<Tesouro> getTesouros() {
        return tesouros;
    }

    boolean isRevelada() {
        return revelada;
    }

    void revelar() {
        revelada = true;
    }

    String getDono() {
        return dono;
    }

    void setDono(String dono) {
        this.dono = dono;
    }

    boolean isConquistada() {
        return dono != null;
    }

    boolean eAdjacente(String paradaId) {
        return paradaId != null && paradaId.startsWith(id + "_");
    }
}
