package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ponto de acesso público às regras implementadas pelo Model.
 *
 * <p>View e Controller trabalham com esta classe e com valores simples, como
 * cores e identificadores. As classes internas do domínio ficam ocultas no
 * pacote {@code model}.</p>
 *
 * <p>O construtor cria uma rede pequena de demonstração (P1, P2 e P3), não o
 * mapa oficial completo. A rede pode ser ampliada pelas operações de
 * configuração desta classe.</p>
 */
public class Jogo {
    // A regra do trabalho permite até cinco participantes na mesma partida.
    /** Número máximo de jogadores aceito pelo jogo. */
    private static final int MAX_JOGADORES = 5;

    /** Mantém o mapa, as paradas e as províncias da partida. */
    private final Tabuleiro tabuleiro = new Tabuleiro();

    /** Localiza cada jogador pela cor, evitando percorrer uma lista. */
    private final Map<String, Jogador> jogadores = new HashMap<>();

    /**
     * Cria uma partida com Karakorum e uma pequena rede de paradas para
     * demonstração e compatibilidade com os exemplos iniciais.
     */
    public Jogo() {
        // Tabuleiro cria Karakorum internamente; aqui são adicionadas as demais paradas do exemplo.
        // A rede P1-P3 é apenas uma configuração de exemplo, não o mapa oficial.
        tabuleiro.adicionarParada("P1", 1);
        tabuleiro.adicionarParada("P2", 2);
        tabuleiro.adicionarParada("P3", 1);
        tabuleiro.ligarParadas(Tabuleiro.KARAKORUM, "P1");
        tabuleiro.ligarParadas(Tabuleiro.KARAKORUM, "P2");
        tabuleiro.ligarParadas("P1", "P3");
    }

    /**
     * Registra um jogador e o coloca inicialmente em Karakorum.
     *
     * @param cor identificador da cor do jogador
     * @throws IllegalArgumentException se a cor for nula, vazia ou já estiver em uso
     * @throws IllegalStateException se já houver cinco jogadores
     */
    public void adicionarJogador(String cor) {
        // Evita cadastrar jogadores sem identificador utilizável.
        if (cor == null || cor.isBlank()) {
            throw new IllegalArgumentException("A cor do jogador não pode ser vazia.");
        }
        // A cor funciona como chave do mapa e precisa ser exclusiva.
        if (jogadores.containsKey(cor)) {
            throw new IllegalArgumentException("Já existe um jogador com essa cor.");
        }
        // Valida o limite antes de criar e guardar o novo objeto Jogador.
        if (jogadores.size() >= MAX_JOGADORES) {
            throw new IllegalStateException("O jogo aceita no máximo cinco jogadores.");
        }
        // O construtor de Jogador também o registra como ocupante de Karakorum.
        jogadores.put(cor, new Jogador(cor, tabuleiro.getParada(Tabuleiro.KARAKORUM)));
    }

    /**
     * Cria uma parada no mapa.
     *
     * @param id identificador único da parada
     * @param capacidade quantidade máxima de jogadores que podem terminar nela
     * @return true se criada; false se os dados forem inválidos ou o ID já existir
     */
    public boolean adicionarParada(String id, int capacidade) {
        // Jogo expõe uma API simples e deixa a validação do mapa com Tabuleiro.
        return tabuleiro.adicionarParada(id, capacidade);
    }

    /**
     * Liga duas paradas como vizinhas; a ligação vale nos dois sentidos.
     *
     * @param idA identificador de uma das paradas
     * @param idB identificador da outra parada
     * @return true se a ligação for criada; false se algum ID não existir
     */
    public boolean ligarParadas(String idA, String idB) {
        // Tabuleiro resolve os IDs e cria uma ligação simétrica entre os objetos.
        return tabuleiro.ligarParadas(idA, idB);
    }

    /**
     * Cria uma província sem tributos.
     *
     * @param id identificador único da província
     * @return true se criada; false se o ID for inválido ou já existir
     */
    public boolean adicionarProvincia(String id) {
        // A província passa a existir no mapa, inicialmente sem peças.
        return tabuleiro.adicionarProvincia(id);
    }

