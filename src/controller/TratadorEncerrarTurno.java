package controller;

import java.awt.event.*;

public class TratadorEncerrarTurno implements ActionListener {
    ControladorJogo controlador;

    public TratadorEncerrarTurno(ControladorJogo c) {
        controlador = c;
    }

    public void actionPerformed(ActionEvent e) {
        controlador.encerrarTurno();
    }
}
