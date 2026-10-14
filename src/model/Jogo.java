package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Papel da classe: Fachada do jogo: reúne o tabuleiro e os jogadores e expõe as ações de um turno
 * (iniciar turno, mover, pegar tributos e gastá-los).
 *
 * Por que existe no jogo: É o ponto de entrada das regras. Quem usa o modelo (interface ou testes)
 * fala só com esta classe, que valida e delega o trabalho.
 */
public class Jogo {
    /**
     * O que é: Limite de jogadores da partida.
     *
     * Por que importa no jogo: O jogo é para 2 a 5 jogadores. Cada um precisa de uma cor, com o seu
     * tabuleiro, as suas fichas e os seus yurts.
     */
    private static final int MAX_JOGADORES = 5;

    private final Tabuleiro tabuleiro = new Tabuleiro();
    private final Map<String, Jogador> jogadores = new HashMap<>();

    /**
     * O que faz: Cria o jogo com um mapa mínimo de demonstração: Karakorum ligada a P1 (simples) e
     * P2 (dupla), e P1 ligada a P3 (simples).
     *
     * Por que importa no jogo: Oferece uma rede pequena para exercitar as regras de movimento:
     * parada simples, parada dupla, atravessar uma parada ocupada e o limite de movimentos. O mapa
     * real do Império Mongol ainda não está montado aqui.
     */
    public Jogo() {
        tabuleiro.adicionarParada("P1", 1);
        tabuleiro.adicionarParada("P2", 2);
        tabuleiro.adicionarParada("P3", 1);
        tabuleiro.ligarParadas(Tabuleiro.KARAKORUM, "P1");
        tabuleiro.ligarParadas(Tabuleiro.KARAKORUM, "P2");
        tabuleiro.ligarParadas("P1", "P3");
    }

    /**
     * O que faz: Cadastra um jogador novo em Karakorum. Lança exceção se a cor for vazia, repetida
     * ou se já houver 5 jogadores.
     *
     * Por que importa no jogo: Cada jogador escolhe uma cor única e a partida aceita de 2 a 5
     * jogadores. Todos começam em Karakorum. A cor funciona como a identidade de quem joga.
     */
    public void adicionarJogador(String cor) {
        if (cor == null || cor.isBlank()) {
            throw new IllegalArgumentException("A cor do jogador não pode ser vazia.");
        }
        if (jogadores.containsKey(cor)) {
            throw new IllegalArgumentException("Já existe um jogador com essa cor.");
        }
        if (jogadores.size() >= MAX_JOGADORES) {
            throw new IllegalStateException("O jogo aceita no máximo cinco jogadores.");
        }
        jogadores.put(cor, new Jogador(cor, tabuleiro.getParada(Tabuleiro.KARAKORUM)));
    }

    /**
     * O que faz: Repassa ao tabuleiro a criação de uma parada.
     *
     * Por que importa no jogo: Permite montar o mapa pela fachada, definindo paradas simples ou
     * duplas.
     */
    public boolean adicionarParada(String id, int capacidade) {
        return tabuleiro.adicionarParada(id, capacidade);
    }

    /**
     * O que faz: Repassa ao tabuleiro a criação de uma rota entre duas paradas.
     *
     * Por que importa no jogo: As rotas definem por onde os peões se movem.
     */
    public boolean ligarParadas(String idA, String idB) {
        return tabuleiro.ligarParadas(idA, idB);
    }

    /**
     * O que faz: Repassa ao tabuleiro a criação de uma província.
     *
     * Por que importa no jogo: Cria as áreas que fornecem tributos.
     */
    public boolean adicionarProvincia(String id) {
        return tabuleiro.adicionarProvincia(id);
    }

    /**
     * O que faz: Repassa ao tabuleiro a ligação entre uma parada e uma província vizinha.
     *
     * Por que importa no jogo: Define de onde se pode coletar tributos cada vez que um peão passa
     * por aquela parada.
     */
    public boolean associarProvincia(String paradaId, String provinciaId) {
        return tabuleiro.associarProvincia(paradaId, provinciaId);
    }

