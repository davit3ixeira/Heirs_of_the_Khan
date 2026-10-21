package view;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class JanelaJogo extends JFrame {
    public final int LARG_DEFAULT = 1400;
    public final int ALT_DEFAULT = 1000;
    public static final int NUM_CARTELAS = 5;

    Image[] cartelas = new Image[NUM_CARTELAS];
    PainelTabuleiro pt;
    PainelJogador pj = new PainelJogador();
    JLabel lVez = new JLabel();
    JLabel lMovimentos = new JLabel();
    JButton bEncerrar = new JButton("Encerrar Turno");

    public JanelaJogo(String s) {
        super(s);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int sl = screenSize.width;
        int sa = screenSize.height;
        int x = sl / 2 - LARG_DEFAULT / 2;
        int y = sa / 2 - ALT_DEFAULT / 2;
        setBounds(x, y, LARG_DEFAULT, ALT_DEFAULT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        pt = new PainelTabuleiro(carregar("assets/tabuleiro.jpg"));
        for (int i = 0; i < NUM_CARTELAS; i++)
            cartelas[i] = carregar("assets/cartelas/cartela" + (i + 1) + ".jpg");

        Container c = getContentPane();
        c.setLayout(null);
        pt.setBounds(0, 0, PainelTabuleiro.LARGURA, PainelTabuleiro.ALTURA);
        lVez.setBounds(950, 20, 420, 30);
        pj.setBounds(950, 60, PainelJogador.LARGURA, PainelJogador.ALTURA);
        lMovimentos.setBounds(950, 300, 420, 30);
        bEncerrar.setBounds(950, 340, 160, 30);
        c.add(pt);
        c.add(lVez);
        c.add(pj);
        c.add(lMovimentos);
        c.add(bEncerrar);
    }

    Image carregar(String arquivo) {
        Image i = null;
        try {
            i = ImageIO.read(new File(arquivo));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return i;
    }

    public PainelTabuleiro getPainelTabuleiro() {
        return pt;
    }

    public PainelJogador getPainelJogador() {
        return pj;
    }

    public JButton getBotaoEncerrar() {
        return bEncerrar;
    }

    public void atualizar(String[] cores, String[] paradas, String corDaVez, int numTabuleiro,
            boolean[] ocupadas, boolean colunaAtivada, int movimentos) {
        pt.setPeoes(cores, paradas);
        pj.setJogador(cartelas[numTabuleiro - 1], corDaVez, ocupadas);
        lVez.setText("Jogador da vez: " + corDaVez);
        lVez.setForeground(Cores.converter(corDaVez));
        if (colunaAtivada)
            lMovimentos.setText("Movimentos restantes: " + movimentos);
        else
            lMovimentos.setText("Escolha uma coluna do seu tabuleiro");
        bEncerrar.setEnabled(colunaAtivada);
    }
}
