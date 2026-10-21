package model;

enum Herdeiro {
    ALTANI(1, 2, 1, 2),
    JOCHI(1, 1, 2, 2),
    CHAGATAI(1, 1, 2, 2),
    TOLUI(1, 2, 1, 2),
    OGEDEI(2, 1, 1, 2);

    static final int NUM_COLUNAS = 4;
    static final int COLUNA_BRANCA = 3;

    private final int[] cavalos;

    Herdeiro(int vermelha, int verde, int amarela, int branca) {
        cavalos = new int[] {vermelha, verde, amarela, branca};
    }

    int cavalos(int coluna) {
        return cavalos[coluna];
    }
}
