package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

/**
 * Casos de teste das regras de movimento e tributos.
 * Cada caso registra o cenário/ação e o resultado esperado; o @Before cria um
 * jogo novo com os jogadores vermelho e azul, salvo quando o teste adiciona outros.
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
