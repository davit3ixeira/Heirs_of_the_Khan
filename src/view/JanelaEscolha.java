package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JanelaEscolha extends JFrame {
    static final String[] HERDEIROS = Poderes.NOMES;
    static final String[] CORES = {"vermelho", "azul", "verde", "amarelo", "preto"};

    static final int LARG_FAIXA = 260;
    static final int ALT_FAIXA = 555;
    static final int MARGEM = 10;
    static final int ESPACO = 12;
    static final int Y_FAIXAS = 40;
    static final int LARG_CONTEUDO = 1368;
    static final int ALT_CONTEUDO = 690;
    static final double FRACAO_FAIXA = 0.255;

    final double fator = Escala.fatorPara(LARG_CONTEUDO + 16, ALT_CONTEUDO + 47);
    JCheckBox[] escolhas = new JCheckBox[HERDEIROS.length];
    JButton bIniciar = new JButton("Iniciar partida");
    JLabel lTitulo = new JLabel("Escolha de 2 a 5 herdeiros: clique na cartela ou marque a caixa.");
    PainelFaixas painel = new PainelFaixas();

    public JanelaEscolha() {
        super("Escolha dos personagens");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        Container c = getContentPane();
        c.setLayout(null);
        c.setPreferredSize(new Dimension(px(LARG_CONTEUDO), px(ALT_CONTEUDO)));

        lTitulo.setFont(lTitulo.getFont().deriveFont(Font.BOLD, 15f));
        definirLimites(lTitulo, MARGEM, 6, LARG_CONTEUDO - 2 * MARGEM, 28);
        definirLimites(painel, 0, Y_FAIXAS - 6, LARG_CONTEUDO, ALT_FAIXA + 12);
        painel.addMouseListener(new CliqueNaCartela());
        c.add(lTitulo);
        c.add(painel);

        Contador contador = new Contador();
        for (int i = 0; i < HERDEIROS.length; i++) {
            escolhas[i] = new JCheckBox(HERDEIROS[i] + " (" + CORES[i] + ")", false);
            escolhas[i].setHorizontalAlignment(SwingConstants.CENTER);
            escolhas[i].setOpaque(true);
            escolhas[i].setBackground(Cores.converter(CORES[i]));
            escolhas[i].setForeground(corDoTexto(Cores.converter(CORES[i])));
            escolhas[i].setFont(escolhas[i].getFont().deriveFont(Font.BOLD, 14f));
            escolhas[i].setToolTipText(Poderes.dica(i + 1));
            escolhas[i].addItemListener(contador);
            definirLimites(escolhas[i], xDaFaixa(i), Y_FAIXAS + ALT_FAIXA + 10, LARG_FAIXA, 30);
            c.add(escolhas[i]);
        }

        bIniciar.setEnabled(false);
        bIniciar.setFont(bIniciar.getFont().deriveFont(Font.BOLD, 15f));
        definirLimites(bIniciar, (LARG_CONTEUDO - 320) / 2, Y_FAIXAS + ALT_FAIXA + 52, 320, 38);
        c.add(bIniciar);

        pack();
        Escala.centralizar(this);
    }

    static int xDaFaixa(int indice) {
        return MARGEM + indice * (LARG_FAIXA + ESPACO);
    }

    private void definirLimites(Component componente, int x, int y, int largura, int altura) {
        componente.setBounds(px(x), px(y), px(largura), px(altura));
    }

    private int px(int valor) {
        return (int) Math.round(valor * fator);
    }

    private static Color corDoTexto(Color fundo) {
        double luminancia = 0.299 * fundo.getRed() + 0.587 * fundo.getGreen() + 0.114 * fundo.getBlue();
        return luminancia > 140 ? Color.BLACK : Color.WHITE;
    }

    public JButton getBotaoIniciar() {
        return bIniciar;
    }

    public String[] getCoresEscolhidas() {
        int n = 0;
        for (JCheckBox e : escolhas)
            if (e.isSelected())
                n++;
        String[] cores = new String[n];
        int k = 0;
        for (int i = 0; i < escolhas.length; i++)
            if (escolhas[i].isSelected())
                cores[k++] = CORES[i];
        return cores;
    }

    public int[] getTabuleirosEscolhidos() {
        int n = getCoresEscolhidas().length;
        int[] tabuleiros = new int[n];
        int k = 0;
        for (int i = 0; i < escolhas.length; i++)
            if (escolhas[i].isSelected())
                tabuleiros[k++] = i + 1;
        return tabuleiros;
    }

    class Contador implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            bIniciar.setEnabled(getCoresEscolhidas().length >= 2);
            painel.repaint();
        }
    }

    class CliqueNaCartela extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            double x = e.getX() / fator;
            double y = e.getY() / fator + Y_FAIXAS - 6;
            for (int i = 0; i < escolhas.length; i++) {
                int inicio = xDaFaixa(i);
                if (x >= inicio && x <= inicio + LARG_FAIXA && y >= Y_FAIXAS && y <= Y_FAIXAS + ALT_FAIXA)
                    escolhas[i].doClick();
            }
        }
    }

    class PainelFaixas extends JPanel {
        Image[] cartelas = new Image[HERDEIROS.length];

        PainelFaixas() {
            for (int i = 0; i < cartelas.length; i++)
                cartelas[i] = Imagens.cartela(i + 1);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.scale(fator, fator);
            for (int i = 0; i < cartelas.length; i++) {
                int x = xDaFaixa(i);
                int y = 6;
                int larguraOrigem = (int) (cartelas[i].getWidth(null) * FRACAO_FAIXA);
                g2d.drawImage(cartelas[i], x, y, x + LARG_FAIXA, y + ALT_FAIXA, 0, 0, larguraOrigem,
                        cartelas[i].getHeight(null), null);
                boolean marcada = escolhas[i] != null && escolhas[i].isSelected();
                if (marcada) {
                    g2d.setPaint(Cores.converter(CORES[i]));
                    g2d.setStroke(new BasicStroke(6));
                    g2d.drawRect(x - 3, y - 3, LARG_FAIXA + 6, ALT_FAIXA + 6);
                } else {
                    g2d.setPaint(Color.DARK_GRAY);
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRect(x, y, LARG_FAIXA, ALT_FAIXA);
                }
            }
        }
    }
}
