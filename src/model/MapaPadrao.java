package model;

class MapaPadrao {
    private static final String[][] PARADAS = {
        {"KIEV_N", "1"}, {"KIEV_O", "1"}, {"KIEV_L", "1"},
        {"MOSCOU_O", "1"}, {"MOSCOU_L", "1"}, {"MOSCOU_S", "1"},
        {"SARAI_N", "1"}, {"SARAI_O", "1"}, {"SARAI_L", "1"},
        {"RUSSIA_O", "2"}, {"RUSSIA_L", "2"},
        {"KAESONG_O", "1"}, {"KAESONG_L", "1"}, {"KAESONG_S", "1"},
        {"PEQUIM_N", "1"}, {"PEQUIM_O", "1"}, {"PEQUIM_L", "1"},
        {"CANTAO_N", "1"}, {"CANTAO_O", "1"}, {"CANTAO_S", "1"},
        {"CHINA_L", "2"}, {"CHINA_S", "2"},
        {"SAMARCANDA_N", "1"}, {"SAMARCANDA_O", "1"}, {"SAMARCANDA_L", "1"},
        {"BAGDA_N", "1"}, {"BAGDA_L", "1"}, {"BAGDA_S", "1"},
        {"CABUL_N", "1"}, {"CABUL_L", "1"}, {"CABUL_S", "1"},
        {"PERSIA_N", "2"}, {"PERSIA_S", "2"},
    };

    private static final String[][] LIGACOES = {
        {"KIEV_N", "KIEV_O"}, {"KIEV_N", "KIEV_L"}, {"KIEV_O", "KIEV_L"},
        {"KIEV_N", "RUSSIA_O"}, {"KIEV_N", "MOSCOU_O"}, {"KIEV_L", "PERSIA_N"},
        {"RUSSIA_O", "MOSCOU_L"},
        {"MOSCOU_O", "MOSCOU_L"}, {"MOSCOU_O", "MOSCOU_S"}, {"MOSCOU_L", "MOSCOU_S"},
        {"MOSCOU_O", "PERSIA_N"}, {"MOSCOU_L", "SARAI_O"}, {"MOSCOU_S", "SAMARCANDA_N"},
        {"SARAI_N", "SARAI_O"}, {"SARAI_N", "SARAI_L"}, {"SARAI_O", "SARAI_L"},
        {"SARAI_O", Tabuleiro.KARAKORUM}, {"SARAI_L", "RUSSIA_L"},
        {"RUSSIA_L", Tabuleiro.KARAKORUM}, {"RUSSIA_L", "KAESONG_O"},
        {"KAESONG_O", "KAESONG_L"}, {"KAESONG_O", "KAESONG_S"}, {"KAESONG_L", "KAESONG_S"},
        {"KAESONG_S", "PEQUIM_N"}, {"KAESONG_S", "CHINA_L"},
        {"PEQUIM_N", Tabuleiro.KARAKORUM},
        {"PEQUIM_N", "PEQUIM_O"}, {"PEQUIM_N", "PEQUIM_L"}, {"PEQUIM_O", "PEQUIM_L"},
        {"PEQUIM_O", "CHINA_S"}, {"PEQUIM_O", "CABUL_N"}, {"PEQUIM_L", "CANTAO_N"},
        {"CHINA_L", "CANTAO_N"},
        {"CANTAO_N", "CANTAO_O"}, {"CANTAO_N", "CANTAO_S"}, {"CANTAO_O", "CANTAO_S"},
        {"CANTAO_O", "CHINA_S"}, {"CHINA_S", "CABUL_L"},
        {"CABUL_N", "CABUL_L"}, {"CABUL_N", "CABUL_S"}, {"CABUL_L", "CABUL_S"},
        {"CABUL_N", "PERSIA_S"}, {"CABUL_N", "SAMARCANDA_L"},
        {"PERSIA_S", "BAGDA_L"},
        {"BAGDA_N", "BAGDA_L"}, {"BAGDA_N", "BAGDA_S"}, {"BAGDA_L", "BAGDA_S"},
        {"BAGDA_N", "PERSIA_N"}, {"BAGDA_N", "SAMARCANDA_O"},
        {"SAMARCANDA_N", "SAMARCANDA_O"}, {"SAMARCANDA_N", "SAMARCANDA_L"},
        {"SAMARCANDA_O", "SAMARCANDA_L"}, {"SAMARCANDA_N", "PERSIA_N"},
        {"SAMARCANDA_L", Tabuleiro.KARAKORUM},
    };

