package view;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;

    private final AcoesMenu acoes;
    private final JLabel mensagem;

    public MenuPrincipal(AcoesMenu acoes) {
        super("Herdeiros do Khan");

        if (acoes == null) {
            throw new IllegalArgumentException("As ações do menu não podem ser nulas.");
        }

        this.acoes = acoes;
        this.mensagem = new JLabel(" ", SwingConstants.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // O tamanho é fixo e fica abaixo do limite de 1400 x 1050 pixels.
        setSize(520, 340);
        setLocationRelativeTo(null);

        montarInterface();
    }

    private void montarInterface() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 20));
        painelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(30, 40, 25, 40));

        JLabel titulo = new JLabel(
                "Herdeiros do Khan",
                SwingConstants.CENTER
        );
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));

        JButton botaoNovaPartida = new JButton("Iniciar nova partida");
        JButton botaoContinuar = new JButton("Continuar partida");

        JPanel painelBotoes = new JPanel(new GridLayout(2, 1, 0, 12));
        painelBotoes.add(botaoNovaPartida);
        painelBotoes.add(botaoContinuar);

        painelPrincipal.add(titulo, BorderLayout.NORTH);
        painelPrincipal.add(painelBotoes, BorderLayout.CENTER);
        painelPrincipal.add(mensagem, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);

        botaoNovaPartida.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                iniciarNovaPartida();
            }
        });

        botaoContinuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                selecionarArquivoSalvo();
            }
        });
    }

    private void iniciarNovaPartida() {
        /*
         * O Controller deve abrir a tela de seleção de personagens.
         * Se conseguiu abrir a próxima tela, o menu é fechado.
         */
        if (acoes.iniciarNovaPartida()) {
            dispose();
        } else {
            mensagem.setText(
                    "A tela de seleção de personagens ainda não está conectada."
            );
        }
    }

    private void selecionarArquivoSalvo() {
        JFileChooser seletor = new JFileChooser();
        seletor.setDialogTitle("Selecione uma partida salva");

        int resultado = seletor.showOpenDialog(this);

        // Se o jogador cancelar, o menu continua aberto.
        if (resultado != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File arquivo = seletor.getSelectedFile();

        if (arquivo == null || !arquivo.isFile()) {
            mensagem.setText("Selecione um arquivo válido.");
            return;
        }

        /*
         * O Controller tentará carregar a partida e abrir o tabuleiro.
         * Se não conseguir, o menu permanece aberto.
         */
        if (acoes.continuarPartida(arquivo)) {
            dispose();
        } else {
            mensagem.setText("Não foi possível carregar essa partida.");
        }
    }

    /**
     * Contrato entre a janela e o Controller.
     * O Controller informa se conseguiu iniciar ou carregar a partida.
     */
    public interface AcoesMenu {
        boolean iniciarNovaPartida();

        boolean continuarPartida(File arquivo);
    }
}