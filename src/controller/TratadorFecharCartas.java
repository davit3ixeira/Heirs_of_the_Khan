package controller;

import java.awt.event.*;

public class TratadorFecharCartas extends WindowAdapter {
    ControladorJogo controlador;

    public TratadorFecharCartas(ControladorJogo c) {
        controlador = c;
    }

    public void windowClosing(WindowEvent e) {
        controlador.alternarCartas();
    }
}
