package model;

public interface Observado {
    void adicionarObservador(Observador o);

    void removerObservador(Observador o);

    Object get(int i);
}
