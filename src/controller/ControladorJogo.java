package controller;

import model.Fachada;
import view.JanelaCartas;
import view.JanelaEscolha;
import view.JanelaInicial;
import view.JanelaJogo;

public class ControladorJogo {
    Fachada fachada = Fachada.getInstance();
    JanelaInicial janelaInicial;
    JanelaEscolha janelaEscolha;
    JanelaJogo janela;
    JanelaCartas janelaCartas;

    public ControladorJogo() {
        janelaInicial = new JanelaInicial();
        janelaInicial.getBotaoNova().addActionListener(new TratadorNovaPartida(this));
    }

    public void iniciar() {
        janelaInicial.setVisible(true);
    }

    void novaPartida() {
        janelaInicial.dispose();
        janelaEscolha = new JanelaEscolha();
        janelaEscolha.getBotaoIniciar().addActionListener(new TratadorIniciarPartida(this));
        janelaEscolha.setVisible(true);
    }

    void iniciarPartida() {
        String[] cores = janelaEscolha.getCoresEscolhidas();
        if (cores.length < 2)
            return;
        int[] tabuleiros = janelaEscolha.getTabuleirosEscolhidos();
        janelaEscolha.dispose();

        janela = new JanelaJogo("Herdeiros do Khan");
        janelaCartas = new JanelaCartas();
        janela.getPainelTabuleiro().addMouseListener(new TratadorCliqueTabuleiro(this));
        janela.getPainelJogador().addMouseListener(new TratadorCliqueJogador(this));
        janela.getBotaoEncerrar().addActionListener(new TratadorEncerrarTurno(this));
        janela.getBotaoCartela().addActionListener(new TratadorAlternarCartela(this));
        janela.getBotaoCartas().addActionListener(new TratadorAlternarCartas(this));
        janelaCartas.addWindowListener(new TratadorFecharCartas(this));
        janelaCartas.getPainelCartas().addMouseListener(new TratadorCliqueCartas(this));

        fachada.adicionarObservador(janela);
        fachada.adicionarObservador(janela.getPainelTabuleiro());
        fachada.adicionarObservador(janela.getPainelJogador());
        fachada.adicionarObservador(janelaCartas);
        janela.setVisible(true);
        fachada.iniciarPartida(cores, tabuleiros);
    }

    void paradaClicada(int x, int y) {
        String parada = janela.getPainelTabuleiro().paradaEm(x, y);
        if (parada != null) {
            fachada.moverJogadorDaVez(parada);
            return;
        }
        String provincia = janela.getPainelTabuleiro().provinciaEm(x, y);
        if (provincia != null)
            fachada.coletarTributo(provincia);
    }

    void colunaClicada(int x, int y) {
        int coluna = janela.getPainelJogador().colunaEm(x, y);
        if (coluna >= 0)
            fachada.ativarColuna(coluna);
    }

    void cartaClicada(int x, int y) {
        String carta = janelaCartas.getPainelCartas().cartaEm(x, y);
        if (carta != null)
            fachada.escolherCartaSecreta(carta);
    }

    void encerrarTurno() {
        fachada.encerrarTurno();
    }

    void alternarCartela() {
        fachada.alternarCartela();
    }

    void alternarCartas() {
        fachada.alternarCartas();
    }
}
