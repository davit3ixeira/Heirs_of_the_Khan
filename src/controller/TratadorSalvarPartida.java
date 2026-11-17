package controller;

import java.awt.event.*;

public class TratadorSalvarPartida implements ActionListener {
    ControladorJogo controlador;

    public TratadorSalvarPartida(ControladorJogo c) {
        controlador = c;
    }

    public void actionPerformed(ActionEvent e) {
        controlador.salvarPartida();
    }
}
