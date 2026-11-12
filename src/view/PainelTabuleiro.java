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

    static final int TAM_TRIBUTO = 40;
    static final int TAM_TESOURO = 40;
    static final int TAM_BONUS = 56;

    Image img;
    MapaVisual mapa = new MapaVisual();
    String[] cores = new String[0];
    String[] paradas = new String[0];
    Map<String, int[]> provincias = new HashMap<>();
    Map<String, String[]> yurts = new HashMap<>();
    String[][] cidades = new String[0][];
    String[][] bonusConselheiros = new String[0][];
    String[] faixa = new String[0];
    String khan = null;
    boolean khanPendente = false;
    String escolha = null;
    int pilha = 0;

    public PainelTabuleiro(Image i) {
        img = i;
    }

    @SuppressWarnings("unchecked")
    public void notificar(Observado o) {
        provincias = (Map<String, int[]>) o.get(Fachada.PROVINCIAS_DETALHE);
        yurts = (Map<String, String[]>) o.get(Fachada.YURTS);
        cidades = (String[][]) o.get(Fachada.CIDADES);
        bonusConselheiros = (String[][]) o.get(Fachada.BONUS_CONSELHEIROS);
        faixa = (String[]) o.get(Fachada.FAIXA);
        khan = (String) o.get(Fachada.KHAN);
        khanPendente = (Boolean) o.get(Fachada.KHAN_PENDENTE);
        escolha = (String) o.get(Fachada.ESCOLHA);
        pilha = (Integer) o.get(Fachada.PILHA_MELHORIAS);
        setPeoes((String[]) o.get(Fachada.CORES), (String[]) o.get(Fachada.PARADAS));
    }

    public void setPeoes(String[] c, String[] p) {
        cores = c;
        paradas = p;
        repaint();
    }

    private double refX(int x) {
        return Escala.doProjeto(x) * MapaVisual.LARGURA / LARGURA;
    }

    private double refY(int y) {
        return Escala.doProjeto(y) * MapaVisual.ALTURA / ALTURA;
    }

    private double telaX(double x) {
        return x * LARGURA / MapaVisual.LARGURA;
    }

    private double telaY(double y) {
        return y * ALTURA / MapaVisual.ALTURA;
    }

    public String paradaEm(int x, int y) {
        return mapa.paradaEm(refX(x), refY(y));
    }

    public String conselheiroEm(int x, int y) {
        return mapa.conselheiroEm(refX(x), refY(y));
    }

    public String localDoKhanEm(int x, int y) {
        return mapa.localDoKhanEm(refX(x), refY(y));
    }

    public int faixaEm(int x, int y) {
        return mapa.faixaEm(refX(x), refY(y));
    }

    public String[] tributoEm(int x, int y) {
        double rx = refX(x);
        double ry = refY(y);
        for (Map.Entry<String, int[]> e : provincias.entrySet()) {
            Point centro = mapa.posicaoDaProvincia(e.getKey());
            int[] tipos = tiposPresentes(e.getValue());
            for (int k = 0; k < tipos.length; k++) {
                double cx = centro.x + (k - (tipos.length - 1) / 2.0) * (TAM_TRIBUTO + 4);
                if (Math.abs(rx - cx) <= TAM_TRIBUTO / 2.0 && Math.abs(ry - centro.y) <= TAM_TRIBUTO / 2.0) {
                    return new String[] {e.getKey(), Imagens.TIPOS_TRIBUTO[tipos[k]]};
                }
            }
        }
        return null;
    }

    public Object[] tesouroDeCidadeEm(int x, int y) {
        double rx = refX(x);
        double ry = refY(y);
        for (String[] cidade : cidades) {
            if (!"ATIVA".equals(cidade[1])) {
                continue;
            }
            Point centro = mapa.posicaoDaCidade(cidade[0]);
            String[] tesouros = cidade[3].isEmpty() ? new String[0] : cidade[3].split(",");
            for (int k = 0; k < tesouros.length; k++) {
                Point p = posicaoDoTesouro(centro, k);
                if (Math.abs(rx - p.x) <= TAM_TESOURO / 2.0 && Math.abs(ry - p.y) <= TAM_TESOURO / 2.0) {
                    return new Object[] {cidade[0], k};
                }
            }
        }
        return null;
    }

    private Point posicaoDoTesouro(Point centro, int k) {
        int dx = (k % 2 == 0) ? -22 : 22;
        int dy = (k / 2 == 0) ? -27 : 17;
        return new Point(centro.x + dx, centro.y + dy);
    }

    private int[] tiposPresentes(int[] quantidades) {
        int total = quantidades[0] + quantidades[1] + quantidades[2];
        int[] tipos = new int[total];
        int k = 0;
        for (int t = 0; t < quantidades.length; t++) {
            for (int i = 0; i < quantidades[t]; i++) {
                tipos[k++] = t;
            }
        }
        return tipos;
    }

    private void desenharImagem(Graphics2D g2d, Image imagem, double cx, double cy, int largRef, int altRef) {
        double w = telaX(largRef);
        double h = telaY(altRef);
        g2d.drawImage(imagem, (int) (telaX(cx) - w / 2), (int) (telaY(cy) - h / 2), (int) w, (int) h, null);
    }

    private void desenharPecaRedonda(Graphics2D g2d, Image imagem, double cx, double cy, int tamRef) {
        double w = telaX(tamRef);
        double h = telaY(tamRef);
        Desenho.imagemRedonda(g2d, imagem, telaX(cx) - w / 2, telaY(cy) - h / 2, w, h);
    }

    private void desenharYurt(Graphics2D g2d, double cx, double cy, Color cor) {
        double x = telaX(cx);
        double y = telaY(cy);
        Path2D casa = new Path2D.Double();
        casa.moveTo(x - 10, y);
        casa.lineTo(x - 10, y - 8);
        casa.lineTo(x, y - 18);
        casa.lineTo(x + 10, y - 8);
        casa.lineTo(x + 10, y);
        casa.closePath();
        g2d.setPaint(cor);
        g2d.fill(casa);
        g2d.setPaint(Color.BLACK);
        g2d.draw(casa);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.scale(Escala.FATOR, Escala.FATOR);
        g2d.drawImage(img, 0, 0, LARGURA, ALTURA, null);

        desenharCidades(g2d);
        desenharTributos(g2d);
        desenharYurtsDasParadas(g2d);
        desenharConselheiros(g2d);
        desenharFaixaDeMelhorias(g2d);
        desenharPeoes(g2d);
        desenharKhan(g2d);
    }

    private void desenharCidades(Graphics2D g2d) {
        for (String[] cidade : cidades) {
            Point centro = mapa.posicaoDaCidade(cidade[0]);
            double cx = telaX(centro.x);
            double cy = telaY(centro.y);
            if ("OCULTA".equals(cidade[1])) {
                Ellipse2D cobertura = new Ellipse2D.Double();
                cobertura.setFrameFromCenter(cx, cy, cx + 48, cy + 48);
                g2d.setPaint(new Color(40, 40, 40, 190));
                g2d.fill(cobertura);
                g2d.setPaint(Color.WHITE);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 22));
                g2d.drawString("?", (int) cx - 6, (int) cy + 8);
            } else if ("CONQUISTADA".equals(cidade[1])) {
                desenharYurt(g2d, centro.x, centro.y + 12, Cores.converter(cidade[2]));
            } else {
                String[] tesouros = cidade[3].isEmpty() ? new String[0] : cidade[3].split(",");
                for (int k = 0; k < tesouros.length; k++) {
                    Point p = posicaoDoTesouro(centro, k);
                    desenharPecaRedonda(g2d, Imagens.tesouro(tesouros[k]), p.x, p.y, TAM_TESOURO);
                }
            }
        }
    }

    private void desenharTributos(Graphics2D g2d) {
        for (Map.Entry<String, int[]> e : provincias.entrySet()) {
            Point centro = mapa.posicaoDaProvincia(e.getKey());
            int[] tipos = tiposPresentes(e.getValue());
            for (int k = 0; k < tipos.length; k++) {
                double cx = centro.x + (k - (tipos.length - 1) / 2.0) * (TAM_TRIBUTO + 4);
                desenharPecaRedonda(g2d, Imagens.tributo(tipos[k]), cx, centro.y, TAM_TRIBUTO);
            }
        }
    }

    private void desenharYurtsDasParadas(Graphics2D g2d) {
        for (Map.Entry<String, String[]> e : yurts.entrySet()) {
            String[] donos = e.getValue();
            for (int k = 0; k < donos.length; k++) {
                Point p = mapa.posicaoDoPiao(e.getKey(), k < 1 ? 0 : 1);
                if (p == null) {
                    continue;
                }
                desenharYurt(g2d, p.x - 18, p.y - 6, Cores.converter(donos[k]));
            }
        }
    }

    private void desenharConselheiros(Graphics2D g2d) {
        for (String[] linha : bonusConselheiros) {
            if (linha[1].isEmpty()) {
                continue;
            }
            Point p = mapa.posicaoDoConselheiro(linha[0]);
            desenharImagem(g2d, Imagens.bonus(linha[1]), p.x, p.y, TAM_BONUS, TAM_BONUS);
        }
    }

    private void desenharFaixaDeMelhorias(Graphics2D g2d) {
        for (int i = 0; i < faixa.length; i++) {
            Point c = mapa.centroDaFaixa(i);
            if (!faixa[i].isEmpty()) {
                desenharImagem(g2d, Imagens.melhoria(faixa[i]), c.x, c.y, MapaVisual.LARG_FAIXA, MapaVisual.ALT_FAIXA);
            }
            if (Fachada.ESCOLHA_MELHORIA_GRATIS.equals(escolha) && !faixa[i].isEmpty()) {
                g2d.setPaint(Color.YELLOW);
                g2d.setStroke(new BasicStroke(3));
                g2d.draw(new Rectangle2D.Double(telaX(c.x - MapaVisual.LARG_FAIXA / 2.0),
                        telaY(c.y - MapaVisual.ALT_FAIXA / 2.0), telaX(MapaVisual.LARG_FAIXA),
                        telaY(MapaVisual.ALT_FAIXA)));
                g2d.setStroke(new BasicStroke(1));
            }
        }
        g2d.setPaint(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
        g2d.drawString("pilha: " + pilha, (int) telaX(560), (int) telaY(1398));
    }

    private void desenharPeoes(Graphics2D g2d) {
        for (int i = 0; i < cores.length; i++) {
            int indice = 0;
            for (int j = 0; j < i; j++) {
                if (paradas[j].equals(paradas[i])) {
                    indice++;
                }
            }
            Point p = mapa.posicaoDoPiao(paradas[i], indice);
            double cX = telaX(p.x);
            double cY = telaY(p.y);
            Ellipse2D circ = new Ellipse2D.Double();
            circ.setFrameFromCenter(cX, cY, cX + DIAM_PEAO / 2, cY + DIAM_PEAO / 2);
            g2d.setPaint(Cores.converter(cores[i]));
            g2d.fill(circ);
            g2d.setPaint(Color.BLACK);
            g2d.draw(circ);
        }
    }

    private void desenharKhan(Graphics2D g2d) {
        String[] locais = mapa.locaisDoKhan();
        if (khanPendente) {
            g2d.setPaint(Color.RED);
            g2d.setStroke(new BasicStroke(4));
            for (String local : locais) {
                if (local.equals(khan)) {
                    continue;
                }
                Point p = mapa.posicaoDoLocalDoKhan(local);
                Ellipse2D anel = new Ellipse2D.Double();
                anel.setFrameFromCenter(telaX(p.x), telaY(p.y), telaX(p.x) + 26, telaY(p.y) + 26);
                g2d.draw(anel);
            }
            g2d.setStroke(new BasicStroke(1));
        }
        Point p = khan == null ? MapaVisual.FORA_DO_TABULEIRO : mapa.posicaoDoLocalDoKhan(khan);
        double cx = telaX(p.x);
        double cy = telaY(p.y);
        Ellipse2D disco = new Ellipse2D.Double();
        disco.setFrameFromCenter(cx, cy, cx + 15, cy + 15);
        g2d.setPaint(Color.BLACK);
        g2d.fill(disco);
        g2d.setPaint(Color.WHITE);
        g2d.draw(disco);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2d.drawString("K", (int) cx - 5, (int) cy + 6);
    }
}
