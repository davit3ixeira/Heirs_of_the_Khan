package view;

import java.awt.*;
import javax.swing.*;

import model.Fachada;
import model.Observado;
import model.Observador;

public class PainelAcoes extends JPanel implements Observador {
    public static final int LARGURA = 420;
    public static final int ALTURA = 520;

    static final int TAM_ICONE = 40;
    static final int TAM_BONUS = 52;
    static final int Y_TRIBUTOS = 66;
    static final int Y_TESOUROS = 116;
    static final int Y_BONUS = 172;
    static final int Y_ESCOLHA = 250;

    String[] cores = new String[0];
    int[] recursos = new int[6];
    String[] tesourosVirtuais = new String[0];
    int[] tributos = new int[3];
    int[] tesouros = new int[5];
    String[] bonus = new String[0];
    String escolha = null;
    String[] opcoesTesouro = new String[0];
    int[] votos = new int[0];
    int[] conselho = new int[2];
    boolean fim = false;
    String[] placar = new String[0];
    String mensagem = "";
    String vez = "";
    boolean khanPendente = false;
    boolean colunaAtivada = false;

    public void notificar(Observado o) {
        cores = (String[]) o.get(Fachada.CORES);
        recursos = (int[]) o.get(Fachada.RECURSOS);
        tesourosVirtuais = (String[]) o.get(Fachada.TESOUROS_VIRTUAIS);
        tributos = (int[]) o.get(Fachada.TRIBUTOS_JOGADOR);
        tesouros = (int[]) o.get(Fachada.TESOUROS_JOGADOR);
        bonus = (String[]) o.get(Fachada.BONUS_JOGADOR);
        escolha = (String) o.get(Fachada.ESCOLHA);
        opcoesTesouro = (String[]) o.get(Fachada.OPCOES_TESOURO);
        votos = (int[]) o.get(Fachada.VOTOS);
        conselho = (int[]) o.get(Fachada.CONSELHO);
        fim = (Boolean) o.get(Fachada.FIM);
        placar = (String[]) o.get(Fachada.PLACAR);
        mensagem = (String) o.get(Fachada.MENSAGEM);
        vez = (String) o.get(Fachada.VEZ);
        khanPendente = (Boolean) o.get(Fachada.KHAN_PENDENTE);
        colunaAtivada = (Boolean) o.get(Fachada.COLUNA_ATIVADA);
        repaint();
    }

    public int bonusEm(int telaX, int telaY) {
        int x = (int) Escala.doProjeto(telaX);
        int y = (int) Escala.doProjeto(telaY);
        for (int i = 0; i < bonus.length; i++) {
            int bx = 10 + i * (TAM_BONUS + 6);
            if (x >= bx && x <= bx + TAM_BONUS && y >= Y_BONUS && y <= Y_BONUS + TAM_BONUS) {
                return i;
            }
        }
        return -1;
    }

