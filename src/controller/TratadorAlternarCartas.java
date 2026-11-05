package controller;

import java.awt.event.*;

public class TratadorAlternarCartas implements ActionListener {
    ControladorJogo controlador;

    public TratadorAlternarCartas(ControladorJogo c) {
        controlador = c;
    }

    public void actionPerformed(ActionEvent e) {
        controlador.alternarCartas();
    }
}
