package controller;

import java.awt.event.*;

public class TratadorAlternarCartela implements ActionListener {
    ControladorJogo controlador;

    public TratadorAlternarCartela(ControladorJogo c) {
        controlador = c;
    }

    public void actionPerformed(ActionEvent e) {
        controlador.alternarCartela();
    }
}
