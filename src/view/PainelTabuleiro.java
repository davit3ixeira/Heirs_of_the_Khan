package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

/** Desenha o tabuleiro e os piões com Graphics2D.drawImage(). */
class PainelTabuleiro extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int TAMANHO_PIAO = 50;

    private final Imagens imagens;
    private final MapaVisual mapa = new MapaVisual();
    private final List<OuvinteTabuleiro> ouvintes = new ArrayList<>();
    private Map<String, String> paradaPorCor = new LinkedHashMap<>();

    PainelTabuleiro(Imagens imagens, int altura) {
        this.imagens = imagens;
        int largura = (int) Math.round(altura * MapaVisual.LARGURA / MapaVisual.ALTURA);
        setPreferredSize(new Dimension(largura, altura));
        addMouseListener(new TratadorDeCliques());
    }

    void adicionarOuvinte(OuvinteTabuleiro ouvinte) {
        ouvintes.add(ouvinte);
    }

    void atualizar(Map<String, String> paradaPorCor) {
        this.paradaPorCor = new LinkedHashMap<>(paradaPorCor);
        repaint();
    }

    private double escalaX() {
        return getWidth() / MapaVisual.LARGURA;
    }

    private double escalaY() {
        return getHeight() / MapaVisual.ALTURA;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(imagens.getTabuleiro(), 0, 0, getWidth(), getHeight(), null);

        Map<String, Integer> ocupacao = new HashMap<>();
        for (Map.Entry<String, String> entrada : paradaPorCor.entrySet()) {
            String parada = entrada.getValue();
            int indice = ocupacao.getOrDefault(parada, 0);
            ocupacao.put(parada, indice + 1);
            Point centro = mapa.posicaoDoPiao(parada, indice);
            if (centro == null) {
                continue;
            }
            int largura = (int) Math.round(TAMANHO_PIAO * escalaX());
            int altura = (int) Math.round(TAMANHO_PIAO * escalaY());
            int x = (int) Math.round(centro.x * escalaX()) - largura / 2;
            int y = (int) Math.round(centro.y * escalaY()) - altura / 2;
            g2d.drawImage(imagens.getPiao(entrada.getKey()), x, y, largura, altura, null);
        }
    }

    private class TratadorDeCliques extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            String parada = mapa.paradaEm(e.getX() / escalaX(), e.getY() / escalaY());
            if (parada == null) {
                return;
            }
            for (OuvinteTabuleiro ouvinte : ouvintes) {
                ouvinte.paradaClicada(parada);
            }
        }
    }
}
