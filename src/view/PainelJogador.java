package view;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class PainelJogador extends JPanel {
    public static final int LARGURA = 420;
    public static final int ALTURA = 229;
    public static final int DIAM_PECA = 30;

    static final double LARG_REF = 1400.0;
    static final double ALT_REF = 763.0;
    static final double[] COL_INICIO = {365, 612, 858, 1135};
    static final double[] COL_FIM = {575, 822, 1068, 1345};
    static final double TOPO = 70;
    static final double BASE = 690;

    Image img;
    String cor = "";
    boolean[] ocupadas = new boolean[4];

    public PainelJogador() {
    }

    public void setJogador(Image i, String c, boolean[] o) {
        img = i;
        cor = c;
        ocupadas = o;
        repaint();
    }

    public int colunaEm(int x, int y) {
        double rx = x * LARG_REF / LARGURA;
        double ry = y * ALT_REF / ALTURA;
        if (ry < TOPO || ry > BASE)
            return -1;
        for (int i = 0; i < COL_INICIO.length; i++) {
            if (rx >= COL_INICIO[i] && rx <= COL_FIM[i])
                return i;
        }
        return -1;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (img == null)
            return;
        g2d.drawImage(img, 0, 0, LARGURA, ALTURA, null);

        for (int i = 0; i < ocupadas.length; i++) {
            if (!ocupadas[i])
                continue;
            double cX = (COL_INICIO[i] + COL_FIM[i]) / 2 * LARGURA / LARG_REF;
            double cY = 635 * ALTURA / ALT_REF;
            Ellipse2D circ = new Ellipse2D.Double();
            circ.setFrameFromCenter(cX, cY, cX + DIAM_PECA / 2, cY + DIAM_PECA / 2);
            g2d.setPaint(Cores.converter(cor));
            g2d.fill(circ);
            g2d.setPaint(Color.BLACK);
            g2d.draw(circ);
        }
    }
}