    private static final String[][] PROVINCIAS = {
        {"PROV_01", "YURT", "KIEV_L", "KIEV_N", "MOSCOU_O", "PERSIA_N"},
        {"PROV_02", "ESPADA", "KIEV_N", "MOSCOU_L", "MOSCOU_O", "RUSSIA_O"},
        {"PROV_03", "MOEDA", "MOSCOU_O", "MOSCOU_S", "PERSIA_N", "SAMARCANDA_N"},
        {"PROV_04", "YURT", Tabuleiro.KARAKORUM, "MOSCOU_L", "MOSCOU_S", "SAMARCANDA_L",
            "SAMARCANDA_N", "SARAI_O"},
        {"PROV_05", "ESPADA", "BAGDA_N", "PERSIA_N", "SAMARCANDA_N", "SAMARCANDA_O"},
        {"PROV_06", "MOEDA", Tabuleiro.KARAKORUM, "RUSSIA_L", "SARAI_L", "SARAI_O"},
        {"PROV_07", "ESPADA", "KAESONG_O", "KAESONG_S", Tabuleiro.KARAKORUM, "PEQUIM_N",
            "RUSSIA_L"},
        {"PROV_08", "MOEDA", "BAGDA_L", "BAGDA_N", "CABUL_N", "PERSIA_S", "SAMARCANDA_L",
            "SAMARCANDA_O"},
        {"PROV_09", "YURT", "CABUL_N", Tabuleiro.KARAKORUM, "PEQUIM_N", "PEQUIM_O",
            "SAMARCANDA_L"},
        {"PROV_10", "MOEDA", "CANTAO_N", "CHINA_L", "KAESONG_S", "PEQUIM_L", "PEQUIM_N"},
        {"PROV_11", "YURT", "CANTAO_N", "CANTAO_O", "CHINA_S", "PEQUIM_L", "PEQUIM_O"},
        {"PROV_12", "ESPADA", "CABUL_L", "CABUL_N", "CHINA_S", "PEQUIM_O"},
    };

    static final String[] KHAN_LOCAIS = {"K1", "K2", "K3", "K4", "MELHORIAS"};

    static final String[][] KHAN_PROVINCIAS = {
        {"PROV_01", "PROV_02", "PROV_03"},
        {"PROV_04", "PROV_06", "PROV_07"},
        {"PROV_05", "PROV_08", "PROV_09"},
        {"PROV_10", "PROV_11", "PROV_12"},
    };

    static final String[][] KHAN_PARADAS = {
        {"KIEV_L", "KIEV_N", "MOSCOU_O", "PERSIA_N"},
        {"SARAI_O", "SARAI_L", "RUSSIA_L", "PEQUIM_N"},
        {"BAGDA_L", "SAMARCANDA_L", "SAMARCANDA_O", "CABUL_N", "PERSIA_S"},
        {"PEQUIM_L", "CANTAO_N", "CANTAO_O", "CHINA_S"},
    };

    private MapaPadrao() {
    }

    static void montar(Tabuleiro tabuleiro) {
        for (String[] parada : PARADAS) {
            tabuleiro.adicionarParada(parada[0], Integer.parseInt(parada[1]));
        }
        for (String[] ligacao : LIGACOES) {
            tabuleiro.ligarParadas(ligacao[0], ligacao[1]);
        }
        for (String[] provincia : PROVINCIAS) {
            tabuleiro.adicionarProvincia(provincia[0]);
            TipoTributo simbolo = TipoTributo.interpretar(provincia[1]);
            tabuleiro.getProvincia(provincia[0]).setSimbolo(simbolo);
            tabuleiro.getProvincia(provincia[0]).adicionarTributo(simbolo);
            for (int i = 2; i < provincia.length; i++) {
                tabuleiro.associarProvincia(provincia[i], provincia[0]);
            }
        }
    }
}
