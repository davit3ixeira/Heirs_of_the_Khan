package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JanelaTabuleiro extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int LARGURA_MAXIMA = 1400;
    private static final int ALTURA_MAXIMA = 1050;
    private static final int ALTURA_TABULEIRO = 960;
    private static final Integer[] FACES = {1, 2, 3, 4, 5, 6};

    private final PainelTabuleiro painelTabuleiro;
    private final PainelDados painelDados;
    private final JLabel rotuloVez = new JLabel(" ");
    private final JLabel rotuloPassos = new JLabel(" ");
    private final JButton botaoJogarDados = new JButton("Jogar Dados");
    private final JButton botaoEscolherDados = new JButton("Escolher Dados");
    private final JComboBox<Integer> escolhaDado1 = new JComboBox<>(FACES);
    private final JComboBox<Integer> escolhaDado2 = new JComboBox<>(FACES);

    public JanelaTabuleiro() {
        super("Herdeiros de Khan - Jogo");
        Imagens imagens = new Imagens();
        painelTabuleiro = new PainelTabuleiro(imagens, ALTURA_TABULEIRO);
        painelDados = new PainelDados(imagens);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().add(painelTabuleiro, BorderLayout.CENTER);
        getContentPane().add(criarPainelLateral(), BorderLayout.EAST);
        pack();
        setSize(Math.min(getWidth(), LARGURA_MAXIMA), Math.min(getHeight(), ALTURA_MAXIMA));
        setLocationRelativeTo(null);
    }

    private JPanel criarPainelLateral() {
        JPanel lateral = new JPanel();
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));
        lateral.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        lateral.setPreferredSize(new Dimension(224, ALTURA_TABULEIRO));

        JLabel titulo = new JLabel("Jogador da vez");
        rotuloVez.setFont(rotuloVez.getFont().deriveFont(Font.BOLD, 16f));
        JPanel escolhas = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        escolhas.add(escolhaDado1);
        escolhas.add(escolhaDado2);
        escolhas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        Component[] componentes = {titulo, rotuloVez, painelDados, rotuloPassos,
            botaoJogarDados, escolhas, botaoEscolherDados};
        for (Component componente : componentes) {
            ((javax.swing.JComponent) componente).setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        botaoJogarDados.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        botaoEscolherDados.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        lateral.add(titulo);
        lateral.add(Box.createVerticalStrut(4));
        lateral.add(rotuloVez);
        lateral.add(Box.createVerticalStrut(10));
        lateral.add(painelDados);
        lateral.add(Box.createVerticalStrut(6));
        lateral.add(rotuloPassos);
        lateral.add(Box.createVerticalGlue());
        lateral.add(botaoJogarDados);
        lateral.add(Box.createVerticalStrut(18));
        lateral.add(escolhas);
        lateral.add(Box.createVerticalStrut(6));
        lateral.add(botaoEscolherDados);
        return lateral;
    }

    public void adicionarOuvinteTabuleiro(OuvinteTabuleiro ouvinte) {
        painelTabuleiro.adicionarOuvinte(ouvinte);
    }

    public void adicionarOuvinteJogarDados(ActionListener ouvinte) {
        botaoJogarDados.addActionListener(ouvinte);
    }

    public void adicionarOuvinteEscolherDados(ActionListener ouvinte) {
        botaoEscolherDados.addActionListener(ouvinte);
    }

    public int getDadoEscolhido1() {
        return (Integer) escolhaDado1.getSelectedItem();
    }

    public int getDadoEscolhido2() {
        return (Integer) escolhaDado2.getSelectedItem();
    }

    public void atualizar(Map<String, String> paradaPorCor, String corDaVez,
            int dado1, int dado2, boolean dadosLancados) {
        painelTabuleiro.atualizar(paradaPorCor);
        painelDados.atualizar(corDaVez, dado1, dado2, dadosLancados);
        rotuloVez.setText(corDaVez.substring(0, 1).toUpperCase() + corDaVez.substring(1));
        rotuloPassos.setText(dadosLancados ? "Até " + (dado1 + dado2) + " passo(s)" : " ");
        botaoJogarDados.setEnabled(!dadosLancados);
        botaoEscolherDados.setEnabled(!dadosLancados);
        escolhaDado1.setEnabled(!dadosLancados);
        escolhaDado2.setEnabled(!dadosLancados);
    }
}
