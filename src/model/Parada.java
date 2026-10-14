package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Papel da classe: Representa um ponto de parada do mapa: suas ligações com outras paradas, as
 * províncias vizinhas e quem está sobre ela agora.
 *
 * Por que existe no jogo: O peão se move pelas rotas e paradas do tabuleiro. É a parada que sabe
 * para onde se pode ir, quantos peões cabem nela e de quais províncias se pode pegar tributos.
 */
class Parada {
    /**
     * O que é: Valor usado como capacidade de uma parada onde cabem todos os jogadores.
     *
     * Por que importa no jogo: Karakorum, a capital, comporta todos os peões ao mesmo tempo, ao
     * contrário das paradas simples (1 peão) e duplas (2 peões).
     */
    static final int CAPACIDADE_ILIMITADA = Integer.MAX_VALUE;

    private final String id;
    private final int capacidade;
    private final Map<String, Parada> vizinhas = new HashMap<>();
    private final Map<String, Provincia> provinciasAdjacentes = new HashMap<>();
    private final Map<String, Jogador> ocupantes = new HashMap<>();

    /**
     * O que faz: Cria a parada com um identificador e uma capacidade máxima de peões.
     *
     * Por que importa no jogo: No mapa há paradas simples (capacidade 1), paradas duplas
     * (capacidade 2) e Karakorum (ilimitada). A capacidade é o que regula onde um movimento pode
     * terminar.
     */
    Parada(String id, int capacidade) {
        this.id = id;
        this.capacidade = capacidade;
    }

    /**
     * O que faz: Devolve o identificador da parada.
     *
     * Por que importa no jogo: Permite referenciar a parada nas rotas, nos percursos informados
     * pelo jogador e no histórico de paradas visitadas no turno.
     */
    String getId() {
        return id;
    }

    /**
     * O que faz: Cria uma rota nos dois sentidos entre esta parada e outra.
     *
     * Por que importa no jogo: O tabuleiro é uma rede de rotas e paradas. As ligações definem o que
     * é 'parada adjacente', e portanto para onde o peão pode ir em cada movimento. A rota vale nos
     * dois sentidos.
     */
    void ligarA(Parada outra) {
        vizinhas.put(outra.id, outra);
        outra.vizinhas.put(id, this);
    }

    /**
     * O que faz: Registra uma província como vizinha desta parada.
     *
     * Por que importa no jogo: Estar numa parada dá acesso às peças de tributo das províncias ao
     * redor dela. Essa associação liga o movimento (parada) à coleta de recursos (província).
     */
    void adicionarProvinciaAdjacente(Provincia provincia) {
        provinciasAdjacentes.put(provincia.getId(), provincia);
    }

    /**
     * O que faz: Diz se a outra parada está diretamente ligada a esta por uma rota.
     *
     * Por que importa no jogo: Mover-se significa mexer o peão para uma parada adjacente. Cada
     * passo de um percurso é validado com esta checagem, o que impede 'teletransportes' pelo mapa.
     */
    boolean eVizinha(Parada outra) {
        return outra != null && vizinhas.containsKey(outra.id);
    }

    /**
     * O que faz: Diz se a província informada faz fronteira com esta parada.
     *
     * Por que importa no jogo: Base da regra de coleta: só se pega tributo de províncias vizinhas
     * às paradas por onde o jogador passou no turno.
     */
    boolean temProvinciaAdjacente(String provinciaId) {
        return provinciasAdjacentes.containsKey(provinciaId);
    }

    /**
     * O que faz: Diz se o jogador pode terminar o movimento aqui: ele já está na parada ou ainda há
     * vaga, conforme a capacidade.
     *
     * Por que importa no jogo: Um peão pode atravessar uma parada ocupada por outro jogador, mas
     * não pode terminar lá, exceto em parada dupla (cabem 2) ou Karakorum (cabem todos). Se o
     * próprio jogador já está na parada, ele não ocupa uma vaga a mais.
     */
    boolean temEspacoPara(Jogador jogador) {
        return ocupantes.containsKey(jogador.getCor()) || ocupantes.size() < capacidade;
    }

    /**
     * O que faz: Registra o jogador como ocupante da parada.
     *
     * Por que importa no jogo: Mantém o controle de quem está onde, para aplicar a regra de
     * capacidade nos movimentos dos demais jogadores.
     */
    void entrar(Jogador jogador) {
        ocupantes.put(jogador.getCor(), jogador);
    }

    /**
     * O que faz: Remove o jogador da lista de ocupantes da parada.
     *
     * Por que importa no jogo: Libera a vaga quando o peão se move, permitindo que outro jogador
     * termine o movimento ali nas jogadas seguintes.
     */
    void sair(Jogador jogador) {
        ocupantes.remove(jogador.getCor());
    }
}
