package controller;

import java.util.Arrays;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ControladorJogo(Arrays.asList("vermelho", "azul", "verde", "amarelo", "preto"))
                        .iniciar();
            }
        });
    }
}
