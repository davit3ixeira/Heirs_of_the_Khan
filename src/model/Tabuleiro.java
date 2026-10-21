package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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

    List<String> menorPercurso(String origemId, String destinoId) {
        Parada origem = paradas.get(origemId);
        Parada destino = paradas.get(destinoId);
        if (origem == null || destino == null) {
            return null;
        }

        Map<Parada, Parada> anterior = new HashMap<>();
        Deque<Parada> fila = new ArrayDeque<>();
        anterior.put(origem, origem);
        fila.add(origem);
        while (!fila.isEmpty() && !anterior.containsKey(destino)) {
            Parada atual = fila.poll();
            for (Parada vizinha : atual.getVizinhas()) {
                if (!anterior.containsKey(vizinha)) {
                    anterior.put(vizinha, atual);
                    fila.add(vizinha);
                }
            }
        }
        if (!anterior.containsKey(destino)) {
            return null;
        }

        LinkedList<String> percurso = new LinkedList<>();
        for (Parada p = destino; p != origem; p = anterior.get(p)) {
            percurso.addFirst(p.getId());
        }
        return percurso;
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

        if (!atual.temEspacoPara(jogador)) {
            return false;
        }

        jogador.irPara(atual, percurso);
        return true;
    }
}