    /**
     * Registra que uma parada é adjacente a uma província.
     *
     * @param paradaId identificador da parada
     * @param provinciaId identificador da província
     * @return true se ambos existirem e a associação for registrada
     */
    public boolean associarProvincia(String paradaId, String provinciaId) {
        // Esta relação será consultada depois para permitir ou negar a coleta.
        return tabuleiro.associarProvincia(paradaId, provinciaId);
    }

    /**
     * Inicia o registro do percurso do jogador neste turno, incluindo sua
     * posição atual. O histórico é usado para autorizar a coleta de tributos.
     *
     * @param cor cor do jogador
     * @return true se o jogador existir
     */
    public boolean iniciarTurno(String cor) {
        Jogador jogador = jogadores.get(cor);
        // Uma cor desconhecida não pode iniciar turno.
        if (jogador == null) {
            return false;
        }
        // O jogador reinicia o histórico e registra sua posição de partida.
        jogador.iniciarTurno();
        return true;
    }

    /**
     * Atalho para um movimento de exatamente uma parada.
     *
     * @param cor cor do jogador
     * @param destino identificador da parada final
     * @return true se o movimento for permitido
     */
    public boolean moverJogador(String cor, String destino) {
        // Protege a sobrecarga que transforma o destino nulo em uma lista de um passo.
        if (destino == null) {
            return false;
        }
        // Reutiliza a validação geral do percurso com limite de um movimento.
        return moverJogador(cor, List.of(destino), 1);
    }

    /**
     * Move o jogador por uma sequência de paradas adjacentes.
     * Cada elemento de percurso é uma parada atravessada, na ordem. O Controller
     * informa o limite calculado a partir da ação e dos cavalos ativados.
     * Paradas intermediárias ocupadas podem ser atravessadas, mas a final precisa
     * ter espaço.
     *
     * @param cor cor do jogador
     * @param percurso IDs das paradas percorridas, sem incluir a posição inicial
     * @param limiteMovimentos quantidade máxima de passos autorizada
     * @return true se todo o percurso for válido e o movimento for realizado
     */
    public boolean moverJogador(String cor, List<String> percurso, int limiteMovimentos) {
        Jogador jogador = jogadores.get(cor);
        if (jogador == null) {
            return false;
        }
        // O Tabuleiro valida o caminho e atualiza a posição apenas se tudo for permitido.
        return tabuleiro.moverJogador(jogador, percurso, limiteMovimentos);
    }

    /**
     * Transfere ao jogador os tributos escolhidos entre as províncias acessíveis
     * pelas paradas visitadas no turno.
     *
     * <p>A validação acontece antes das alterações: confere percurso, tipos e
     * quantidades; em seguida, retira as peças das províncias e as adiciona ao
     * inventário. Assim, uma solicitação inválida não causa transferência parcial.</p>
     *
     * @param cor cor do jogador
     * @param limiteDePecas número máximo de peças nesta coleta
     * @param escolhas uma escolha por peça a retirar
     * @return true se todas as peças forem transferidas; false se alguma regra falhar
     */
    public boolean coletarTributos(String cor, int limiteDePecas, List<EscolhaTributo> escolhas) {
        Jogador jogador = jogadores.get(cor);
        // Falha antes de percorrer a lista se o pedido básico for inválido.
        if (jogador == null || escolhas == null || limiteDePecas < 0
                || escolhas.size() > limiteDePecas) {
            return false;
        }

        // Conta pedidos repetidos do mesmo tipo na mesma província.
        Map<Provincia, Map<TipoTributo, Integer>> quantidadesSolicitadas = new HashMap<>();
        for (EscolhaTributo escolha : escolhas) {
            // Uma entrada nula não descreve uma peça que possa ser localizada.
            if (escolha == null) {
                return false;
            }
            // Converte os dados externos (IDs e texto) para objetos e tipos do domínio.
            Provincia provincia = tabuleiro.getProvincia(escolha.getProvinciaId());
            TipoTributo tipo = TipoTributo.interpretar(escolha.getTipo());
            // A peça só pode vir de província existente, válida e adjacente ao percurso.
            if (provincia == null || tipo == null
                    || !jogador.esteveEmParadaAdjacenteA(provincia.getId(), tabuleiro)) {
                return false;
            }
            // merge soma 1 mesmo quando o Controller pede várias peças iguais.
            quantidadesSolicitadas.computeIfAbsent(provincia, p -> new HashMap<>())
                    .merge(tipo, 1, Integer::sum);
        }

        // Confere o estoque antes de remover qualquer peça.
        for (Map.Entry<Provincia, Map<TipoTributo, Integer>> entrada
                : quantidadesSolicitadas.entrySet()) {
            for (Map.Entry<TipoTributo, Integer> tipo : entrada.getValue().entrySet()) {
                // Compara estoque atual com total solicitado daquele tipo naquela província.
                if (entrada.getKey().quantidade(tipo.getKey()) < tipo.getValue()) {
                    return false;
                }
            }
        }

        // Como a solicitação inteira é válida, realiza agora as transferências.
        for (EscolhaTributo escolha : escolhas) {
            Provincia provincia = tabuleiro.getProvincia(escolha.getProvinciaId());
            TipoTributo tipo = TipoTributo.interpretar(escolha.getTipo());
            // Cada peça sai da província de origem e entra no inventário do jogador.
            provincia.removerTributo(tipo);
            jogador.adicionarTributo(tipo);
        }
        return true;
    }

