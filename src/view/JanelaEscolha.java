package view;

import java.awt.*;
import javax.swing.*;

public class JanelaEscolha extends JFrame {
    static final String[] HERDEIROS = {"Altani", "Jochi", "Chagatai", "Tolui", "Ogedei"};
    static final String[] CORES = {"vermelho", "azul", "verde", "amarelo", "preto"};

    JCheckBox[] escolhas = new JCheckBox[HERDEIROS.length];
    JButton bIniciar = new JButton("Iniciar partida");

    public JanelaEscolha() {
        super("Escolha dos personagens");
        setSize(320, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        Container c = getContentPane();
        c.setLayout(new GridLayout(HERDEIROS.length + 1, 1, 5, 5));
        for (int i = 0; i < HERDEIROS.length; i++) {
            escolhas[i] = new JCheckBox(HERDEIROS[i] + " (" + CORES[i] + ")");
            escolhas[i].setForeground(Cores.converter(CORES[i]));
            c.add(escolhas[i]);
        }
        c.add(bIniciar);
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
}
