package model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Papel da classe: Guarda o mapa do jogo, com paradas, rotas e províncias, e valida os movimentos
 * dos peões.
 *
 * Por que existe no jogo: É o mapa do Império Mongol com a capital Karakorum. Concentra as regras
 * de movimento para que nenhum peão faça uma jogada impossível.
 */
class Tabuleiro {
    /**
     * O que é: Identificador fixo da capital mongol.
     *
     * Por que importa no jogo: Karakorum é o ponto de partida de todos os peões e uma parada
     * especial: comporta todos os jogadores e não pode ter yurts.
     */
    static final String KARAKORUM = "KARAKORUM";

    private final Map<String, Parada> paradas = new HashMap<>();
    private final Map<String, Provincia> provincias = new HashMap<>();

    /**
     * O que faz: Cria o tabuleiro já com Karakorum, de capacidade ilimitada.
     *
     * Por que importa no jogo: Todos os peões começam em Karakorum na preparação, então ela precisa
     * existir antes de qualquer jogador ser adicionado.
     */
    Tabuleiro() {
        paradas.put(KARAKORUM, new Parada(KARAKORUM, Parada.CAPACIDADE_ILIMITADA));
    }

    /**
     * O que faz: Cria uma parada nova. Recusa identificador vazio ou repetido e capacidade menor
     * que 1.
     *
     * Por que importa no jogo: Monta o mapa com paradas simples (1 peão) e duplas (2 peões).
     * Identificadores únicos evitam ambiguidade na hora de informar os percursos.
     */
    boolean adicionarParada(String id, int capacidade) {
        if (id == null || id.isBlank() || capacidade < 1 || paradas.containsKey(id)) {
            return false;
        }
        paradas.put(id, new Parada(id, capacidade));
        return true;
    }

    /**
     * O que faz: Cria uma província nova, recusando identificador vazio ou repetido.
     *
     * Por que importa no jogo: Monta as áreas de onde saem os tributos (por exemplo, as regiões da
     * Rússia, China e Pérsia).
     */
    boolean adicionarProvincia(String id) {
        if (id == null || id.isBlank() || provincias.containsKey(id)) {
            return false;
        }
        provincias.put(id, new Provincia(id));
        return true;
    }

    /**
     * O que faz: Liga duas paradas existentes por uma rota. Recusa paradas inexistentes ou uma
     * parada ligada a si mesma.
     *
     * Por que importa no jogo: As rotas definem por onde o peão pode andar. Sem a ligação, as
     * paradas não seriam adjacentes e o movimento entre elas seria negado.
     */
    boolean ligarParadas(String idA, String idB) {
        Parada a = paradas.get(idA);
        Parada b = paradas.get(idB);
        if (a == null || b == null || a == b) {
            return false;
        }
        a.ligarA(b);
        return true;
    }

    /**
     * O que faz: Declara que uma província é vizinha de uma parada. Recusa ids inexistentes.
     *
     * Por que importa no jogo: Define de quais paradas se pode pegar tributos de cada província, a
     * ponte entre mover e coletar.
     */
    boolean associarProvincia(String paradaId, String provinciaId) {
        Parada parada = paradas.get(paradaId);
        Provincia provincia = provincias.get(provinciaId);
        if (parada == null || provincia == null) {
            return false;
        }
        parada.adicionarProvinciaAdjacente(provincia);
        return true;
    }

    /**
     * O que faz: Busca uma parada pelo identificador (null se não existir).
     *
     * Por que importa no jogo: Permite que as demais classes localizem paradas, em especial
     * Karakorum, onde os jogadores são colocados no início.
     */
    Parada getParada(String id) {
        return paradas.get(id);
    }

    /**
     * O que faz: Busca uma província pelo identificador (null se não existir).
     *
     * Por que importa no jogo: Usada na coleta de tributos para validar que a província escolhida
     * existe e consultar as suas peças.
     */
    Provincia getProvincia(String id) {
        return provincias.get(id);
    }

    /**
     * O que faz: Diz se a parada informada existe e é vizinha da província informada.
     *
     * Por que importa no jogo: Responde à pergunta do jogador sobre o histórico do turno: 'estive
     * numa parada ao lado dessa província?'. É a base da regra de coleta de tributos.
     */
    boolean paradaTemProvinciaAdjacente(String paradaId, String provinciaId) {
        Parada parada = paradas.get(paradaId);
        return parada != null && parada.temProvinciaAdjacente(provinciaId);
    }

    /**
     * O que faz: Valida e executa um percurso. O percurso não pode ser vazio nem maior que o limite
     * de movimentos, cada passo deve ir para uma parada vizinha da anterior e a parada final
     * precisa ter espaço para o jogador. Se qualquer regra falhar, nada muda (tudo ou nada).
     *
     * Por que importa no jogo: É a ação 'Mover'. O limite de movimentos vem da coluna de ativação
     * escolhida (cavalos). Cada parada atravessada custa 1 movimento. Pode-se passar por parada
     * ocupada, mas só terminar em parada livre, dupla com vaga ou Karakorum.
     */
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
