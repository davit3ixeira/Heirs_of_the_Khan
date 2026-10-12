package model;

public class JogadaInvalidaException extends Exception {
	private static final long serialVersionUID = 1L;

	public JogadaInvalidaException() {
		super();
	}

	public JogadaInvalidaException(String mensagem) {
		super(mensagem);
	}
}
