package view;

import java.awt.*;

class Cores {
    static Color converter(String cor) {
        if (cor.equals("vermelho"))
            return Color.RED;
        if (cor.equals("azul"))
            return Color.BLUE;
        if (cor.equals("verde"))
            return Color.GREEN;
        if (cor.equals("amarelo"))
            return Color.YELLOW;
        return Color.BLACK;
    }
}
