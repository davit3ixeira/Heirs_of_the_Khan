package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class JogoTest {
    private Jogo jogo;

    @Before
    public void preparar() {
        jogo = new Jogo();
        jogo.adicionarJogador("vermelho");
        jogo.adicionarJogador("azul");
    }

    @Test
    public void jogadorComecaEmKarakorum() {
        assertEquals("KARAKORUM", jogo.paradaDe("vermelho"));
    }

    @Test
    public void moveParaParadaVizinhaLivre() {
        assertTrue(jogo.moverJogador("vermelho", "P1"));
        assertEquals("P1", jogo.paradaDe("vermelho"));
    }

    @Test
    public void naoMoveParaParadaNaoVizinha() {
        assertFalse(jogo.moverJogador("vermelho", "P3"));
        assertEquals("KARAKORUM", jogo.paradaDe("vermelho"));
    }

    @Test
    public void naoTerminaEmParadaSimplesOcupada() {
        jogo.moverJogador("vermelho", "P1");
        assertFalse(jogo.moverJogador("azul", "P1"));
    }

    @Test
    public void podeTerminarEmParadaDuplaComOutroJogador() {
        jogo.moverJogador("vermelho", "P2");
        assertTrue(jogo.moverJogador("azul", "P2"));
    }
}
