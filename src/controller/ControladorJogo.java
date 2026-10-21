package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.Jogo;
import view.JanelaTabuleiro;
import view.OuvinteTabuleiro;

public class ControladorJogo {
    private final Jogo jogo;
    private final JanelaTabuleiro janela;

    public ControladorJogo(List<String> cores) {
        jogo = Jogo.novaPartida(cores);
        janela = new JanelaTabuleiro();
        janela.adicionarOuvinteJogarDados(new TratadorJogarDados());
        janela.adicionarOuvinteEscolherDados(new TratadorEscolherDados());
        janela.adicionarOuvinteTabuleiro(new TratadorCliqueTabuleiro());
        atualizarJanela();
    }

    public void iniciar() {
        janela.setVisible(true);
    }

    private void atualizarJanela() {
        Map<String, String> paradaPorCor = new LinkedHashMap<>();
        for (String cor : jogo.getCores()) {
            paradaPorCor.put(cor, jogo.paradaDe(cor));
        }
        janela.atualizar(paradaPorCor, jogo.getJogadorDaVez(),
                jogo.getDado(0), jogo.getDado(1), jogo.isDadosLancados());
    }

    private class TratadorJogarDados implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (jogo.lancarDados()) {
                atualizarJanela();
            }
        }
    }

    private class TratadorEscolherDados implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (jogo.definirDados(janela.getDadoEscolhido1(), janela.getDadoEscolhido2())) {
                atualizarJanela();
            }
        }
    }

    private class TratadorCliqueTabuleiro implements OuvinteTabuleiro {
        @Override
        public void paradaClicada(String paradaId) {
            if (jogo.moverJogadorDaVez(paradaId)) {
                atualizarJanela();
            }
        }
    }
}