    /**
     * O que faz: Marca o início do turno do jogador. Devolve false se a cor não existir.
     *
     * Por que importa no jogo: Cada turno recomeça o histórico de paradas visitadas, que determina
     * de quais províncias o jogador pode pegar tributos naquele turno.
     */
    public boolean iniciarTurno(String cor) {
        Jogador jogador = jogadores.get(cor);
        if (jogador == null) {
            return false;
        }
        jogador.iniciarTurno();
        return true;
    }

    /**
     * O que faz: Atalho para mover o jogador a uma única parada vizinha (limite de 1 movimento).
     *
     * Por que importa no jogo: Corresponde à ação 'Mover' com um único movimento, o caso mais
     * simples. Rejeita destino nulo.
     */
    public boolean moverJogador(String cor, String destino) {
        if (destino == null) {
            return false;
        }
        return moverJogador(cor, List.of(destino), 1);
    }

    /**
     * O que faz: Move o jogador por um percurso de várias paradas, respeitando o limite de
     * movimentos. Devolve false se a cor não existir ou se o movimento for inválido.
     *
     * Por que importa no jogo: O limite vem do número de cavalos da coluna ativada (e das melhorias
     * e peças de bônus). Permite andar várias paradas em uma ação, atravessando paradas ocupadas
     * desde que o destino final tenha espaço.
     */
    public boolean moverJogador(String cor, List<String> percurso, int limiteMovimentos) {
        Jogador jogador = jogadores.get(cor);
        if (jogador == null) {
            return false;
        }
        return tabuleiro.moverJogador(jogador, percurso, limiteMovimentos);
    }

    /**
     * O que faz: Executa a coleta de tributos em duas fases, validando tudo antes de alterar
     * qualquer coisa. (1) cada escolha deve apontar uma província existente e um tipo válido, e o
     * jogador deve ter estado numa parada adjacente a ela neste turno; (2) cada província deve ter
     * peças suficientes para o total pedido. Só depois as peças saem das províncias e vão para o
     * jogador. Rejeita se o número de escolhas passar do limite de peças.
     *
     * Por que importa no jogo: É a ação 'Pegar peças de tributo'. O limite de peças é o número de
     * punhos da coluna. A regra permite pegar mais de uma peça da mesma província, mas só das
     * adjacentes ao que foi visitado no turno e só se existirem. O tudo ou nada evita coletas
     * parciais.
     */
    public boolean coletarTributos(String cor, int limiteDePecas, List<EscolhaTributo> escolhas) {
        Jogador jogador = jogadores.get(cor);
        if (jogador == null || escolhas == null || limiteDePecas < 0
                || escolhas.size() > limiteDePecas) {
            return false;
        }

        Map<Provincia, Map<TipoTributo, Integer>> quantidadesSolicitadas = new HashMap<>();
        for (EscolhaTributo escolha : escolhas) {
            if (escolha == null) {
                return false;
            }
            Provincia provincia = tabuleiro.getProvincia(escolha.getProvinciaId());
            TipoTributo tipo = TipoTributo.interpretar(escolha.getTipo());
            if (provincia == null || tipo == null
                    || !jogador.esteveEmParadaAdjacenteA(provincia.getId(), tabuleiro)) {
                return false;
            }
            quantidadesSolicitadas.computeIfAbsent(provincia, p -> new HashMap<>())
                    .merge(tipo, 1, Integer::sum);
        }

        for (Map.Entry<Provincia, Map<TipoTributo, Integer>> entrada
                : quantidadesSolicitadas.entrySet()) {
            for (Map.Entry<TipoTributo, Integer> tipo : entrada.getValue().entrySet()) {
                if (entrada.getKey().quantidade(tipo.getKey()) < tipo.getValue()) {
                    return false;
                }
            }
        }

        for (EscolhaTributo escolha : escolhas) {
            Provincia provincia = tabuleiro.getProvincia(escolha.getProvinciaId());
            TipoTributo tipo = TipoTributo.interpretar(escolha.getTipo());
            provincia.removerTributo(tipo);
            jogador.adicionarTributo(tipo);
        }
        return true;
    }

