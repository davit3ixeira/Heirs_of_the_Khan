package model;

import java.util.HashMap;
import java.util.Map;

class Tabuleiro {
    static final String KARAKORUM = "KARAKORUM";

    private final Map<String, Parada> paradas = new HashMap<>();
    private final Map<String, Provincia> provincias = new HashMap<>();

    Tabuleiro() {
        paradas.put(KARAKORUM, new Parada(KARAKORUM, Parada.CAPACIDADE_ILIMITADA));
    }

    void adicionarParada(String id, int capacidade) {
        paradas.put(id, new Parada(id, capacidade));
    }

    void adicionarProvincia(String id) {
        provincias.put(id, new Provincia(id));
    }

    void ligarParadas(String a, String b) {
        paradas.get(a).ligarA(paradas.get(b));
    }

    Parada getParada(String id) {
        return paradas.get(id);
    }

    Provincia getProvincia(String id) {
        return provincias.get(id);
    }

    boolean moverJogador(Jogador jogador, String idDestino) {
        Parada destino = paradas.get(idDestino);
        if (destino == null) {
            return false;
        }
        if (!jogador.getParadaAtual().eVizinha(destino)) {
            return false;
        }
        if (!destino.temEspaco()) {
            return false;
        }
        jogador.irPara(destino);
        return true;
    }
}
