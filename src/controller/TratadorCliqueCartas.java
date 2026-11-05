package controller;

import java.awt.event.*;

public class TratadorCliqueCartas implements MouseListener {
    ControladorJogo controlador;

    public TratadorCliqueCartas(ControladorJogo c) {
        controlador = c;
    }

    public void mouseClicked(MouseEvent e) {
        controlador.cartaClicada(e.getX(), e.getY());
    }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }
}
