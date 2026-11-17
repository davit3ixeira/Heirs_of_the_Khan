package view;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import model.Fachada;
import model.Observado;
import model.Observador;

public class JanelaJogo extends JFrame implements Observador {
    public static final int LARG_CONTEUDO = 1384;
    public static final int ALT_CONTEUDO = 945;
    public static final int NUM_CARTELAS = 5;

    Image[] cartelas = new Image[NUM_CARTELAS];
    PainelTabuleiro pt;
    PainelJogador pj;
    PainelAcoes pa = new PainelAcoes();
    JLabel lVez = new JLabel();
    JButton bEncerrar = new JButton("Encerrar Turno");
    JButton bPilha = new JButton("Comprar da pilha");
    JButton bCartela = new JButton("Cartela");
    JButton bCartas = new JButton("Cartas");
    JButton bPoder = new JButton("Poder");
    JButton bSalvar = new JButton("Salvar jogo");
    JLabel lAviso = new JLabel(" ");

    public JanelaJogo(String s) {
        super(s);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        pt = new PainelTabuleiro(carregar("assets/tabuleiro.jpg"));
        for (int i = 0; i < NUM_CARTELAS; i++)
            cartelas[i] = Imagens.cartela(i + 1);
        pj = new PainelJogador(cartelas);

        Container c = getContentPane();
        c.setLayout(null);
        c.setPreferredSize(new Dimension(Escala.px(LARG_CONTEUDO), Escala.px(ALT_CONTEUDO)));
        definirLimites(pt, 0, 0, PainelTabuleiro.LARGURA, PainelTabuleiro.ALTURA);
        definirLimites(lVez, 950, 5, 420, 26);
        definirLimites(pj, 950, 34, PainelJogador.LARGURA, PainelJogador.ALTURA);
        definirLimites(pa, 950, 268, PainelAcoes.LARGURA, PainelAcoes.ALTURA);
        definirLimites(bEncerrar, 950, 812, 205, 30);
        definirLimites(bPilha, 1165, 812, 205, 30);
        definirLimites(bCartela, 950, 848, 205, 30);
        definirLimites(bCartas, 1165, 848, 205, 30);
        definirLimites(bPoder, 950, 884, 205, 30);
        definirLimites(bSalvar, 1165, 884, 205, 30);
        definirLimites(lAviso, 950, 918, 420, 22);
        c.add(pt);
        c.add(lVez);
        c.add(pj);
        c.add(pa);
        c.add(bEncerrar);
        c.add(bPilha);
        c.add(bCartela);
        c.add(bCartas);
        c.add(bPoder);
        c.add(bSalvar);
        c.add(lAviso);
        pack();
        centralizar();
    }

    /** Posiciona um componente usando as medidas do projeto, ajustadas pela escala. */
    private void definirLimites(Component componente, int x, int y, int largura, int altura) {
        componente.setBounds(Escala.px(x), Escala.px(y), Escala.px(largura), Escala.px(altura));
    }

    /** Centraliza a janela na área útil da tela, sem cobrir a barra de tarefas. */
    private void centralizar() {
        Rectangle area = Escala.areaUtil();
        if (area == null) {
            setLocationRelativeTo(null);
            return;
        }
        int x = area.x + (area.width - getWidth()) / 2;
        int y = area.y + (area.height - getHeight()) / 2;
        setLocation(Math.max(area.x, x), Math.max(area.y, y));
    }

    static Image carregar(String arquivo) {
        Image i = null;
        try {
            i = ImageIO.read(new File(arquivo));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return i;
    }

    public PainelTabuleiro getPainelTabuleiro() {
        return pt;
    }

    public PainelJogador getPainelJogador() {
        return pj;
    }

    public PainelAcoes getPainelAcoes() {
        return pa;
    }

    public JButton getBotaoEncerrar() {
        return bEncerrar;
    }

    public JButton getBotaoPilha() {
        return bPilha;
    }

    public JButton getBotaoCartela() {
        return bCartela;
    }

    public JButton getBotaoCartas() {
        return bCartas;
    }

    public JButton getBotaoPoder() {
        return bPoder;
    }

    public JButton getBotaoSalvar() {
        return bSalvar;
    }

    public void mostrarAviso(String texto, boolean erro) {
        lAviso.setForeground(erro ? Color.RED : new Color(0, 110, 0));
        lAviso.setText(texto);
    }

    public void notificar(Observado o) {
        String corDaVez = (String) o.get(Fachada.VEZ);
        boolean colunaAtivada = (Boolean) o.get(Fachada.COLUNA_ATIVADA);
        boolean khanPendente = (Boolean) o.get(Fachada.KHAN_PENDENTE);
        boolean fim = (Boolean) o.get(Fachada.FIM);
        boolean semEscolha = o.get(Fachada.ESCOLHA) == null;
        lVez.setText(fim ? "Fim de jogo" : "Jogador da vez: " + corDaVez);
        lVez.setForeground(Cores.converter(corDaVez));
        bEncerrar.setEnabled(colunaAtivada && !khanPendente && semEscolha && !fim);
        bPilha.setEnabled(colunaAtivada && !fim);
        pj.setVisible((Boolean) o.get(Fachada.CARTELA_VISIVEL));
        int numero = (Integer) o.get(Fachada.TABULEIRO);
        bPoder.setText(Poderes.ROTULOS[numero - 1]);
        bPoder.setToolTipText(Poderes.dica(numero));
        boolean poderPorBotao = numero == 1 || numero == 3;
        bPoder.setEnabled(poderPorBotao && (Boolean) o.get(Fachada.PODER_DISPONIVEL));
    }
}
