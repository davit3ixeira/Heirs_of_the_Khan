package model;

import java.util.HashMap;
import java.util.Map;

public class Jogo {
    private final Tabuleiro tabuleiro = new Tabuleiro();
    private final Map<String, Jogador> jogadores = new HashMap<>();

    public Jogo() {
        tabuleiro.adicionarParada("P1", 1);
        tabuleiro.adicionarParada("P2", 2);
        tabuleiro.adicionarParada("P3", 1);
        tabuleiro.ligarParadas(Tabuleiro.KARAKORUM, "P1");
        tabuleiro.ligarParadas(Tabuleiro.KARAKORUM, "P2");
        tabuleiro.ligarParadas("P1", "P3");
    }

    public void adicionarJogador(String cor) {
        Parada inicio = tabuleiro.getParada(Tabuleiro.KARAKORUM);
        jogadores.put(cor, new Jogador(cor, inicio));
    }

    public boolean moverJogador(String cor, String destino) {
        Jogador jogador = jogadores.get(cor);
        if (jogador == null) {
            return false;
        }
        return tabuleiro.moverJogador(jogador, destino);
    }

    public String paradaDe(String cor) {
        return jogadores.get(cor).getParadaAtual().getId();
    }
}