    /**
     * O que faz: Coloca peças de tributo numa província, respeitando o limite de 3 peças.
     *
     * Por que importa no jogo: Serve para a preparação (1 peça por província, conforme os símbolos
     * do tabuleiro) e para montar cenários de teste.
     */
    public boolean colocarTributosIniciais(String provinciaId, String tipo, int quantidade) {
        Provincia provincia = tabuleiro.getProvincia(provinciaId);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return provincia != null && provincia.adicionarTributos(tributo, quantidade);
    }

    /**
     * O que faz: Informa quantas peças de um tipo há na província, ou -1 se a província ou o tipo
     * forem inválidos.
     *
     * Por que importa no jogo: Permite saber o que ainda pode ser pego e conferir o efeito de uma
     * coleta. O -1 diferencia 'inválido' de 'nenhuma peça' (0).
     */
    public int quantidadeTributoNaProvincia(String provinciaId, String tipo) {
        Provincia provincia = tabuleiro.getProvincia(provinciaId);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return provincia == null || tributo == null ? -1 : provincia.quantidade(tributo);
    }

    /**
     * O que faz: Informa quantas peças de um tipo o jogador guarda, ou -1 se o jogador ou o tipo
     * forem inválidos.
     *
     * Por que importa no jogo: Mostra os recursos disponíveis para atacar cidades (espadas),
     * construir yurts ou comprar melhorias (moedas).
     */
    public int quantidadeTributoDoJogador(String cor, String tipo) {
        Jogador jogador = jogadores.get(cor);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return jogador == null || tributo == null ? -1 : jogador.quantidadeTributo(tributo);
    }

    /**
     * O que faz: Consome peças de tributo do jogador, se ele tiver a quantidade pedida. Devolve
     * false se a cor ou o tipo forem inválidos ou se o saldo não bastar.
     *
     * Por que importa no jogo: É o pagamento das ações: 1 espada para tomar um tesouro (2 para os
     * seguintes no mesmo turno), 2 moedas por melhoria, e 1 yurt para cada construção.
     */
    public boolean gastarTributos(String cor, String tipo, int quantidade) {
        Jogador jogador = jogadores.get(cor);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return jogador != null && tributo != null && jogador.gastarTributos(tributo, quantidade);
    }

    /**
     * O que faz: Devolve o identificador da parada em que o jogador está, ou null se a cor não
     * existir.
     *
     * Por que importa no jogo: Permite saber onde cada peão está, o que decide os movimentos
     * possíveis, a ocupação das paradas e quais províncias estão ao alcance.
     */
    public String paradaDe(String cor) {
        Jogador jogador = jogadores.get(cor);
        return jogador == null ? null : jogador.getParadaAtual().getId();
    }

    /**
     * Papel da classe: Objeto simples que descreve uma escolha de coleta: de qual província e qual
     * tipo de tributo.
     *
     * Por que existe no jogo: Ao pegar tributos o jogador escolhe peça a peça. Esta classe empacota
     * cada escolha (província e tipo) para o jogo validar todas de uma vez.
     */
    public static final class EscolhaTributo {
        private final String provinciaId;
        private final String tipo;

        /**
         * O que faz: Cria uma escolha de coleta com a província e o tipo desejados.
         *
         * Por que importa no jogo: Cada escolha equivale a uma das ações de 'pegar peça de tributo'
         * da coluna ativada.
         */
        public EscolhaTributo(String provinciaId, String tipo) {
            this.provinciaId = provinciaId;
            this.tipo = tipo;
        }

        /**
         * O que faz: Devolve a província escolhida.
         *
         * Por que importa no jogo: O jogo confere se ela existe e se está ao alcance das paradas
         * visitadas no turno.
         */
        public String getProvinciaId() {
            return provinciaId;
        }

        /**
         * O que faz: Devolve o tipo de tributo escolhido, em texto.
         *
         * Por que importa no jogo: O jogo converte o texto em TipoTributo e verifica se a província
         * tem a peça.
         */
        public String getTipo() {
            return tipo;
        }
    }
}
