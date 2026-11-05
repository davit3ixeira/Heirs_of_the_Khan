package view;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class PainelCartas extends JPanel {
    public static final int LARGURA = 910;
    public static final int ALTURA = 330;
    public static final int LARG_CARTA = 160;
    public static final int ALT_CARTA = 250;

    Map<String, Image> imagens = new HashMap<>();
    String[] abertas = new String[0];
    String secreta;
    String[] oferecidas = new String[0];
    String escolhendo;

    public void setCartas(String[] a, String s, String[] of, String e) {
        abertas = a;
        secreta = s;
        oferecidas = of;
        escolhendo = e;
        repaint();
    }

    public String cartaEm(int x, int y) {
        for (int i = 0; i < oferecidas.length; i++) {
            int x0 = 10 + i * 170;
            if (x >= x0 && x <= x0 + LARG_CARTA && y >= 30 && y <= 30 + ALT_CARTA)
                return oferecidas[i];
        }
        return null;
    }

    Image imagem(String id) {
        if (!imagens.containsKey(id))
            imagens.put(id, JanelaJogo.carregar("assets/cartas/cartas" + id + ".jpg"));
        return imagens.get(id);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (oferecidas.length > 0) {
            g2d.drawString("Jogador " + escolhendo + ": clique na carta secreta que deseja manter", 10, 20);
            for (int i = 0; i < oferecidas.length; i++)
                g2d.drawImage(imagem(oferecidas[i]), 10 + i * 170, 30, LARG_CARTA, ALT_CARTA, null);
            return;
        }
        g2d.drawString("Cartas de conselheiro abertas", 10, 20);
        for (int i = 0; i < abertas.length; i++)
            g2d.drawImage(imagem(abertas[i]), 10 + i * 170, 30, LARG_CARTA, ALT_CARTA, null);
        g2d.drawString("Sua carta secreta", 720, 20);
        if (secreta != null)
            g2d.drawImage(imagem(secreta), 720, 30, LARG_CARTA, ALT_CARTA, null);
    }
}
