package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

/**
 * Testes originais de estoque provincial e coleta de tributos.
 * Os cenários e as verificações foram mantidos como estavam em JogoTest.java.
 */
public class ProvinciaTest {
    private Jogo jogo;

    @Before
    public void preparar() {
        jogo = new Jogo();
        jogo.adicionarJogador("vermelho");
        jogo.adicionarJogador("azul");
    }


    /**
     * CT10 — Limite de três peças por província.
     * Pré-condição: província vazia; ação: inserir três tributos e tentar inserir um quarto.
     * Esperado: quarto tributo rejeitado e total mantido em três.
     */
    @Test
    public void provinciaNaoAceitaMaisDeTresPecas() {
        assertTrue(jogo.adicionarProvincia("Samarcanda"));
        assertTrue(jogo.colocarTributosIniciais("Samarcanda", "ESPADA", 2));
        assertTrue(jogo.colocarTributosIniciais("Samarcanda", "MOEDA", 1));
        assertFalse(jogo.colocarTributosIniciais("Samarcanda", "YURT", 1));
        assertEquals(3, jogo.quantidadeTributoNaProvincia("Samarcanda", "YURT")
                + jogo.quantidadeTributoNaProvincia("Samarcanda", "MOEDA")
                + jogo.quantidadeTributoNaProvincia("Samarcanda", "ESPADA"));
    }

    /**
     * CT11 — Coleta válida de tributos adjacentes ao percurso.
     * Pré-condição: província com espada e moeda, adjacente a P1; ação: visitar P1 e coletar ambas.
     * Esperado: jogador recebe uma peça de cada tipo e a espada é removida da província.
     */
    @Test
    public void coletaTributosAdjacentesAoPercursoETransfereAoJogador() {
        assertTrue(jogo.adicionarProvincia("Samarcanda"));
        assertTrue(jogo.associarProvincia("P1", "Samarcanda"));
        assertTrue(jogo.colocarTributosIniciais("Samarcanda", "ESPADA", 1));
        assertTrue(jogo.colocarTributosIniciais("Samarcanda", "MOEDA", 1));

        assertTrue(jogo.iniciarTurno("vermelho"));
        assertTrue(jogo.moverJogador("vermelho", Arrays.asList("P1", "P3"), 2));
        assertTrue(jogo.coletarTributos("vermelho", 2, Arrays.asList(
                new Jogo.EscolhaTributo("Samarcanda", "ESPADA"),
                new Jogo.EscolhaTributo("Samarcanda", "MOEDA"))));

        assertEquals(1, jogo.quantidadeTributoDoJogador("vermelho", "ESPADA"));
        assertEquals(1, jogo.quantidadeTributoDoJogador("vermelho", "MOEDA"));
        assertEquals(0, jogo.quantidadeTributoNaProvincia("Samarcanda", "ESPADA"));
        assertEquals(0, jogo.quantidadeTributoNaProvincia("Samarcanda", "MOEDA"));
    }

    /**
     * CT12 — Coleta em província fora do percurso.
     * Pré-condição: tributo em província adjacente a P2; ação: tentar coletar sem visitar P2.
     * Esperado: coleta rejeitada e tributo permanece na província.
     */
    @Test
    public void naoColetaTributoDeProvinciaForaDoPercurso() {
        assertTrue(jogo.adicionarProvincia("China"));
        assertTrue(jogo.associarProvincia("P2", "China"));
        assertTrue(jogo.colocarTributosIniciais("China", "YURT", 1));
        assertTrue(jogo.iniciarTurno("vermelho"));

        assertFalse(jogo.coletarTributos("vermelho", 1, Collections.singletonList(
                new Jogo.EscolhaTributo("China", "YURT"))));
        assertEquals(1, jogo.quantidadeTributoNaProvincia("China", "YURT"));
    }

    /**
     * CT13 — Limite de peças por ação de coleta.
     * Pré-condição: duas espadas disponíveis em província visitada; ação: solicitar duas com limite um.
     * Esperado: solicitação rejeitada e ambas as espadas permanecem na província.
     */
    @Test
    public void naoColetaMaisPecasDoQuePermiteAColuna() {
        assertTrue(jogo.adicionarProvincia("Samarcanda"));
        assertTrue(jogo.associarProvincia("P1", "Samarcanda"));
        assertTrue(jogo.colocarTributosIniciais("Samarcanda", "ESPADA", 2));
        assertTrue(jogo.iniciarTurno("vermelho"));
        assertTrue(jogo.moverJogador("vermelho", "P1"));

        assertFalse(jogo.coletarTributos("vermelho", 1, Arrays.asList(
                new Jogo.EscolhaTributo("Samarcanda", "ESPADA"),
                new Jogo.EscolhaTributo("Samarcanda", "ESPADA"))));
        assertEquals(2, jogo.quantidadeTributoNaProvincia("Samarcanda", "ESPADA"));
    }
}
