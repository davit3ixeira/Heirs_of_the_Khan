package model;

import java.io.File;

import javax.swing.SwingUtilities;

import view.MenuPrincipal;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MenuPrincipal.AcoesMenu acoes =
                        new MenuPrincipal.AcoesMenu() {
                    @Override
                    public boolean iniciarNovaPartida() {
                        /*
                         * Etapa seguinte: o Controller deverá abrir
                         * a tela de seleção de personagens.
                         */
                        return false;
                    }

                    @Override
                    public boolean continuarPartida(File arquivo) {
                        /*
                         * Etapa seguinte: o Controller deverá carregar
                         * o estado salvo e abrir o tabuleiro.
                         */
                        return false;
                    }
                };

                MenuPrincipal menu = new MenuPrincipal(acoes);
                menu.setVisible(true);
            }
        });
    }
}