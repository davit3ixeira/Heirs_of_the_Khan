package view;

import java.awt.*;
import javax.swing.*;

public class JanelaInicial extends JFrame {
    JButton bNova = new JButton("Nova partida");
    JButton bContinuar = new JButton("Continuar partida");

    public JanelaInicial() {
        super("Herdeiros do Khan");
        setSize(320, 160);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        bContinuar.setEnabled(false);
        Container c = getContentPane();
        c.setLayout(new GridLayout(2, 1, 10, 10));
        c.add(bNova);
        c.add(bContinuar);
    }

    public JButton getBotaoNova() {
        return bNova;
    }
}
