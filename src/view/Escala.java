package view;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

/**
 * Fator de escala da interface. As dimensões do projeto (janela de 1400 x 992 pixels) são
 * reduzidas para que a janela caiba na área útil da tela sem ocupá-la inteiramente. O valor
 * pode ser forçado com -Djogo.escala=0.8 na configuração de execução.
 */
final class Escala {
    private static final int LARGURA_PROJETO = 1400;
    private static final int ALTURA_PROJETO = 992;
    private static final double OCUPACAO_MAXIMA_ALTURA = 0.88;
    private static final double OCUPACAO_MAXIMA_LARGURA = 0.92;
    private static final double FATOR_MINIMO = 0.5;

    static final double FATOR = calcular();

    private Escala() {
    }

    static int px(int valor) {
        return (int) Math.round(valor * FATOR);
    }

    /** Converte uma coordenada de tela (pixels reais) para a coordenada do projeto. */
    static double doProjeto(int valor) {
        return valor / FATOR;
    }

    private static double calcular() {
        String forcado = System.getProperty("jogo.escala");
        if (forcado != null) {
            try {
                double fator = Double.parseDouble(forcado);
                if (fator > 0) {
                    return fator;
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido para jogo.escala: " + forcado);
            }
        }
        if (GraphicsEnvironment.isHeadless()) {
            return 1.0;
        }
        Rectangle area = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        double porAltura = OCUPACAO_MAXIMA_ALTURA * area.height / ALTURA_PROJETO;
        double porLargura = OCUPACAO_MAXIMA_LARGURA * area.width / LARGURA_PROJETO;
        return Math.max(FATOR_MINIMO, Math.min(1.0, Math.min(porAltura, porLargura)));
    }

    /** Área útil da tela (sem barra de tarefas), ou null se não houver tela. */
    static Rectangle areaUtil() {
        if (GraphicsEnvironment.isHeadless()) {
            return null;
        }
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    }
}
