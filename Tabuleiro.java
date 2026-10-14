package model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/** Regras de grafo, movimento e associação entre paradas e províncias. */
class Tabuleiro {
    static final String KARAKORUM = "KARAKORUM";

    private final Map<String, Parada> paradas = new HashMap<>();
    private final Map<String, Provincia> provincias = new HashMap<>();

    Tabuleiro() {
        paradas.put(KARAKORUM, new Parada(KARAKORUM, Parada.CAPACIDADE_ILIMITADA));
    }

    boolean adicionarParada(String id, int capacidade) {
        if (id == null || id.isBlank() || capacidade < 1 || paradas.containsKey(id)) {
            return false;
        }
        paradas.put(id, new Parada(id, capacidade));
        return true;
    }

    boolean adicionarProvincia(String id) {
        if (id == null || id.isBlank() || provincias.containsKey(id)) {
            return false;
        }
        provincias.put(id, new Provincia(id));
        return true;
    }

    boolean ligarParadas(String idA, String idB) {
        Parada a = paradas.get(idA);
        Parada b = paradas.get(idB);
        if (a == null || b == null || a == b) {
            return false;
        }
        a.ligarA(b);
        return true;
    }

    boolean associarProvincia(String paradaId, String provinciaId) {
        Parada parada = paradas.get(paradaId);
        Provincia provincia = provincias.get(provinciaId);
        if (parada == null || provincia == null) {
            return false;
        }
        parada.adicionarProvinciaAdjacente(provincia);
        return true;
    }

    Parada getParada(String id) {
        return paradas.get(id);
    }

    Provincia getProvincia(String id) {
        return provincias.get(id);
    }

    boolean paradaTemProvinciaAdjacente(String paradaId, String provinciaId) {
        Parada parada = paradas.get(paradaId);
        return parada != null && parada.temProvinciaAdjacente(provinciaId);
    }

    boolean moverJogador(Jogador jogador, List<String> idsPercorridos, int limiteMovimentos) {
        if (jogador == null || idsPercorridos == null || idsPercorridos.isEmpty()
                || limiteMovimentos < 1 || idsPercorridos.size() > limiteMovimentos) {
            return false;
        }

        Parada atual = jogador.getParadaAtual();
        List<Parada> percurso = new ArrayList<>();
        for (String id : idsPercorridos) {
            Parada proxima = paradas.get(id);
            if (proxima == null || !atual.eVizinha(proxima)) {
                return false;
            }
            percurso.add(proxima);
            atual = proxima;
        }

        // Paradas ocupadas podem ser atravessadas; a capacidade só restringe o destino final.
        if (!atual.temEspacoPara(jogador)) {
            return false;
        }

        jogador.irPara(atual, percurso);
        return true;
    }
}
