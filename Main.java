import model.Jogo;

public class Main {
    public static void main(String[] args) {
        Jogo jogo = new Jogo();
        jogo.adicionarJogador("vermelho");
        System.out.println(jogo.paradaDe("vermelho"));
        System.out.println(jogo.moverJogador("vermelho", "P1"));
        System.out.println(jogo.paradaDe("vermelho"));
    }
}
