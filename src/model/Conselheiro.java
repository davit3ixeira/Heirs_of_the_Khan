package model;

import java.util.List;

final class Conselheiro {
    private final String id;
    private final Tesouro tipo;
    private final int quantidade;
    private final List<Tesouro> exigidos;
    private final int votos;
    private Bonus bonus;

    private Conselheiro(String id, Tesouro tipo, int quantidade, List<Tesouro> exigidos, int votos) {
        this.id = id;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.exigidos = exigidos;
        this.votos = votos;
    }

    static Conselheiro especifico(String id, Tesouro tipo) {
        return new Conselheiro(id, tipo, 2, null, 1);
    }

    static Conselheiro qualquer(String id) {
        return new Conselheiro(id, null, 3, null, 1);
    }

    static Conselheiro karakorum(Tesouro a, Tesouro b, Tesouro c) {
        return new Conselheiro(Tabuleiro.KARAKORUM, null, 3, List.of(a, b, c), 2);
    }

    String getId() {
        return id;
    }

    Tesouro getTipo() {
        return tipo;
    }

    int getQuantidade() {
        return quantidade;
    }

    List<Tesouro> getExigidos() {
        return exigidos;
    }

    int getVotos() {
        return votos;
    }

    Bonus getBonus() {
        return bonus;
    }

    void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }
}
