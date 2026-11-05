package view;

import javax.swing.*;

import model.Fachada;
import model.Observado;
import model.Observador;

public class JanelaCartas extends JFrame implements Observador {
    PainelCartas pc = new PainelCartas();

    public JanelaCartas() {
        super("Cartas");
        setBounds(100, 100, PainelCartas.LARGURA, PainelCartas.ALTURA);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        getContentPane().add(pc);
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
