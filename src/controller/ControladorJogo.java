package controller;

import java.util.List;

import model.Jogo;
import view.JanelaJogo;

public class ControladorJogo {
    Jogo jogo;
    JanelaJogo janela;

    public ControladorJogo(String[] cores) {
        jogo = new Jogo();
        for (int i = 0; i < cores.length; i++)
            jogo.adicionarJogador(cores[i]);
        jogo.iniciarTurno(jogo.getJogadorDaVez());

        janela = new JanelaJogo("Herdeiros do Khan");
        janela.getPainelTabuleiro().addMouseListener(new TratadorCliqueTabuleiro(this));
        janela.getPainelJogador().addMouseListener(new TratadorCliqueJogador(this));
        janela.getBotaoEncerrar().addActionListener(new TratadorEncerrarTurno(this));
        atualizarJanela();
    }

    public void iniciar() {
        janela.setVisible(true);
    }

    void paradaClicada(int x, int y) {
        String parada = janela.getPainelTabuleiro().paradaEm(x, y);
        if (parada != null && jogo.moverJogadorDaVez(parada))
            atualizarJanela();
    }

    void colunaClicada(int x, int y) {
        int coluna = janela.getPainelJogador().colunaEm(x, y);
        if (coluna >= 0 && jogo.ativarColuna(coluna))
            atualizarJanela();
    }

    void encerrarTurno() {
        if (jogo.encerrarTurno())
            atualizarJanela();
    }

    void atualizarJanela() {
        List<String> lc = jogo.getCores();
        String[] cores = new String[lc.size()];
        String[] paradas = new String[lc.size()];
        for (int i = 0; i < lc.size(); i++) {
            cores[i] = lc.get(i);
            paradas[i] = jogo.paradaDe(cores[i]);
        }
        String vez = jogo.getJogadorDaVez();
        boolean[] ocupadas = new boolean[4];
        for (int i = 0; i < ocupadas.length; i++)
            ocupadas[i] = jogo.colunaOcupada(vez, i);
        janela.atualizar(cores, paradas, vez, jogo.getNumeroDoTabuleiro(vez), ocupadas,
                jogo.getColunaAtivada() >= 0, jogo.getMovimentosRestantes());
    }
}
