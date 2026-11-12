package view;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

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

    private static final Object[][] PROVINCIAS = {
        {"PROV_01", 310, 200}, {"PROV_02", 410, 170}, {"PROV_03", 385, 455},
        {"PROV_04", 605, 445}, {"PROV_05", 255, 585}, {"PROV_06", 945, 365},
        {"PROV_07", 1040, 505}, {"PROV_08", 290, 800}, {"PROV_09", 730, 780},
        {"PROV_10", 1150, 750}, {"PROV_11", 1000, 885}, {"PROV_12", 810, 925},
    };

    private static final int RAIO_PROVINCIA = 28;

    private static final Object[][] CIDADES = {
        {"KIEV", 147, 179}, {"MOSCOU", 519, 305}, {"SARAI", 840, 199},
        {"SAMARCANDA", 372, 667}, {"BAGDA", 128, 808}, {"CABUL", 609, 994},
        {"KAESONG", 1237, 391}, {"PEQUIM", 1000, 711}, {"CANTAO", 1173, 1064},
    };

    private static final Object[][] CONSELHEIROS = {
        {"RUSSIA_O", 660, 77}, {"RUSSIA_L", 1167, 122}, {"PERSIA_N", 122, 494},
        {"KARAKORUM", 699, 667}, {"CHINA_L", 1327, 820}, {"PERSIA_S", 340, 994},
        {"CHINA_S", 808, 1147},
    };

    private static final Object[][] LOCAIS_KHAN = {
        {"K1", 301, 282}, {"K2", 833, 436}, {"K3", 385, 820}, {"K4", 1026, 987},
        {"MELHORIAS", 51, 1349},
    };

    static final int LARG_FAIXA = 104;
    static final int ALT_FAIXA = 54;
    private static final int[] X_FAIXA = {146, 272, 400, 517, 650};
    private static final int Y_FAIXA = 1349;
    static final Point FORA_DO_TABULEIRO = new Point(1340, 1385);

    private final Map<String, Point[]> circulos = new HashMap<>();
    private final Map<String, Point> provincias = new HashMap<>();

    MapaVisual() {
        for (Object[] def : PROVINCIAS) {
            provincias.put((String) def[0], new Point((Integer) def[1], (Integer) def[2]));
        }
        for (Object[] def : CIRCULOS) {
            Point[] pontos = new Point[(def.length - 1) / 2];
            for (int i = 0; i < pontos.length; i++) {
                pontos[i] = new Point((Integer) def[1 + 2 * i], (Integer) def[2 + 2 * i]);
            }
            circulos.put((String) def[0], pontos);
        }
    }

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

    String provinciaEm(double x, double y) {
        for (Map.Entry<String, Point> entrada : provincias.entrySet()) {
            if (entrada.getValue().distance(x, y) <= RAIO_PROVINCIA) {
                return entrada.getKey();
            }
        }
        return null;
    }

    Point posicaoDaProvincia(String provinciaId) {
        return provincias.get(provinciaId);
    }

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

    Point posicaoDaCidade(String cidadeId) {
        for (Object[] def : CIDADES) {
            if (def[0].equals(cidadeId)) {
                return new Point((Integer) def[1], (Integer) def[2]);
            }
        }
        return null;
    }

    Point posicaoDoConselheiro(String conselheiroId) {
        for (Object[] def : CONSELHEIROS) {
            if (def[0].equals(conselheiroId)) {
                return new Point((Integer) def[1], (Integer) def[2]);
            }
        }
        return null;
    }

    String conselheiroEm(double x, double y) {
        for (Object[] def : CONSELHEIROS) {
            if (new Point((Integer) def[1], (Integer) def[2]).distance(x, y) <= 45) {
                return (String) def[0];
            }
        }
        return null;
    }

    Point posicaoDoLocalDoKhan(String local) {
        for (Object[] def : LOCAIS_KHAN) {
            if (def[0].equals(local)) {
                return new Point((Integer) def[1], (Integer) def[2]);
            }
        }
        return null;
    }

    String[] locaisDoKhan() {
        String[] ids = new String[LOCAIS_KHAN.length];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = (String) LOCAIS_KHAN[i][0];
        }
        return ids;
    }

    String localDoKhanEm(double x, double y) {
        for (Object[] def : LOCAIS_KHAN) {
            if (new Point((Integer) def[1], (Integer) def[2]).distance(x, y) <= 45) {
                return (String) def[0];
            }
        }
        return null;
    }

    Point centroDaFaixa(int indice) {
        return new Point(X_FAIXA[indice], Y_FAIXA);
    }

    int faixaEm(double x, double y) {
        for (int i = 0; i < X_FAIXA.length; i++) {
            if (Math.abs(x - X_FAIXA[i]) <= LARG_FAIXA / 2.0 && Math.abs(y - Y_FAIXA) <= ALT_FAIXA / 2.0) {
                return i;
            }
        }
        return -1;
    }
}
