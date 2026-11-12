package model;

enum Herdeiro {
    ALTANI(new int[] {1, 2, 1, 2}, new int[] {2, 1, 1, 0},
            new boolean[] {true, false, false, true}, new boolean[] {false, false, true, false}),
    JOCHI(new int[] {1, 1, 2, 2}, new int[] {1, 2, 1, 0},
            new boolean[] {false, true, false, true}, new boolean[] {true, false, false, false}),
    CHAGATAI(new int[] {1, 1, 2, 2}, new int[] {2, 1, 1, 0},
            new boolean[] {true, false, false, true}, new boolean[] {false, true, false, false}),
    TOLUI(new int[] {1, 2, 1, 2}, new int[] {1, 1, 2, 0},
            new boolean[] {false, false, true, true}, new boolean[] {true, false, false, false}),
    OGEDEI(new int[] {2, 1, 1, 2}, new int[] {1, 1, 2, 0},
            new boolean[] {false, false, true, true}, new boolean[] {false, true, false, false});

    static final int NUM_COLUNAS = 4;
    static final int COLUNA_BRANCA = 3;

    private final int[] cavalos;
    private final int[] maos;
    private final boolean[] khan;
    private final boolean[] moedaVirtual;

    Herdeiro(int[] cavalos, int[] maos, boolean[] khan, boolean[] moedaVirtual) {
        this.cavalos = cavalos;
        this.maos = maos;
        this.khan = khan;
        this.moedaVirtual = moedaVirtual;
    }

    int cavalos(int coluna) {
        return cavalos[coluna];
    }

    int maos(int coluna) {
        return maos[coluna];
    }

    boolean khan(int coluna) {
        return khan[coluna];
    }

    boolean moedaVirtual(int coluna) {
        return moedaVirtual[coluna];
    }

    boolean recupera(int coluna) {
        return coluna == COLUNA_BRANCA;
    }
}
