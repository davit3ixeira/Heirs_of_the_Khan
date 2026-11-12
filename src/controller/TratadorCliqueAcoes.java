package controller;

import java.awt.event.*;

public class TratadorCliqueAcoes implements MouseListener {
    ControladorJogo controlador;

    public TratadorCliqueAcoes(ControladorJogo c) {
        controlador = c;
    }

    public void mouseClicked(MouseEvent e) {
        controlador.acoesClicado(e.getX(), e.getY());
    }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }
}
