package view;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

/**
 * Posições das paradas sobre a imagem do tabuleiro, num sistema de referência
 * de LARGURA x ALTURA. Paradas duplas têm dois círculos.
 */
class MapaVisual {
    static final double LARGURA = 1400;
    static final double ALTURA = 1406.6;
    static final String KARAKORUM = "KARAKORUM";

    private static final int RAIO = 27;
    private static final Rectangle AREA_KARAKORUM = new Rectangle(580, 505, 240, 235);

    private static final Object[][] CIRCULOS = {
        {"KIEV_N", 235, 120}, {"KIEV_O", 61, 260}, {"KIEV_L", 228, 258},
        {"MOSCOU_O", 413, 268}, {"MOSCOU_L", 613, 238}, {"MOSCOU_S", 523, 405},
        {"SARAI_N", 845, 110}, {"SARAI_O", 750, 325}, {"SARAI_L", 912, 285},
        {"RUSSIA_O", 411, 83, 447, 120}, {"RUSSIA_L", 1052, 240, 1105, 240},
        {"KAESONG_O", 1202, 315}, {"KAESONG_L", 1335, 428}, {"KAESONG_S", 1217, 516},
        {"PEQUIM_N", 1032, 605}, {"PEQUIM_O", 912, 773}, {"PEQUIM_L", 1047, 808},
        {"CANTAO_N", 1217, 975}, {"CANTAO_O", 1077, 1073}, {"CANTAO_S", 1240, 1160},
        {"CHINA_L", 1232, 725, 1232, 775}, {"CHINA_S", 860, 1073, 912, 1073},
        {"SAMARCANDA_N", 405, 575}, {"SAMARCANDA_O", 275, 679}, {"SAMARCANDA_L", 461, 748},
        {"BAGDA_N", 104, 716}, {"BAGDA_L", 218, 791}, {"BAGDA_S", 85, 921},
        {"CABUL_N", 615, 876}, {"CABUL_L", 710, 993}, {"CABUL_S", 585, 1078},
        {"PERSIA_N", 225, 405, 220, 457}, {"PERSIA_S", 322, 908, 378, 908},
    };

    private final Map<String, Point[]> circulos = new HashMap<>();

    MapaVisual() {
        for (Object[] def : CIRCULOS) {
            Point[] pontos = new Point[(def.length - 1) / 2];
            for (int i = 0; i < pontos.length; i++) {
                pontos[i] = new Point((Integer) def[1 + 2 * i], (Integer) def[2 + 2 * i]);
            }
            circulos.put((String) def[0], pontos);
        }
    }

    /** Parada sob o ponto (x, y) de referência, ou null. */
    String paradaEm(double x, double y) {
        if (AREA_KARAKORUM.contains(x, y)) {
            return KARAKORUM;
        }
        for (Map.Entry<String, Point[]> entrada : circulos.entrySet()) {
            for (Point p : entrada.getValue()) {
                if (p.distance(x, y) <= RAIO) {
                    return entrada.getKey();
                }
            }
        }
        return null;
    }

    /** Centro onde desenhar o i-ésimo pião de uma parada. */
    Point posicaoDoPiao(String paradaId, int indice) {
        if (KARAKORUM.equals(paradaId)) {
            return new Point(632 + indice * 34, 700);
        }
        Point[] pontos = circulos.get(paradaId);
        if (pontos == null) {
            return null;
        }
        Point base = pontos[Math.min(indice, pontos.length - 1)];
        int deslocamento = indice < pontos.length ? 0 : 10 * (indice - pontos.length + 1);
        return new Point(base.x + deslocamento, base.y - deslocamento);
    }
}
