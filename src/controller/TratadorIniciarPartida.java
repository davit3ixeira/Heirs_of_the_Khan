package controller;

import java.awt.event.*;

public class TratadorIniciarPartida implements ActionListener {
    ControladorJogo controlador;

    public TratadorIniciarPartida(ControladorJogo c) {
        controlador = c;
    }

    public void actionPerformed(ActionEvent e) {
        controlador.iniciarPartida();
    }
}
