package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

/** Testes unitários das regras de movimento e tributos implementadas nesta etapa. */
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
        assertTrue(jogo.moverJogador("vermelho", "P1"));
        assertFalse(jogo.moverJogador("azul", "P1"));
        assertEquals("KARAKORUM", jogo.paradaDe("azul"));
    }

    @Test
    public void podeTerminarEmParadaDuplaComOutroJogador() {
        assertTrue(jogo.moverJogador("vermelho", "P2"));
        assertTrue(jogo.moverJogador("azul", "P2"));
    }

    @Test
    public void terceiroJogadorNaoPodeTerminarEmParadaDuplaCheia() {
        jogo.adicionarJogador("verde");
        assertTrue(jogo.moverJogador("vermelho", "P2"));
        assertTrue(jogo.moverJogador("azul", "P2"));
        assertFalse(jogo.moverJogador("verde", "P2"));
        assertEquals("KARAKORUM", jogo.paradaDe("verde"));
    }

    @Test
    public void percorreMaisDeUmaParadaDentroDoLimite() {
        assertTrue(jogo.moverJogador("vermelho", Arrays.asList("P1", "P3"), 2));
        assertEquals("P3", jogo.paradaDe("vermelho"));
    }

    @Test
    public void rejeitaPercursoMaiorQueLimiteSemMover() {
        assertFalse(jogo.moverJogador("vermelho", Arrays.asList("P1", "P3"), 1));
        assertEquals("KARAKORUM", jogo.paradaDe("vermelho"));
    }

    @Test
    public void podeAtravessarParadaSimplesOcupadaSemTerminarNela() {
        assertTrue(jogo.moverJogador("azul", "P1"));
        assertTrue(jogo.moverJogador("vermelho", Arrays.asList("P1", "P3"), 2));
        assertEquals("P3", jogo.paradaDe("vermelho"));
    }

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
    }

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
