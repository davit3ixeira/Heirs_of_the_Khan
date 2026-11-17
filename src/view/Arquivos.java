package view;

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class Arquivos {
    private static final String EXTENSAO = "khan";

    private Arquivos() {
    }

    private static JFileChooser criarSeletor() {
        JFileChooser seletor = new JFileChooser(new File(System.getProperty("user.dir")));
        seletor.setFileFilter(new FileNameExtensionFilter("Partida de Herdeiros do Khan (*." + EXTENSAO + ")", EXTENSAO));
        return seletor;
    }

    public static String escolherParaAbrir(Component pai) {
        JFileChooser seletor = criarSeletor();
        seletor.setDialogTitle("Continuar partida salva");
        if (seletor.showOpenDialog(pai) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        return seletor.getSelectedFile().getAbsolutePath();
    }

    public static String escolherParaSalvar(Component pai) {
        JFileChooser seletor = criarSeletor();
        seletor.setDialogTitle("Salvar partida");
        if (seletor.showSaveDialog(pai) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        String caminho = seletor.getSelectedFile().getAbsolutePath();
        if (!caminho.toLowerCase().endsWith("." + EXTENSAO)) {
            caminho = caminho + "." + EXTENSAO;
        }
        return caminho;
    }
}
