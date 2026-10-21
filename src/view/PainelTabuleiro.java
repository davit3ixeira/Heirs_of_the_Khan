package view;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class PainelTabuleiro extends JPanel {
    public static final int LARGURA = 936;
    public static final int ALTURA = 940;
    public static final int DIAM_PEAO = 22;

    Image img;
    MapaVisual mapa = new MapaVisual();
    String[] cores = new String[0];
    String[] paradas = new String[0];

    public PainelTabuleiro(Image i) {
        img = i;
    }

    public void setPeoes(String[] c, String[] p) {
        cores = c;
        paradas = p;
        repaint();
    }

    public String paradaEm(int x, int y) {
        return mapa.paradaEm(x * MapaVisual.LARGURA / LARGURA, y * MapaVisual.ALTURA / ALTURA);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(img, 0, 0, LARGURA, ALTURA, null);

        for (int i = 0; i < cores.length; i++) {
            int indice = 0;
            for (int j = 0; j < i; j++) {
                if (paradas[j].equals(paradas[i])) {
                    indice++;
                }
            }
            Point p = mapa.posicaoDoPiao(paradas[i], indice);
            double cX = p.x * LARGURA / MapaVisual.LARGURA;
            double cY = p.y * ALTURA / MapaVisual.ALTURA;
            Ellipse2D circ = new Ellipse2D.Double();
            circ.setFrameFromCenter(cX, cY, cX + DIAM_PEAO / 2, cY + DIAM_PEAO / 2);
            g2d.setPaint(Cores.converter(cores[i]));
            g2d.fill(circ);
            g2d.setPaint(Color.BLACK);
            g2d.draw(circ);
        }
    }
}
