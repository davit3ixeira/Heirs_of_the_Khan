package view;

import java.awt.Dimension;
import javax.swing.*;

import model.Fachada;
import model.Observado;
import model.Observador;

public class JanelaCartas extends JFrame implements Observador {
    PainelCartas pc = new PainelCartas();

    public JanelaCartas() {
        super("Cartas");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        pc.setPreferredSize(new Dimension(Escala.px(PainelCartas.LARGURA), Escala.px(PainelCartas.ALTURA)));
        getContentPane().add(pc);
        pack();
        setLocation(100, 100);
    }

    public PainelCartas getPainelCartas() {
        return pc;
    }

    public void notificar(Observado o) {
        pc.setCartas((String[]) o.get(Fachada.CARTAS_ABERTAS), (String) o.get(Fachada.CARTA_SECRETA),
                (String[]) o.get(Fachada.CARTAS_OFERECIDAS), (String) o.get(Fachada.ESCOLHENDO));
        setVisible((Boolean) o.get(Fachada.CARTAS_VISIVEIS));
    }
}
