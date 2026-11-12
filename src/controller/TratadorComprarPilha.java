package controller;

import java.awt.event.*;

public class TratadorComprarPilha implements ActionListener {
    ControladorJogo controlador;

    public TratadorComprarPilha(ControladorJogo c) {
        controlador = c;
    }

    public void actionPerformed(ActionEvent e) {
        controlador.comprarDaPilha();
    }
}
