package model;

enum Regiao {
    RUSSIA(2, "RUSSIA", "KIEV", "MOSCOU", "SARAI"),
    CHINA(0, "CHINA", "PEQUIM", "CANTAO", "KAESONG"),
    PERSIA(1, "PERSIA", "SAMARCANDA", "BAGDA", "CABUL");

    final int coluna;
    private final String[] prefixos;

    Regiao(int coluna, String... prefixos) {
        this.coluna = coluna;
        this.prefixos = prefixos;
    }

    static Regiao daParada(String paradaId) {
        if (paradaId == null) {
            return null;
        }
        for (Regiao regiao : values()) {
            for (String prefixo : regiao.prefixos) {
                if (paradaId.startsWith(prefixo + "_")) {
                    return regiao;
                }
            }
        }
        return null;
    }

    static Regiao daCidade(String cidadeId) {
        for (Regiao regiao : values()) {
            for (int i = 1; i < regiao.prefixos.length; i++) {
                if (regiao.prefixos[i].equals(cidadeId)) {
                    return regiao;
                }
            }
        }
        return null;
    }
}
