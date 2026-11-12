package view;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

class Imagens {
    private static final Map<String, Image> CACHE = new HashMap<>();

    static final String[] TIPOS_TRIBUTO = {"ESPADA", "YURT", "MOEDA"};
    private static final String[] ARQ_TRIBUTO = {"cartoes6-2", "cartoes6-1", "cartoes6-3"};

    static final String[] TESOUROS = {"BOI", "SEDA", "FERRO", "PELE", "ESPECIARIA"};
    private static final String[] ARQ_TESOURO = {"cartoes7-1", "cartoes7-2", "cartoes7-3",
        "cartoes7-4", "cartoes7-5"};

    private static final String[] BONUS = {"VOTO", "ESPADAS", "MOEDAS", "YURTS", "MOVIMENTOS",
        "MAOS", "TESOUROS"};
    private static final String[] ARQ_BONUS = {"cartoes3-2", "cartoes3-4", "cartoes3-7",
        "cartoes3-3", "cartoes3-1", "cartoes3-6", "cartoes3-5"};

    private Imagens() {
    }

    private static Image obter(String arquivo) {
        Image imagem = CACHE.get(arquivo);
        if (imagem == null) {
            imagem = JanelaJogo.carregar(arquivo);
            CACHE.put(arquivo, imagem);
        }
        return imagem;
    }

    private static int indice(String[] chaves, String chave) {
        for (int i = 0; i < chaves.length; i++) {
            if (chaves[i].equals(chave)) {
                return i;
            }
        }
        return -1;
    }

    static Image tributo(int tipo) {
        return obter("assets/cartoes/" + ARQ_TRIBUTO[tipo] + ".jpg");
    }

    static Image tesouro(String nome) {
        return obter("assets/cartoes/" + ARQ_TESOURO[indice(TESOUROS, nome)] + ".jpg");
    }

    static Image bonus(String nome) {
        return obter("assets/cartoes/" + ARQ_BONUS[indice(BONUS, nome)] + ".jpg");
    }

    static Image melhoria(String id) {
        return obter("assets/cartoes/" + id + ".jpg");
    }
}
