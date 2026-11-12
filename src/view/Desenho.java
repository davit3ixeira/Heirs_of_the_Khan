package view;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

class Desenho {
    private Desenho() {
    }

    static void imagemRedonda(Graphics2D g2d, Image imagem, double x, double y, double largura, double altura) {
        Shape anterior = g2d.getClip();
        g2d.clip(new Ellipse2D.Double(x + 0.04 * largura, y + 0.04 * altura, 0.92 * largura, 0.92 * altura));
        g2d.drawImage(imagem, (int) x, (int) y, (int) largura, (int) altura, null);
        g2d.setClip(anterior);
    }
}
