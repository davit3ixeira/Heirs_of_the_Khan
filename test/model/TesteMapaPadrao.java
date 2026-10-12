package model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TesteMapaPadrao {
	private static final int DEFAULT_TIMEOUT = 2000;

	private Mapa padrao;

	@Before
	public void criarMapaPadrao() {
		padrao = MapaPadrao.criar();
	}

	private int provinciasDoKhan() {
		int total = 0;
		for (Provincia provincia : padrao.getProvincias()) {
			if (provincia.isDoKhan()) {
				total++;
			}
		}
		return total;
	}

	private int paradasDuplas() {
		int total = 0;
		for (Parada parada : padrao.getParadas()) {
			if (parada.isDupla()) {
				total++;
			}
		}
		return total;
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void mapaPadraoTemNoveCidades() {
		assertEquals("Tres cidades em cada uma das 3 regioes", 9, padrao.getCidades().size());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void mapaPadraoTemSeteConselheiros() {
		assertEquals("6 paradas duplas mais Karakorum", 7, padrao.getConselheiros().size());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void mapaPadraoTemSeisParadasDuplas() {
		assertEquals("Cada parada dupla abriga um conselheiro", 6, paradasDuplas());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void mapaPadraoTemQuatroProvinciasDoKhan() {
		assertEquals("O manual preve 4 provincias do Khan", 4, provinciasDoKhan());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void provinciaDoKhanApontaParaDuasVizinhas() {
		assertEquals("Cada provincia do Khan tem 2 setas", 2, padrao.getProvincia("khan_central").getVizinhasDoKhan().size());
	}

	@Test(timeout = DEFAULT_TIMEOUT)
	public void todasAsParadasSaoAlcancaveisAPartirDeKarakorum() {
		assertEquals("A parada mais distante do anel da Persia fica a 4 rotas", 4, padrao.distancia(padrao.getKarakorum(), padrao.getParada("P4")));
	}
}
