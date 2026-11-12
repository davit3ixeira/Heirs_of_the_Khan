package controller;

import model.Fachada;
import view.JanelaCartas;
import view.JanelaEscolha;
import view.JanelaInicial;
import view.JanelaJogo;
import view.PainelAcoes;
import view.PainelTabuleiro;

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
        janela.getPainelAcoes().addMouseListener(new TratadorCliqueAcoes(this));
        janela.getBotaoEncerrar().addActionListener(new TratadorEncerrarTurno(this));
        janela.getBotaoPilha().addActionListener(new TratadorComprarPilha(this));
        janela.getBotaoCartela().addActionListener(new TratadorAlternarCartela(this));
        janela.getBotaoCartas().addActionListener(new TratadorAlternarCartas(this));
        janelaCartas.addWindowListener(new TratadorFecharCartas(this));
        janelaCartas.getPainelCartas().addMouseListener(new TratadorCliqueCartas(this));

        fachada.adicionarObservador(janela);
        fachada.adicionarObservador(janela.getPainelTabuleiro());
        fachada.adicionarObservador(janela.getPainelJogador());
        fachada.adicionarObservador(janela.getPainelAcoes());
        fachada.adicionarObservador(janelaCartas);
        janela.setVisible(true);
        fachada.iniciarPartida(cores, tabuleiros);
    }

    void tabuleiroClicado(int x, int y, boolean botaoDireito) {
        PainelTabuleiro tabuleiro = janela.getPainelTabuleiro();
        if (botaoDireito) {
            String parada = tabuleiro.paradaEm(x, y);
            if (parada != null)
                fachada.construirYurt(parada);
            return;
        }
        int faixa = tabuleiro.faixaEm(x, y);
        if (faixa >= 0) {
            if (Fachada.ESCOLHA_MELHORIA_GRATIS.equals(fachada.get(Fachada.ESCOLHA)))
                fachada.escolherMelhoriaGratis(faixa);
            else
                fachada.comprarMelhoria(faixa);
            return;
        }
        if ((Boolean) fachada.get(Fachada.KHAN_PENDENTE)) {
            String local = tabuleiro.localDoKhanEm(x, y);
            if (local != null) {
                fachada.colocarKhan(local);
                return;
            }
        }
        Object[] cidade = tabuleiro.tesouroDeCidadeEm(x, y);
        if (cidade != null) {
            fachada.atacarCidade((String) cidade[0], (Integer) cidade[1]);
            return;
        }
        String[] tributo = tabuleiro.tributoEm(x, y);
        if (tributo != null) {
            fachada.coletarTributo(tributo[0], tributo[1]);
            return;
        }
        String conselheiro = tabuleiro.conselheiroEm(x, y);
        if (conselheiro != null) {
            fachada.entregarTesouros(conselheiro);
            return;
        }
        String parada = tabuleiro.paradaEm(x, y);
        if (parada != null)
            fachada.moverJogadorDaVez(parada);
    }

    void acoesClicado(int x, int y) {
        PainelAcoes acoes = janela.getPainelAcoes();
        int opcao = acoes.opcaoDeTesouroEm(x, y);
        if (opcao >= 0) {
            fachada.escolherTesouroBonus(opcao);
            return;
        }
        int bonus = acoes.bonusEm(x, y);
        if (bonus >= 0)
            fachada.usarBonus(bonus);
    }

    void comprarDaPilha() {
        if (Fachada.ESCOLHA_MELHORIA_GRATIS.equals(fachada.get(Fachada.ESCOLHA)))
            fachada.escolherMelhoriaGratis(-1);
        else
            fachada.comprarMelhoria(-1);
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
