package view;

import java.awt.*;
import javax.swing.*;

public class JanelaInicial extends JFrame {
    JButton bNova = new JButton("Nova partida");
    JButton bContinuar = new JButton("Continuar partida");
    JLabel lMensagem = new JLabel(" ", SwingConstants.CENTER);

    public JanelaInicial() {
        super("Herdeiros do Khan");
        setSize(320, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        lMensagem.setForeground(Color.RED);
        Container c = getContentPane();
        c.setLayout(new GridLayout(3, 1, 10, 10));
        c.add(bNova);
        c.add(bContinuar);
        c.add(lMensagem);
    }

    public JButton getBotaoNova() {
        return bNova;
    }

    public JButton getBotaoContinuar() {
        return bContinuar;
    }

    public void mostrarMensagem(String texto) {
        lMensagem.setText(texto);
    }
}
