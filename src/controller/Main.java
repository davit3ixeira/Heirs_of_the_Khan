package controller;

public class Main {
    public static void main(String[] args) {
        String[] cores = {"vermelho", "azul", "verde", "amarelo", "preto"};
        ControladorJogo c = new ControladorJogo(cores);
        c.iniciar();
    }
}
