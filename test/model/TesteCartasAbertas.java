package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TesteCartasAbertas extends TesteBase {

	private void vermelhoChegaAQuatroEspadas() throws JogadaInvalidaException {
		darTributos(Cor.vermelho, TipoTributo.espada, 3);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.pegarTributo("pa");
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void primeiroATerQuatroEspadasGanhaUmVoto() throws JogadaInvalidaException {
		vermelhoChegaAQuatroEspadas();
		assertEquals("A carta de 4 espadas vale 1 voto", 1, jogo.getVotos(Cor.vermelho));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void cartaCumpridaSaiDoJogo() throws JogadaInvalidaException {
		vermelhoChegaAQuatroEspadas();
		assertEquals("Restam 3 das 4 cartas reveladas", 3, jogo.getQuantidadeDeCartasAbertas());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void votoDaCartaAvancaAFichaNeutra() throws JogadaInvalidaException {
		vermelhoChegaAQuatroEspadas();
		assertEquals("Todo voto ganho avanca tambem a ficha neutra", 1, jogo.getVotosNeutros());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void cartaSoEhVerificadaParaOJogadorDaVez() throws JogadaInvalidaException {
		darTributos(Cor.azul, TipoTributo.espada, 4);
		jogo.iniciarTurno(Coluna.vermelha);
		jogo.pegarTributo("pa");
		assertEquals("O azul tem 4 espadas, mas nao e a vez dele", 0, jogo.getVotos(Cor.azul));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void cartaDeLinhaDeMelhoriasEhCumpridaComUmaLinha() throws JogadaInvalidaException {
		colocarMelhorias(Cor.vermelho, Coluna.vermelha, TipoMelhoria.punho, 1);
		colocarMelhorias(Cor.vermelho, Coluna.verde, TipoMelhoria.punho, 1);
		colocarMelhorias(Cor.vermelho, Coluna.amarela, TipoMelhoria.punho, 1);
		assertTrue("Uma linha completa cumpre a carta", new CartaAbertaLinhaDeMelhorias().cumprida(jogador(Cor.vermelho), mapa));
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void cartaDeYurtsEmCadaRegiaoExigeTodasAsRegioes() {
		colocarYurt(Cor.vermelho, "A");
		colocarYurt(Cor.vermelho, "D");
		assertFalse("Falta um yurt na Persia", new CartaAbertaYurtsEmCadaRegiao(1).cumprida(jogador(Cor.vermelho), mapa));
	}
}
