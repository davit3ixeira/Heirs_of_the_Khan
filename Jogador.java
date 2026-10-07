package model;

class Jogador {
    private final String cor;
    private Parada paradaAtual;

    Jogador(String cor, Parada inicial) {
        this.cor = cor;
        this.paradaAtual = inicial;
        inicial.entrar(this);
    }

    String getCor() {
        return cor;
    }

    Parada getParadaAtual() {
        return paradaAtual;
    }

    void irPara(Parada destino) {
        paradaAtual.sair(this);
        paradaAtual = destino;
        destino.entrar(this);
    }
}
