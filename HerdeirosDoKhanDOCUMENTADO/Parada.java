package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa um ponto de parada no mapa.
 * Mantém as paradas vizinhas, as províncias adjacentes e os jogadores presentes.
 */
class Parada {
    /** Valor especial usado por Karakorum para não impor limite de ocupantes. */
    static final int CAPACIDADE_ILIMITADA = Integer.MAX_VALUE;

    /** Identificador usado nas conexões e consultas do mapa. */
    private final String id;

    /** Número máximo de jogadores que podem terminar nesta parada. */
    private final int capacidade;

    /** Paradas conectadas diretamente a esta. A chave é o ID da vizinha. */
    private final Map<String, Parada> vizinhas = new HashMap<>();

    /** Províncias que podem ser alcançadas a partir desta parada para coleta. */
    private final Map<String, Provincia> provinciasAdjacentes = new HashMap<>();

    /** Jogadores que terminam nesta parada, indexados pela cor. */
    private final Map<String, Jogador> ocupantes = new HashMap<>();

    /** Cria a parada com identificador e capacidade definidos pelo mapa. */
    Parada(String id, int capacidade) {
        // A capacidade e o ID definem a parada durante toda a partida.
        this.id = id;
        this.capacidade = capacidade;
    }

    /** @return identificador único desta parada */
    String getId() {
        return id;
    }

    /**
     * Cria uma conexão nos dois sentidos entre esta parada e outra.
     *
     * @param outra parada que passará a ser vizinha
     */
    void ligarA(Parada outra) {
        // HashMap usa os IDs como chaves para consultar vizinhos rapidamente.
        vizinhas.put(outra.id, outra);
        // A ligação é simétrica: quem é vizinha também reconhece esta parada.
        outra.vizinhas.put(id, this);
    }

    /** Registra uma província como adjacente a esta parada. */
    void adicionarProvinciaAdjacente(Provincia provincia) {
        // A chave impede associações duplicadas com a mesma província.
        provinciasAdjacentes.put(provincia.getId(), provincia);
    }

    /** @return true se a outra parada estiver ligada diretamente a esta */
    boolean eVizinha(Parada outra) {
        // Verifica uma aresta direta; não procura caminhos indiretos pelo mapa.
        return outra != null && vizinhas.containsKey(outra.id);
    }

    /** @return true se esta parada estiver associada à província informada */
    boolean temProvinciaAdjacente(String provinciaId) {
        // A associação é consultada pelo ID, sem expor o mapa interno.
        return provinciasAdjacentes.containsKey(provinciaId);
    }

    /**
     * Verifica a capacidade do destino.
     * Um jogador que já esteja registrado nesta parada não é contado como novo ocupante.
     */
    boolean temEspacoPara(Jogador jogador) {
        // Permite o retorno do próprio jogador à parada sem contar a si mesmo duas vezes.
        return ocupantes.containsKey(jogador.getCor()) || ocupantes.size() < capacidade;
    }

    /** Registra o jogador como ocupante desta parada. */
    void entrar(Jogador jogador) {
        // A cor única funciona como chave do registro de ocupação.
        ocupantes.put(jogador.getCor(), jogador);
    }

    /** Remove o jogador do registro de ocupantes desta parada. */
    void sair(Jogador jogador) {
        // Remove somente o jogador identificado pela sua cor.
        ocupantes.remove(jogador.getCor());
    }
}
