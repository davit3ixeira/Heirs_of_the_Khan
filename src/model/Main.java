package model;
import model.Jogo;

/**
 * Papel da classe: Ponto de entrada de demonstração do modelo.
 *
 * Por que existe no jogo: Permite ver rapidamente o início de uma partida sem uma interface
 * gráfica.
 */
public class Main {
    /**
     * O que faz: Cria um jogo, adiciona o jogador vermelho, mostra sua parada inicial, move-o para
     * P1 e mostra a nova parada.
     *
     * Por que importa no jogo: Demonstra o fluxo básico das regras: todo peão começa em Karakorum e
     * pode se mover para uma parada adjacente.
     */
    public static void main(String[] args) {
        Jogo jogo = new Jogo();
        jogo.adicionarJogador("vermelho");
        System.out.println(jogo.paradaDe("vermelho"));
        System.out.println(jogo.moverJogador("vermelho", "P1"));
        System.out.println(jogo.paradaDe("vermelho"));
    }
}
