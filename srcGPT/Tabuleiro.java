package model;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Estrutura interna do mapa e regras que dependem de suas relações.
 * Paradas formam um grafo; cada parada pode ainda estar associada a províncias adjacentes.
 */
class Tabuleiro {
    /** Nome reservado para a parada inicial e de capacidade ilimitada. */
    static final String KARAKORUM = "KARAKORUM";

    /** Índice das paradas do mapa pelo identificador. */
    private final Map<String, Parada> paradas = new HashMap<>();

    /** Índice das províncias do mapa pelo identificador. */
    private final Map<String, Provincia> provincias = new HashMap<>();

    /** Cria Karakorum, que permite a presença de qualquer quantidade de jogadores. */
    Tabuleiro() {
        // Todo jogador começa neste ponto, que não limita a quantidade de ocupantes.
        paradas.put(KARAKORUM, new Parada(KARAKORUM, Parada.CAPACIDADE_ILIMITADA));
    }

    /**
     * Valida os dados e cadastra uma nova parada.
     *
     * @return true se cadastrada; false para ID vazio, capacidade inválida ou ID repetido
     */
    boolean adicionarParada(String id, int capacidade) {
        // Recusa valores que não permitiriam identificar ou ocupar corretamente a parada.
        if (id == null || id.isBlank() || capacidade < 1 || paradas.containsKey(id)) {
            return false;
        }
        // Só grava a parada depois que todos os dados foram validados.
        paradas.put(id, new Parada(id, capacidade));
        return true;
    }

    /** Cadastra uma província vazia; retorna false se o identificador for inválido ou repetido. */
    boolean adicionarProvincia(String id) {
        // IDs vazios ou duplicados tornariam as consultas ambíguas.
        if (id == null || id.isBlank() || provincias.containsKey(id)) {
            return false;
        }
        // A província começa sem tributos; o estoque pode ser preparado depois.
        provincias.put(id, new Provincia(id));
        return true;
    }

    /**
     * Liga duas paradas existentes nos dois sentidos.
     *
     * @return false se alguma parada não existir ou se os IDs apontarem para a mesma parada
     */
    boolean ligarParadas(String idA, String idB) {
        // Traduz os IDs fornecidos pela API em objetos do mapa.
        Parada a = paradas.get(idA);
        Parada b = paradas.get(idB);
        // Só objetos cadastrados e distintos podem formar uma ligação.
        if (a == null || b == null || a == b) {
            return false;
        }
        // Parada.ligarA registra a ligação tanto em a quanto em b.
        a.ligarA(b);
        return true;
    }

    /** Associa uma província existente a uma parada existente. */
    boolean associarProvincia(String paradaId, String provinciaId) {
        // Procura as duas extremidades da relação antes de alterá-la.
        Parada parada = paradas.get(paradaId);
        Provincia provincia = provincias.get(provinciaId);
        if (parada == null || provincia == null) {
            return false;
        }
        // A própria parada mantém a lista de províncias adjacentes.
        parada.adicionarProvinciaAdjacente(provincia);
        return true;
    }

    /** @return parada cadastrada ou null quando o ID não existe */
    Parada getParada(String id) {
        return paradas.get(id);
    }

    /** @return província cadastrada ou null quando o ID não existe */
    Provincia getProvincia(String id) {
        return provincias.get(id);
    }

    /** Verifica a associação entre uma parada e uma província, tratando parada inexistente como false. */
    boolean paradaTemProvinciaAdjacente(String paradaId, String provinciaId) {
        Parada parada = paradas.get(paradaId);
        // A parada inexistente não pode ter províncias adjacentes.
        return parada != null && parada.temProvinciaAdjacente(provinciaId);
    }

    /**
     * Valida e realiza o percurso do jogador.
     * Primeiro converte os IDs em paradas e verifica cada ligação; só depois altera a posição.
     * A capacidade é aplicada apenas ao destino final, não às paradas intermediárias.
     *
     * @param jogador jogador que se moverá
     * @param idsPercorridos IDs de cada parada visitada, na ordem
     * @param limiteMovimentos quantidade máxima de passos permitida
     * @return true se o movimento completo puder ser feito
     */
    boolean moverJogador(Jogador jogador, List<String> idsPercorridos, int limiteMovimentos) {
        // Rejeita pedidos incompletos e limites impossíveis antes de analisar qualquer passo.
        if (jogador == null || idsPercorridos == null || idsPercorridos.isEmpty()
                || limiteMovimentos < 1 || idsPercorridos.size() > limiteMovimentos) {
            return false;
        }

        Parada atual = jogador.getParadaAtual();
        // Guarda os objetos já resolvidos para atualizar o histórico após validar todo o caminho.
        List<Parada> percurso = new ArrayList<>();
        for (String id : idsPercorridos) {
            // Cada próximo ID precisa existir e estar ligado à parada anterior.
            Parada proxima = paradas.get(id);
            if (proxima == null || !atual.eVizinha(proxima)) {
                return false;
            }
            // Acrescenta esta etapa e usa-a como origem da próxima verificação.
            percurso.add(proxima);
            atual = proxima;
        }

        // Paradas ocupadas podem ser atravessadas; só a parada final precisa ter espaço.
        if (!atual.temEspacoPara(jogador)) {
            return false;
        }

        // O estado só muda após todas as verificações terem sido concluídas.
        jogador.irPara(atual, percurso);
        return true;
    }
}
