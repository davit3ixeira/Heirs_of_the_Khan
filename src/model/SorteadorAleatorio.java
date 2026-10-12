package model;

class SorteadorAleatorio implements Sorteador {
	public int sortear(int n) {
		return (int) (Math.random() * n);
	}
}
