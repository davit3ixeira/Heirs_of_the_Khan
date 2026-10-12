package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Mapa {
	static final String KARAKORUM = "Karakorum";

	private final Map<String, Parada> paradas = new HashMap<String, Parada>();
	private final Map<String, Provincia> provincias = new HashMap<String, Provincia>();
	private final Map<String, Cidade> cidades = new HashMap<String, Cidade>();
	private final List<Cidade> ordemDasCidades = new ArrayList<Cidade>();

	void adicionarParada(String id, Regiao regiao, boolean dupla) {
		paradas.put(id, new Parada(id, regiao, dupla, false));
	}

	void adicionarKarakorum() {
		paradas.put(KARAKORUM, new Parada(KARAKORUM, null, false, true));
	}

	void ligar(String a, String b) {
		Parada primeira = paradas.get(a);
		Parada segunda = paradas.get(b);
		primeira.adicionarVizinha(segunda);
		segunda.adicionarVizinha(primeira);
	}

	void adicionarProvincia(String id, TipoTributo icone, String[] adjacentes) {
		Provincia provincia = new Provincia(id, icone);
		for (String idParada : adjacentes) {
			provincia.adicionarParada(paradas.get(idParada));
		}
		provincias.put(id, provincia);
	}

	void adicionarCidade(String nome, Regiao regiao, String[] adjacentes) {
		Cidade cidade = new Cidade(nome, regiao);
		for (String idParada : adjacentes) {
			cidade.adicionarParada(paradas.get(idParada));
		}
		cidades.put(nome, cidade);
		ordemDasCidades.add(cidade);
	}

	void definirDemanda(String idParada, Demanda demanda) {
		paradas.get(idParada).setDemanda(demanda);
	}

	Parada getParada(String id) {
		return paradas.get(id);
	}

	Parada getKarakorum() {
		return paradas.get(KARAKORUM);
	}

	Provincia getProvincia(String id) {
		return provincias.get(id);
	}

	Cidade getCidade(String nome) {
		return cidades.get(nome);
	}

	List<Cidade> getCidades() {
		return ordemDasCidades;
	}

	List<Provincia> getProvincias() {
		return new ArrayList<Provincia>(provincias.values());
	}

	Set<Parada> alcancaveisComUmMovimento(Parada origem, Jogador jogador) {
		Set<Parada> alcancaveis = new HashSet<Parada>();
		Set<Parada> expandidas = new HashSet<Parada>();
		LinkedList<Parada> fila = new LinkedList<Parada>();
		fila.addLast(origem);
		expandidas.add(origem);
		while (!fila.isEmpty()) {
			Parada atual = fila.removeFirst();
			for (Parada vizinha : atual.getVizinhas()) {
				if (vizinha != origem) {
					alcancaveis.add(vizinha);
				}
				if (vizinha.temYurtDe(jogador) && !expandidas.contains(vizinha)) {
					expandidas.add(vizinha);
					fila.addLast(vizinha);
				}
			}
		}
		return alcancaveis;
	}
}
