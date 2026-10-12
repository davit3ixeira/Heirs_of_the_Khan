package model;

class Conselheiro {
	private final Parada parada;
	private final Demanda demanda;
	private TipoBonus bonus;

	Conselheiro(Parada parada, Demanda demanda) {
		this.parada = parada;
		this.demanda = demanda;
	}

	Parada getParada() {
		return parada;
	}

	Demanda getDemanda() {
		return demanda;
	}

	TipoBonus getBonus() {
		return bonus;
	}

	void setBonus(TipoBonus bonus) {
		this.bonus = bonus;
	}

	TipoBonus retirarBonus() {
		TipoBonus retirado = bonus;
		bonus = null;
		return retirado;
	}
}
