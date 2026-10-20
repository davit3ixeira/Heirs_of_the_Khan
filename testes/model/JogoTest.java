package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

/**
 * Testes originais de posição inicial e movimento básico.
 * Os cenários e as verificações foram mantidos como estavam em JogoTest.java.
 */
public class JogoTest {
    private Jogo jogo;

    @Before
    public void preparar() {
        jogo = new Jogo();
        jogo.adicionarJogador("vermelho");
        jogo.adicionarJogador("azul");
    }


    /**
     * CT01 — Posição inicial.
     * Pré-condição: fixture padrão; ação: consultar a posição de vermelho.
     * Esperado: retorno igual a "KARAKORUM".
     */
    @Test
    public void jogadorComecaEmKarakorum() {
        assertEquals("KARAKORUM", jogo.paradaDe("vermelho"));
    }

    /**
     * CT02 — Movimento para parada vizinha livre.
     * Pré-condição: vermelho em KARAKORUM; ação: mover para P1.
     * Esperado: movimento aceito e nova posição igual a P1.
     */
    @Test
    public void moveParaParadaVizinhaLivre() {
        assertTrue(jogo.moverJogador("vermelho", "P1"));
        assertEquals("P1", jogo.paradaDe("vermelho"));
    }

    /**
     * CT03 — Tentativa de movimento não adjacente.
     * Pré-condição: vermelho em KARAKORUM; ação: tentar mover diretamente para P3.
     * Esperado: movimento rejeitado e posição mantida em KARAKORUM.
     */
    @Test
    public void naoMoveParaParadaNaoVizinha() {
        assertFalse(jogo.moverJogador("vermelho", "P3"));
        assertEquals("KARAKORUM", jogo.paradaDe("vermelho"));
    }
}
