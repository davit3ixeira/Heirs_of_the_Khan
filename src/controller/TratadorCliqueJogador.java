package controller;

import java.awt.event.*;

public class TratadorCliqueJogador implements MouseListener {
    ControladorJogo controlador;

    public TratadorCliqueJogador(ControladorJogo c) {
        controlador = c;
    }

    public void mouseClicked(MouseEvent e) {
        controlador.colunaClicada(e.getX(), e.getY());
    }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }
}
