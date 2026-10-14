package model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Papel da classe: Representa um herdeiro do Khan: o peão de uma cor, sua posição no mapa e os
 * tributos que ele guardou.
 *
 * Por que existe no jogo: Cada jogador escolhe uma cor e controla um peão que parte de Karakorum,
 * anda pelas paradas, pega peças de tributo e as guarda para usar em turnos seguintes.
 */
class Jogador {
    private final String cor;
    private Parada paradaAtual;
    private final Map<TipoTributo, Integer> tributos = new HashMap<>();
    private final Set<String> paradasVisitadasNoTurno = new LinkedHashSet<>();

    /**
     * O que faz: Cria o jogador com a sua cor, coloca o peão na parada inicial e já o registra como
     * ocupante dela.
     *
     * Por que importa no jogo: Na preparação, todos os peões começam em Karakorum, a capital
     * mongol. Registrar a entrada aqui garante que a ocupação da parada reflita a regra de
     * capacidade desde o primeiro turno.
     */
    Jogador(String cor, Parada inicial) {
        this.cor = cor;
        this.paradaAtual = inicial;
        inicial.entrar(this);
    }

    /**
     * O que faz: Devolve a cor do jogador.
     *
     * Por que importa no jogo: A cor é a identidade do jogador (tabuleiro, peão, fichas e yurts são
     * todos da sua cor). É usada como chave para saber quem ocupa cada parada e quantos peões
     * distintos há nela.
     */
    String getCor() {
        return cor;
    }

    /**
     * O que faz: Devolve a parada onde o peão está agora.
     *
     * Por que importa no jogo: É o ponto de partida de qualquer movimento: a primeira parada do
     * percurso precisa ser vizinha dela, pois mover-se é sempre ir para uma parada adjacente.
     */
    Parada getParadaAtual() {
        return paradaAtual;
    }

    /**
     * O que faz: Zera o histórico de paradas do turno e registra apenas a parada em que o jogador
     * começa.
     *
     * Por que importa no jogo: Peças de tributo só podem ser pegas nas províncias adjacentes às
     * paradas onde o jogador esteve NO SEU TURNO, 'seja antes, durante ou após' o movimento. Por
     * isso o histórico recomeça a cada turno e a parada inicial já conta como visitada (é o 'antes'
     * do movimento).
     */
    void iniciarTurno() {
        paradasVisitadasNoTurno.clear();
        paradasVisitadasNoTurno.add(paradaAtual.getId());
    }

    /**
     * O que faz: Executa o deslocamento: tira o peão da parada atual, coloca-o no destino e
     * registra todas as paradas do percurso como visitadas no turno. Se o turno não foi iniciado,
     * registra antes a parada de origem.
     *
     * Por que importa no jogo: É a ação 'Mover' já validada. Guardar as paradas atravessadas (e não
     * só a final) é o que permite pegar tributos de províncias vizinhas a qualquer ponto do
     * caminho. Obs.: a regra de 'pular yurts', em que a parada pulada NÃO conta como visitada,
     * ainda não é modelada.
     */
    void irPara(Parada destino, List<Parada> paradasPercorridas) {
        if (paradasVisitadasNoTurno.isEmpty()) {
            paradasVisitadasNoTurno.add(paradaAtual.getId());
        }
        paradaAtual.sair(this);
        paradaAtual = destino;
        destino.entrar(this);

        for (Parada parada : paradasPercorridas) {
            paradasVisitadasNoTurno.add(parada.getId());
        }
    }

    /**
     * O que faz: Diz se alguma parada visitada neste turno faz fronteira com a província indicada.
     *
     * Por que importa no jogo: É a condição para a ação 'Pegar peças de tributo': só se pode pegar
     * de províncias adjacentes a pelo menos uma parada onde o jogador esteve. Sem essa verificação
     * seria possível coletar tributos de qualquer lugar do mapa, sem precisar cavalgar até eles.
     */
    boolean esteveEmParadaAdjacenteA(String provinciaId, Tabuleiro tabuleiro) {
        for (String paradaId : paradasVisitadasNoTurno) {
            if (tabuleiro.paradaTemProvinciaAdjacente(paradaId, provinciaId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * O que faz: Acrescenta uma peça do tipo informado (espada, yurt ou moeda) ao estoque do
     * jogador.
     *
     * Por que importa no jogo: Ao pegar tributos, a peça passa da província para o jogador, que
     * pode usá-la na hora ou guardá-la para turnos seguintes. Cada tipo habilita uma ação: atacar
     * cidades, construir yurts ou comprar melhorias.
     */
    void adicionarTributo(TipoTributo tipo) {
        tributos.merge(tipo, 1, Integer::sum);
    }

    /**
     * O que faz: Informa quantas peças de um tipo o jogador possui (0 se não tiver nenhuma).
     *
     * Por que importa no jogo: As ações dependem de ter recursos: espadas para tomar tesouros de
     * cidades (1 para o primeiro, 2 para os seguintes no mesmo turno), yurts para construir e 2
     * moedas para comprar uma melhoria.
     */
    int quantidadeTributo(TipoTributo tipo) {
        return tributos.getOrDefault(tipo, 0);
    }

    /**
     * O que faz: Remove do estoque a quantidade pedida de um tipo de tributo, mas só se o jogador
     * tiver o suficiente; devolve false, sem alterar nada, se o pedido for inválido ou maior que o
     * saldo.
     *
     * Por que importa no jogo: Usar uma peça de tributo a consome (ela volta ao estoque ao lado do
     * tabuleiro). A validação impede gastar o que não se tem, por exemplo atacar uma cidade sem a
     * espada ou comprar uma melhoria com menos de 2 moedas.
     */
    boolean gastarTributos(TipoTributo tipo, int quantidade) {
        int atual = quantidadeTributo(tipo);
        if (tipo == null || quantidade <= 0 || atual < quantidade) {
            return false;
        }
        if (atual == quantidade) {
            tributos.remove(tipo);
        } else {
            tributos.put(tipo, atual - quantidade);
        }
        return true;
    }
}
