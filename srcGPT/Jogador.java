package model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Estado interno de um participante: posição, cor, inventário e percurso do turno.
 * A classe não é pública porque outros componentes acessam jogadores pela API de Jogo.
 */
class Jogador {
    /** Identificador estável usado também para controlar ocupação das paradas. */
    private final String cor;

    /** Parada em que o jogador se encontra neste momento. */
    private Parada paradaAtual;

    /** Quantidade de cada tipo de tributo que o jogador possui. */
    private final Map<TipoTributo, Integer> tributos = new HashMap<>();

    /** Paradas visitadas no turno atual; o conjunto evita registrar uma parada duas vezes. */
    private final Set<String> paradasVisitadasNoTurno = new LinkedHashSet<>();

    /**
     * Cria o jogador e registra sua presença na parada inicial.
     *
     * @param cor cor/identificador do jogador
     * @param inicial parada inicial, normalmente Karakorum
     */
    Jogador(String cor, Parada inicial) {
        // A identidade não muda durante a partida.
        this.cor = cor;
        // Guarda a parada inicial e sincroniza também o mapa de ocupantes dela.
        this.paradaAtual = inicial;
        inicial.entrar(this);
    }

    /** @return cor usada para identificar este jogador */
    String getCor() {
        return cor;
    }

    /** @return objeto da parada atual */
    Parada getParadaAtual() {
        return paradaAtual;
    }

    /**
     * Limpa o percurso anterior e registra a posição atual como primeira parada do turno.
     */
    void iniciarTurno() {
        // Remove o caminho armazenado no turno anterior.
        paradasVisitadasNoTurno.clear();
        // A posição de origem também conta para a coleta de tributos deste turno.
        paradasVisitadasNoTurno.add(paradaAtual.getId());
    }

    /**
     * Atualiza a posição e registra todas as paradas percorridas.
     * O Tabuleiro chama este método somente depois de validar o caminho e a capacidade final.
     *
     * @param destino parada final do movimento
     * @param paradasPercorridas paradas visitadas em ordem, incluindo o destino
     */
    void irPara(Parada destino, List<Parada> paradasPercorridas) {
        // Se o Controller ainda não iniciou o turno explicitamente, preserva a origem no histórico.
        if (paradasVisitadasNoTurno.isEmpty()) {
            paradasVisitadasNoTurno.add(paradaAtual.getId());
        }

        // Atualiza o registro de ocupação em ambas as paradas.
        paradaAtual.sair(this);
        // Atualiza a referência principal do jogador.
        paradaAtual = destino;
        // Registra o jogador na nova parada.
        destino.entrar(this);

        // Guarda o percurso inteiro para que a coleta considere qualquer parada visitada.
        for (Parada parada : paradasPercorridas) {
            paradasVisitadasNoTurno.add(parada.getId());
        }
    }

    /**
     * Verifica se o percurso do turno incluiu alguma parada junto à província indicada.
     *
     * @param provinciaId ID da província que o jogador pretende coletar
     * @param tabuleiro mapa usado para consultar as adjacências
     * @return true se uma parada visitada estiver associada à província
     */
    boolean esteveEmParadaAdjacenteA(String provinciaId, Tabuleiro tabuleiro) {
        for (String paradaId : paradasVisitadasNoTurno) {
            // Basta encontrar uma parada visitada que tenha essa província ao lado.
            if (tabuleiro.paradaTemProvinciaAdjacente(paradaId, provinciaId)) {
                return true;
            }
        }
        // Nenhuma parada do percurso dava acesso à província.
        return false;
    }

    /** Acrescenta uma unidade do tributo ao inventário do jogador. */
    void adicionarTributo(TipoTributo tipo) {
        // Se já houver peças desse tipo, incrementa; senão, cria o saldo inicial igual a 1.
        tributos.merge(tipo, 1, Integer::sum);
    }

    /** @return quantidade do tipo indicado; zero quando o jogador não possui esse tipo */
    int quantidadeTributo(TipoTributo tipo) {
        // Ausência de chave representa inventário vazio para esse tipo.
        return tributos.getOrDefault(tipo, 0);
    }

    /**
     * Tenta pagar uma quantidade de tributos do inventário.
     * Quantidades não positivas e valores acima do saldo são recusados.
     *
     * @return true se as peças foram descontadas
     */
    boolean gastarTributos(TipoTributo tipo, int quantidade) {
        int atual = quantidadeTributo(tipo);
        // O jogador não pode pagar quantidade inválida nem ficar com saldo negativo.
        if (tipo == null || quantidade <= 0 || atual < quantidade) {
            return false;
        }
        if (atual == quantidade) {
            // Remove a entrada quando o saldo chega a zero.
            tributos.remove(tipo);
        } else {
            // Caso ainda reste saldo, armazena a quantidade atualizada.
            tributos.put(tipo, atual - quantidade);
        }
        return true;
    }
}