    /**
     * Adiciona tributos durante a preparação do mapa ou em testes.
     *
     * @param provinciaId província que receberá as peças
     * @param tipo texto ESPADA, YURT ou MOEDA
     * @param quantidade número de peças a inserir
     * @return true se a província aceitar todas as peças
     */
    public boolean colocarTributosIniciais(String provinciaId, String tipo, int quantidade) {
        Provincia provincia = tabuleiro.getProvincia(provinciaId);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return provincia != null && provincia.adicionarTributos(tributo, quantidade);
    }

    /**
     * Consulta o estoque de um tipo de tributo em uma província.
     *
     * @return quantidade disponível ou -1 se a província ou o tipo não for reconhecido
     */
    public int quantidadeTributoNaProvincia(String provinciaId, String tipo) {
        Provincia provincia = tabuleiro.getProvincia(provinciaId);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        // O valor -1 diferencia ID/tipo inválido de uma quantidade legítima igual a zero.
        return provincia == null || tributo == null ? -1 : provincia.quantidade(tributo);
    }

    /**
     * Consulta o inventário de um jogador.
     *
     * @return quantidade possuída ou -1 se jogador ou tipo não for reconhecido
     */
    public int quantidadeTributoDoJogador(String cor, String tipo) {
        Jogador jogador = jogadores.get(cor);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        // O jogador pode possuir zero peças; -1 sinaliza que a consulta não pôde ser feita.
        return jogador == null || tributo == null ? -1 : jogador.quantidadeTributo(tributo);
    }

    /**
     * Desconta do inventário as peças usadas para pagar uma ação.
     *
     * @param cor cor do jogador
     * @param tipo texto do tipo de tributo
     * @param quantidade número de peças a gastar
     * @return true se o jogador possuir e gastar essa quantidade
     */
    public boolean gastarTributos(String cor, String tipo, int quantidade) {
        Jogador jogador = jogadores.get(cor);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        // A regra de saldo fica em Jogador, que conhece seu próprio inventário.
        return jogador != null && tributo != null && jogador.gastarTributos(tributo, quantidade);
    }

    /**
     * Consulta o identificador da parada onde o jogador está.
     *
     * @param cor cor do jogador
     * @return ID da parada ou null se a cor não estiver cadastrada
     */
    public String paradaDe(String cor) {
        Jogador jogador = jogadores.get(cor);
        // Não expõe o objeto Parada; somente seu ID é devolvido pela API pública.
        return jogador == null ? null : jogador.getParadaAtual().getId();
    }

    /**
     * Valor simples que representa uma peça escolhida para coleta.
     * Evita expor as classes internas do Model ao Controller.
     */
    public static final class EscolhaTributo {
        /** ID da província onde a peça está. */
        private final String provinciaId;

        /** Tipo da peça, informado como texto. */
        private final String tipo;

        /**
         * Cria uma escolha de tributo.
         *
         * @param provinciaId província de origem
         * @param tipo tipo da peça: ESPADA, YURT ou MOEDA
         */
        public EscolhaTributo(String provinciaId, String tipo) {
            this.provinciaId = provinciaId;
            this.tipo = tipo;
        }

        /** @return ID da província escolhida */
        public String getProvinciaId() {
            return provinciaId;
        }

        /** @return tipo de tributo escolhido */
        public String getTipo() {
            return tipo;
        }
    }
}

