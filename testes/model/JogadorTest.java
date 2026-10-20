package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

/**
 * Teste original de validação do gasto de tributos do jogador.
 * Os cenários e as verificações foram mantidos como estavam em JogoTest.java.
 */
public class JogadorTest {
    private Jogo jogo;

    @Before
    public void preparar() {
        jogo = new Jogo();
        jogo.adicionarJogador("vermelho");
        jogo.adicionarJogador("azul");
    }


    /**
     * CT14 — Validação do gasto de tributos.
     * Pré-condição: vermelho possui uma espada; ação: tentar gastar duas e depois gastar uma.
     * Esperado: primeiro gasto rejeitado, segundo aceito e saldo final igual a zero.
     */
    @Test
    public void jogadorSoPodeGastarTributosQuePossui() {
        assertTrue(jogo.adicionarProvincia("Samarcanda"));
        assertTrue(jogo.associarProvincia("P1", "Samarcanda"));
        assertTrue(jogo.colocarTributosIniciais("Samarcanda", "ESPADA", 1));
        assertTrue(jogo.iniciarTurno("vermelho"));
        assertTrue(jogo.moverJogador("vermelho", "P1"));
        assertTrue(jogo.coletarTributos("vermelho", 1, Collections.singletonList(
                new Jogo.EscolhaTributo("Samarcanda", "ESPADA"))));

        assertFalse(jogo.gastarTributos("vermelho", "ESPADA", 2));
        assertTrue(jogo.gastarTributos("vermelho", "ESPADA", 1));
        assertEquals(0, jogo.quantidadeTributoDoJogador("vermelho", "ESPADA"));
    }
}
