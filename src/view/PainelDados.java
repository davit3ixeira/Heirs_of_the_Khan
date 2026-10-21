package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

/** Área dos dados, pintada com a cor do jogador da vez. */
class PainelDados extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int TAMANHO_DADO = 70;
    private static final Map<String, Color> CORES = new HashMap<>();

    static {
        CORES.put("vermelho", new Color(205, 30, 30));
        CORES.put("azul", new Color(35, 75, 210));
        CORES.put("verde", new Color(30, 150, 55));
        CORES.put("amarelo", new Color(245, 205, 25));
        CORES.put("preto", new Color(35, 35, 35));
    }

    private final Imagens imagens;
    private String corDaVez;
    private int dado1;
    private int dado2;
    private boolean dadosLancados;

    PainelDados(Imagens imagens) {
        this.imagens = imagens;
        setPreferredSize(new Dimension(200, 110));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
    }

    void atualizar(String corDaVez, int dado1, int dado2, boolean dadosLancados) {
        this.corDaVez = corDaVez;
        this.dado1 = dado1;
        this.dado2 = dado2;
        this.dadosLancados = dadosLancados;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setColor(CORES.getOrDefault(corDaVez, Color.GRAY));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        if (!dadosLancados) {
            return;
        }
        int espaco = (getWidth() - 2 * TAMANHO_DADO) / 3;
        int y = (getHeight() - TAMANHO_DADO) / 2;
        g2d.drawImage(imagens.getDado(dado1), espaco, y, TAMANHO_DADO, TAMANHO_DADO, null);
        g2d.drawImage(imagens.getDado(dado2), 2 * espaco + TAMANHO_DADO, y,
                TAMANHO_DADO, TAMANHO_DADO, null);
    }
}
