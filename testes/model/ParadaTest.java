package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

/**
 * Testes originais de ocupação e capacidade das paradas.
 * Os cenários e as verificações foram mantidos como estavam em JogoTest.java.
 */
public class ParadaTest {
    private Jogo jogo;

    @Before
    public void preparar() {
        jogo = new Jogo();
        jogo.adicionarJogador("vermelho");
        jogo.adicionarJogador("azul");
    }


    /**
     * CT04 — Entrada em parada simples ocupada.
     * Pré-condição: vermelho ocupa P1; ação: azul tenta terminar em P1.
     * Esperado: tentativa rejeitada e azul permanece em KARAKORUM.
     */
    @Test
    public void naoTerminaEmParadaSimplesOcupada() {
        assertTrue(jogo.moverJogador("vermelho", "P1"));
        assertFalse(jogo.moverJogador("azul", "P1"));
        assertEquals("KARAKORUM", jogo.paradaDe("azul"));
    }

    /**
     * CT05 — Uso da capacidade dupla.
     * Pré-condição: P2 tem capacidade dois; ação: vermelho e azul entram em P2.
     * Esperado: os dois movimentos são aceitos.
     */
    @Test
    public void podeTerminarEmParadaDuplaComOutroJogador() {
        assertTrue(jogo.moverJogador("vermelho", "P2"));
        assertTrue(jogo.moverJogador("azul", "P2"));
    }

    /**
     * CT06 — Tentativa de exceder a capacidade.
     * Pré-condição: vermelho e azul ocupam P2; ação: verde também tenta entrar.
     * Esperado: tentativa rejeitada e verde permanece em KARAKORUM.
     */
    @Test
    public void terceiroJogadorNaoPodeTerminarEmParadaDuplaCheia() {
        jogo.adicionarJogador("verde");
        assertTrue(jogo.moverJogador("vermelho", "P2"));
        assertTrue(jogo.moverJogador("azul", "P2"));
        assertFalse(jogo.moverJogador("verde", "P2"));
        assertEquals("KARAKORUM", jogo.paradaDe("verde"));
    }
}
