package controller;

import java.awt.event.*;

public class TratadorContinuarPartida implements ActionListener {
    ControladorJogo controlador;

    public TratadorContinuarPartida(ControladorJogo c) {
        controlador = c;
    }

    public void actionPerformed(ActionEvent e) {
        controlador.continuarPartida();
    }
}
