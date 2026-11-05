package view;

import java.awt.*;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import model.Fachada;
import model.Observado;
import model.Observador;

public class PainelTabuleiro extends JPanel implements Observador {
    public static final int LARGURA = 936;
    public static final int ALTURA = 940;
    public static final int DIAM_PEAO = 22;

    Image img;
    MapaVisual mapa = new MapaVisual();
    String[] cores = new String[0];
    String[] paradas = new String[0];
    Map<String, Integer> tributos = new HashMap<>();

    public PainelTabuleiro(Image i) {
        img = i;
    }

    @SuppressWarnings("unchecked")
    public void notificar(Observado o) {
        tributos = (Map<String, Integer>) o.get(Fachada.TRIBUTOS_PROVINCIAS);
        setPeoes((String[]) o.get(Fachada.CORES), (String[]) o.get(Fachada.PARADAS));
    }

    public void setPeoes(String[] c, String[] p) {
        cores = c;
        paradas = p;
        repaint();
    }

    public String paradaEm(int x, int y) {
        return mapa.paradaEm(x * MapaVisual.LARGURA / LARGURA, y * MapaVisual.ALTURA / ALTURA);
    }

    public String provinciaEm(int x, int y) {
        return mapa.provinciaEm(x * MapaVisual.LARGURA / LARGURA, y * MapaVisual.ALTURA / ALTURA);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(img, 0, 0, LARGURA, ALTURA, null);

        for (Map.Entry<String, Integer> t : tributos.entrySet()) {
            Point p = mapa.posicaoDaProvincia(t.getKey());
            double cX = p.x * LARGURA / MapaVisual.LARGURA + 16;
            double cY = p.y * ALTURA / MapaVisual.ALTURA + 16;
            Ellipse2D marca = new Ellipse2D.Double();
            marca.setFrameFromCenter(cX, cY, cX + 9, cY + 9);
            g2d.setPaint(Color.WHITE);
            g2d.fill(marca);
            g2d.setPaint(Color.BLACK);
            g2d.draw(marca);
            g2d.drawString(String.valueOf(t.getValue()), (int) cX - 3, (int) cY + 4);
        }

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
