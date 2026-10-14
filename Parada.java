package model;

import java.util.HashMap;
import java.util.Map;

/** Nó do mapa: ligações, províncias vizinhas, capacidade e peões presentes. */
class Parada {
    static final int CAPACIDADE_ILIMITADA = Integer.MAX_VALUE;

    private final String id;
    private final int capacidade;
    private final Map<String, Parada> vizinhas = new HashMap<>();
    private final Map<String, Provincia> provinciasAdjacentes = new HashMap<>();
    private final Map<String, Jogador> ocupantes = new HashMap<>();

    Parada(String id, int capacidade) {
        this.id = id;
        this.capacidade = capacidade;
    }

    String getId() {
        return id;
    }

    void ligarA(Parada outra) {
        vizinhas.put(outra.id, outra);
        outra.vizinhas.put(id, this);
    }

    void adicionarProvinciaAdjacente(Provincia provincia) {
        provinciasAdjacentes.put(provincia.getId(), provincia);
    }

    boolean eVizinha(Parada outra) {
        return outra != null && vizinhas.containsKey(outra.id);
    }

    boolean temProvinciaAdjacente(String provinciaId) {
        return provinciasAdjacentes.containsKey(provinciaId);
    }

    boolean temEspacoPara(Jogador jogador) {
        return ocupantes.containsKey(jogador.getCor()) || ocupantes.size() < capacidade;
    }

    void entrar(Jogador jogador) {
        ocupantes.put(jogador.getCor(), jogador);
    }

    void sair(Jogador jogador) {
        ocupantes.remove(jogador.getCor());
    }
}
