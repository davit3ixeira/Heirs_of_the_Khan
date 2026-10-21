package view;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/** Carrega as imagens dos elementos de jogo uma única vez. */
class Imagens {
    private static final String PASTA = "assets";

    private final Image tabuleiro;
    private final Image[] dados = new Image[7];
    private final Map<String, Image> pioes = new HashMap<>();

    Imagens() {
        tabuleiro = carregar("tabuleiro.jpg");
        for (int face = 1; face <= 6; face++) {
            dados[face] = carregar("dados/dado" + face + ".png");
        }
    }

    Image getTabuleiro() {
        return tabuleiro;
    }

    Image getDado(int face) {
        return dados[face];
    }

    Image getPiao(String cor) {
        Image piao = pioes.get(cor);
        if (piao == null) {
            piao = carregar("pioes/piao_" + cor + ".png");
            pioes.put(cor, piao);
        }
        return piao;
    }

    private static Image carregar(String caminho) {
        try {
            return ImageIO.read(new File(PASTA, caminho));
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível carregar " + caminho, e);
        }
    }
}
