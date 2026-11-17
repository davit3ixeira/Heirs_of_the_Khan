package controller;

import java.awt.event.*;

public class TratadorPoder implements ActionListener {
    ControladorJogo controlador;

    public TratadorPoder(ControladorJogo c) {
        controlador = c;
    }

    public void actionPerformed(ActionEvent e) {
        controlador.usarPoder();
    }
}
