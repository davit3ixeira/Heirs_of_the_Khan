package model;

final class Melhoria {
    private static final String[] LETRA_COLUNA = {"R", "V", "A"};

    private final TipoMelhoria tipo;
    private final int coluna;
    private final Tesouro tesouro;

    Melhoria(TipoMelhoria tipo, int coluna, Tesouro tesouro) {
        this.tipo = tipo;
        this.coluna = coluna;
        this.tesouro = tesouro;
    }

    TipoMelhoria getTipo() {
        return tipo;
    }

    int getColuna() {
        return coluna;
    }

    Tesouro getTesouro() {
        return tesouro;
    }

    String getId() {
        switch (tipo) {
        case MOEDA:
            return "MOE-" + LETRA_COLUNA[coluna];
        case ESPADA:
            return "ESP-" + LETRA_COLUNA[coluna];
        case YURT:
            return "CAS-" + LETRA_COLUNA[coluna];
        case MAO:
            return "MAO-" + LETRA_COLUNA[coluna];
        case CAVALOS:
            return "CAV-" + LETRA_COLUNA[coluna];
        default:
            switch (tesouro) {
            case BOI:
                return "BOI-A";
            case ESPECIARIA:
                return "CUI-A";
            case FERRO:
                return "MOB-R";
            case PELE:
                return "MOP-V";
            default:
                return "PER-V";
            }
        }
    }

    String getSimbolo() {
        return tipo == TipoMelhoria.TESOURO ? "T-" + tesouro.name() : tipo.name();
    }

    static Melhoria deTesouro(Tesouro tesouro) {
        int coluna;
        switch (tesouro) {
        case BOI:
        case ESPECIARIA:
            coluna = 2;
            break;
        case FERRO:
            coluna = 0;
            break;
        default:
            coluna = 1;
            break;
        }
        return new Melhoria(TipoMelhoria.TESOURO, coluna, tesouro);
    }
}
