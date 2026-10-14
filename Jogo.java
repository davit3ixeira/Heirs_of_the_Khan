package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API pública inicial do Model. O mapa P1-P3 é apenas uma rede demonstrativa;
 * a aplicação pode montar outra rede pelas operações públicas de configuração.
 */
public class Jogo {
    private static final int MAX_JOGADORES = 5;

    private final Tabuleiro tabuleiro = new Tabuleiro();
    private final Map<String, Jogador> jogadores = new HashMap<>();

    public Jogo() {
        // Mantido para o exemplo Main.java e para os testes da entrega anterior.
        tabuleiro.adicionarParada("P1", 1);
        tabuleiro.adicionarParada("P2", 2);
        tabuleiro.adicionarParada("P3", 1);
        tabuleiro.ligarParadas(Tabuleiro.KARAKORUM, "P1");
        tabuleiro.ligarParadas(Tabuleiro.KARAKORUM, "P2");
        tabuleiro.ligarParadas("P1", "P3");
    }

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

    /** Monta o grafo do mapa antes de iniciar a partida. */
    public boolean adicionarParada(String id, int capacidade) {
        return tabuleiro.adicionarParada(id, capacidade);
    }

    public boolean ligarParadas(String idA, String idB) {
        return tabuleiro.ligarParadas(idA, idB);
    }

    public boolean adicionarProvincia(String id) {
        return tabuleiro.adicionarProvincia(id);
    }

    public boolean associarProvincia(String paradaId, String provinciaId) {
        return tabuleiro.associarProvincia(paradaId, provinciaId);
    }

    /** Prepara o percurso do jogador para um turno; inclui a parada onde ele começa. */
    public boolean iniciarTurno(String cor) {
        Jogador jogador = jogadores.get(cor);
        if (jogador == null) {
            return false;
        }
        jogador.iniciarTurno();
        return true;
    }

    /** Atalho compatível com a versão inicial: move exatamente uma parada. */
    public boolean moverJogador(String cor, String destino) {
        if (destino == null) {
            return false;
        }
        return moverJogador(cor, List.of(destino), 1);
    }

    /**
     * Move por uma sequência de paradas adjacentes. O Controller fornece o limite
     * obtido da ação/cavalos ativados. Paradas intermediárias ocupadas podem ser cruzadas.
     */
    public boolean moverJogador(String cor, List<String> percurso, int limiteMovimentos) {
        Jogador jogador = jogadores.get(cor);
        if (jogador == null) {
            return false;
        }
        return tabuleiro.moverJogador(jogador, percurso, limiteMovimentos);
    }

    /**
     * Retira peças das províncias adjacentes às paradas visitadas no turno e as
     * entrega ao jogador. Toda a solicitação é validada antes de alterar o estado.
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

    /** Adiciona peças ao mapa durante a preparação da partida. */
    public boolean colocarTributosIniciais(String provinciaId, String tipo, int quantidade) {
        Provincia provincia = tabuleiro.getProvincia(provinciaId);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return provincia != null && provincia.adicionarTributos(tributo, quantidade);
    }

    public int quantidadeTributoNaProvincia(String provinciaId, String tipo) {
        Provincia provincia = tabuleiro.getProvincia(provinciaId);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return provincia == null || tributo == null ? -1 : provincia.quantidade(tributo);
    }

    public int quantidadeTributoDoJogador(String cor, String tipo) {
        Jogador jogador = jogadores.get(cor);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return jogador == null || tributo == null ? -1 : jogador.quantidadeTributo(tributo);
    }

    /** Remove tributos do inventário do jogador para pagar uma ação. */
    public boolean gastarTributos(String cor, String tipo, int quantidade) {
        Jogador jogador = jogadores.get(cor);
        TipoTributo tributo = TipoTributo.interpretar(tipo);
        return jogador != null && tributo != null && jogador.gastarTributos(tributo, quantidade);
    }

    /** Retorna null se a cor ainda não foi registrada. */
    public String paradaDe(String cor) {
        Jogador jogador = jogadores.get(cor);
        return jogador == null ? null : jogador.getParadaAtual().getId();
    }

    /** DTO da escolha feita pelo Controller; não expõe as classes internas do Model. */
    public static final class EscolhaTributo {
        private final String provinciaId;
        private final String tipo;

        public EscolhaTributo(String provinciaId, String tipo) {
            this.provinciaId = provinciaId;
            this.tipo = tipo;
        }

        public String getProvinciaId() {
            return provinciaId;
        }

        public String getTipo() {
            return tipo;
        }
    }
}
