package view;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import model.Fachada;
import model.Observado;
import model.Observador;

public class PainelJogador extends JPanel implements Observador {
    public static final int LARGURA = 420;
    public static final int ALTURA = 229;
    public static final int DIAM_PECA = 30;

    static final double LARG_REF = 1400.0;
    static final double ALT_REF = 763.0;
    static final double[] COL_INICIO = {365, 612, 858, 1135};
    static final double[] COL_FIM = {575, 822, 1068, 1345};
    static final double TOPO = 70;
    static final double BASE = 690;

    Image[] cartelas;
    Image img;
    String cor = "";
    boolean[] ocupadas = new boolean[4];
    String[][] melhorias = {{}, {}, {}, {}};

    public PainelJogador(Image[] cartelas) {
        this.cartelas = cartelas;
    }

    public void notificar(Observado o) {
        int numero = (Integer) o.get(Fachada.TABULEIRO);
        melhorias = (String[][]) o.get(Fachada.MELHORIAS_JOGADOR);
        setJogador(cartelas[numero - 1], (String) o.get(Fachada.VEZ), (boolean[]) o.get(Fachada.OCUPADAS));
    }

    public void setJogador(Image i, String c, boolean[] o) {
        img = i;
        cor = c;
        ocupadas = o;
        repaint();
    }

    public int colunaEm(int x, int y) {
        double rx = Escala.doProjeto(x) * LARG_REF / LARGURA;
        double ry = Escala.doProjeto(y) * ALT_REF / ALTURA;
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
        g2d.scale(Escala.FATOR, Escala.FATOR);
        g2d.drawImage(img, 0, 0, LARGURA, ALTURA, null);
        desenharMelhorias(g2d);

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

    private void desenharMelhorias(Graphics2D g2d) {
        double altLinha = (BASE - TOPO) / 5.0;
        for (int c = 0; c < melhorias.length; c++) {
            for (int r = 0; r < melhorias[c].length; r++) {
                double topoRef = BASE - altLinha * (r + 2) + 8;
                double x = (COL_INICIO[c] + 8) * LARGURA / LARG_REF;
                double y = topoRef * ALTURA / ALT_REF;
                double w = (COL_FIM[c] - COL_INICIO[c] - 16) * LARGURA / LARG_REF;
                double h = (altLinha - 16) * ALTURA / ALT_REF;
                g2d.drawImage(Imagens.melhoria(melhorias[c][r]), (int) x, (int) y, (int) w, (int) h, null);
            }
        }
    }
}
