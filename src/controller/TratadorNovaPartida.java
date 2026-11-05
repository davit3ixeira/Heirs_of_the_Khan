package controller;

import java.awt.event.*;

public class TratadorNovaPartida implements ActionListener {
    ControladorJogo controlador;

    public TratadorNovaPartida(ControladorJogo c) {
        controlador = c;
    }

    public void actionPerformed(ActionEvent e) {
        controlador.novaPartida();
    }
}