    public int opcaoDeTesouroEm(int telaX, int telaY) {
        int x = (int) Escala.doProjeto(telaX);
        int y = (int) Escala.doProjeto(telaY);
        if (!Fachada.ESCOLHA_TESOURO_BONUS.equals(escolha)) {
            return -1;
        }
        for (int i = 0; i < opcoesTesouro.length; i++) {
            int bx = 10 + i * (TAM_BONUS + 10);
            if (x >= bx && x <= bx + TAM_BONUS && y >= Y_ESCOLHA + 22 && y <= Y_ESCOLHA + 22 + TAM_BONUS) {
                return i;
            }
        }
        return -1;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.scale(Escala.FATOR, Escala.FATOR);
        if (fim) {
            desenharPlacar(g2d);
            return;
        }
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g2d.setPaint(Color.BLACK);
        g2d.drawString("Movimentos: " + recursos[0] + "   Pegar tributos: " + recursos[1]
                + "   Yurts na reserva: " + recursos[5], 10, 18);
        StringBuilder virtuais = new StringBuilder("Virtuais (só neste turno) - espada: " + recursos[2]
                + "  yurt: " + recursos[3] + "  moeda: " + recursos[4]);
        for (String t : tesourosVirtuais) {
            virtuais.append("  ").append(t.toLowerCase());
        }
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2d.drawString(virtuais.toString(), 10, 36);
        if (khanPendente) {
            g2d.setPaint(Color.RED);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2d.drawString("Coloque o Khan num dos anéis vermelhos do tabuleiro.", 10, 54);
        } else if (!colunaAtivada) {
            g2d.setPaint(Color.BLUE);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2d.drawString("Escolha uma coluna do seu tabuleiro.", 10, 54);
        }

        g2d.setPaint(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        for (int t = 0; t < tributos.length; t++) {
            int x = 10 + t * 100;
            Desenho.imagemRedonda(g2d, Imagens.tributo(t), x, Y_TRIBUTOS, TAM_ICONE, TAM_ICONE);
            g2d.drawString("x" + tributos[t], x + TAM_ICONE + 6, Y_TRIBUTOS + 26);
        }
        for (int t = 0; t < tesouros.length; t++) {
            int x = 10 + t * 80;
            Desenho.imagemRedonda(g2d, Imagens.tesouro(Imagens.TESOUROS[t]), x, Y_TESOUROS, TAM_ICONE, TAM_ICONE);
            g2d.drawString("x" + tesouros[t], x + TAM_ICONE + 4, Y_TESOUROS + 26);
        }

        g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g2d.drawString("Bônus guardados (clique para usar):", 10, Y_BONUS - 6);
        for (int i = 0; i < bonus.length; i++) {
            g2d.drawImage(Imagens.bonus(bonus[i]), 10 + i * (TAM_BONUS + 6), Y_BONUS, TAM_BONUS, TAM_BONUS, null);
        }

        desenharEscolha(g2d);
        desenharVotos(g2d);

        g2d.setPaint(Color.DARK_GRAY);
        g2d.setFont(new Font("SansSerif", Font.ITALIC, 12));
        g2d.drawString(mensagem, 10, ALTURA - 68);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
        String[] ajuda = {
            "Parada: clique move; botão direito constrói yurt (parada visitada).",
            "Tributo da província: clique pega. Tesouro de cidade: clique ataca.",
            "Peça de bônus no tabuleiro: clique entrega tesouros ao conselheiro.",
            "Melhoria da faixa: clique compra (2 moedas)."
        };
        for (int i = 0; i < ajuda.length; i++) {
            g2d.drawString(ajuda[i], 10, ALTURA - 56 + i * 15);
        }
    }

    private void desenharEscolha(Graphics2D g2d) {
        if (escolha == null) {
            return;
        }
        g2d.setPaint(Color.RED);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 13));
        if (Fachada.ESCOLHA_TESOURO_BONUS.equals(escolha)) {
            g2d.drawString("Escolha 1 tesouro para manter:", 10, Y_ESCOLHA + 14);
            for (int i = 0; i < opcoesTesouro.length; i++) {
                Desenho.imagemRedonda(g2d, Imagens.tesouro(opcoesTesouro[i]), 10 + i * (TAM_BONUS + 10),
                        Y_ESCOLHA + 22, TAM_BONUS, TAM_BONUS);
            }
        } else {
            g2d.drawString("Linha completa! Escolha uma melhoria gratuita", 10, Y_ESCOLHA + 14);
            g2d.drawString("na faixa do tabuleiro ou use \"Comprar da pilha\".", 10, Y_ESCOLHA + 30);
        }
    }

    private void desenharVotos(Graphics2D g2d) {
        int y = 340;
        g2d.setPaint(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2d.drawString("Votos no Kurultai", 10, y);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 14));
        for (int i = 0; i < cores.length && i < votos.length; i++) {
            g2d.setPaint(Cores.converter(cores[i]));
            g2d.fillRect(10, y + 8 + i * 18, 13, 13);
            g2d.setPaint(Color.BLACK);
            g2d.drawRect(10, y + 8 + i * 18, 13, 13);
            String marca = cores[i].equals(vez) ? "  <-- vez" : "";
            g2d.drawString(cores[i] + ": " + votos[i] + marca, 30, y + 19 + i * 18);
        }
        g2d.drawString("Ficha do conselho: " + conselho[0] + " / " + conselho[1], 220, y + 19);
    }

    private void desenharPlacar(Graphics2D g2d) {
        g2d.setPaint(Color.BLACK);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2d.drawString("Fim de jogo", 10, 24);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
        for (int i = 0; i < placar.length; i++) {
            g2d.drawString(placar[i], 10, 52 + i * 22);
        }
    }
}
