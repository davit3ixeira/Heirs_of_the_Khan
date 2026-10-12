package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class Mapa {
	static final String KARAKORUM = "Karakorum";

	private final Map<String, Parada> paradas = new HashMap<String, Parada>();
	private final List<Parada> ordemDasParadas = new ArrayList<Parada>();
	private final Map<String, Provincia> provincias = new HashMap<String, Provincia>();
	private final List<Provincia> ordemDasProvincias = new ArrayList<Provincia>();
	private final Map<String, Cidade> cidades = new HashMap<String, Cidade>();
	private final List<Cidade> ordemDasCidades = new ArrayList<Cidade>();
	private final Map<String, Conselheiro> conselheiros = new HashMap<String, Conselheiro>();
	private final List<Conselheiro> ordemDosConselheiros = new ArrayList<Conselheiro>();

	void adicionarParada(String id, Regiao regiao, boolean dupla) {
		registrar(new Parada(id, regiao, dupla, false));
	}

	void adicionarKarakorum() {
		registrar(new Parada(KARAKORUM, null, false, true));
	}

	private void registrar(Parada parada) {
		paradas.put(parada.getId(), parada);
		ordemDasParadas.add(parada);
	}

	void ligar(String a, String b) {
		Parada primeira = paradas.get(a);
		Parada segunda = paradas.get(b);
		primeira.adicionarVizinha(segunda);
		segunda.adicionarVizinha(primeira);
	}

	void adicionarProvincia(String id, TipoTributo icone, boolean doKhan, String[] adjacentes) {
		Provincia provincia = new Provincia(id, icone, doKhan);
		for (String idParada : adjacentes) {
			Parada parada = paradas.get(idParada);
			provincia.adicionarParada(parada);
			parada.adicionarProvincia(provincia);
		}
		provincias.put(id, provincia);
		ordemDasProvincias.add(provincia);
	}

	void definirVizinhasDoKhan(String id, String primeira, String segunda) {
		Provincia provincia = provincias.get(id);
		provincia.adicionarVizinhaDoKhan(provincias.get(primeira));
		provincia.adicionarVizinhaDoKhan(provincias.get(segunda));
	}

	void adicionarCidade(String nome, Regiao regiao, String[] adjacentes) {
		Cidade cidade = new Cidade(nome, regiao);
		for (String idParada : adjacentes) {
			Parada parada = paradas.get(idParada);
			cidade.adicionarParada(parada);
			parada.adicionarCidade(cidade);
		}
		cidades.put(nome, cidade);
		ordemDasCidades.add(cidade);
	}

	void adicionarConselheiro(String idParada, Demanda demanda) {
		Parada parada = paradas.get(idParada);
		Conselheiro conselheiro = new Conselheiro(parada, demanda);
		parada.setConselheiro(conselheiro);
		conselheiros.put(idParada, conselheiro);
		ordemDosConselheiros.add(conselheiro);
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

	Conselheiro getConselheiro(String idParada) {
		return conselheiros.get(idParada);
	}

	List<Parada> getParadas() {
		return ordemDasParadas;
	}

	List<Provincia> getProvincias() {
		return ordemDasProvincias;
	}

	List<Cidade> getCidades() {
		return ordemDasCidades;
	}

	List<Conselheiro> getConselheiros() {
		return ordemDosConselheiros;
	}

	Set<Parada> alcancaveisComUmMovimento(Parada origem, Jogador jogador) {
		Set<Parada> alcancaveis = new HashSet<Parada>();
		Set<Parada> expandidas = new HashSet<Parada>();
		Queue<Parada> fila = new LinkedList<Parada>();
		fila.offer(origem);
		expandidas.add(origem);
		while (!fila.isEmpty()) {
			Parada atual = fila.poll();
			for (Parada vizinha : atual.getVizinhas()) {
				if (vizinha != origem) {
					alcancaveis.add(vizinha);
				}
				if (vizinha.temYurtDe(jogador) && !expandidas.contains(vizinha)) {
					expandidas.add(vizinha);
					fila.offer(vizinha);
				}
			}
		}
		return alcancaveis;
	}

	int distancia(Parada origem, Parada destino) {
		Map<Parada, Integer> distancias = new HashMap<Parada, Integer>();
		Queue<Parada> fila = new LinkedList<Parada>();
		distancias.put(origem, 0);
		fila.offer(origem);
		while (!fila.isEmpty()) {
			Parada atual = fila.poll();
			if (atual == destino) {
				return distancias.get(atual);
			}
			for (Parada vizinha : atual.getVizinhas()) {
				if (!distancias.containsKey(vizinha)) {
					distancias.put(vizinha, distancias.get(atual) + 1);
					fila.offer(vizinha);
				}
			}
		}
		return Integer.MAX_VALUE;
	}
}
