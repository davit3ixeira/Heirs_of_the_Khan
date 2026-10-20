package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

/**
 * Testes originais de percursos e passagem por paradas.
 * Os cenários e as verificações foram mantidos como estavam em JogoTest.java.
 */
public class TabuleiroTest {
    private Jogo jogo;

    @Before
    public void preparar() {
        jogo = new Jogo();
        jogo.adicionarJogador("vermelho");
        jogo.adicionarJogador("azul");
    }


    /**
     * CT07 — Percurso com passos dentro do limite.
     * Pré-condição: vermelho em KARAKORUM; ação: percorrer P1 e P3 com limite dois.
     * Esperado: percurso aceito e posição final igual a P3.
     */
    @Test
    public void percorreMaisDeUmaParadaDentroDoLimite() {
        assertTrue(jogo.moverJogador("vermelho", Arrays.asList("P1", "P3"), 2));
        assertEquals("P3", jogo.paradaDe("vermelho"));
    }

    /**
     * CT08 — Percurso acima do limite permitido.
     * Pré-condição: vermelho em KARAKORUM; ação: solicitar dois passos com limite um.
     * Esperado: percurso rejeitado e posição inalterada.
     */
    @Test
    public void rejeitaPercursoMaiorQueLimiteSemMover() {
        assertFalse(jogo.moverJogador("vermelho", Arrays.asList("P1", "P3"), 1));
        assertEquals("KARAKORUM", jogo.paradaDe("vermelho"));
    }

    /**
     * CT09 — Passagem por parada ocupada.
     * Pré-condição: azul ocupa P1; ação: vermelho atravessa P1 e segue até P3.
     * Esperado: movimento aceito e vermelho termina em P3.
     */
    @Test
    public void podeAtravessarParadaSimplesOcupadaSemTerminarNela() {
        assertTrue(jogo.moverJogador("azul", "P1"));
        assertTrue(jogo.moverJogador("vermelho", Arrays.asList("P1", "P3"), 2));
        assertEquals("P3", jogo.paradaDe("vermelho"));
    }
}
