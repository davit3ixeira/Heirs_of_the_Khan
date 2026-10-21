package controller;

import java.awt.event.*;

public class TratadorCliqueTabuleiro implements MouseListener {
    ControladorJogo controlador;

    public TratadorCliqueTabuleiro(ControladorJogo c) {
        controlador = c;
    }

    public void mouseClicked(MouseEvent e) {
        controlador.paradaClicada(e.getX(), e.getY());
    }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }
}
