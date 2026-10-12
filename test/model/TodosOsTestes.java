package model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TesteCriacaoDoJogo.class, TesteMovimento.class, TesteTributo.class, TesteKhan.class, TesteAtaque.class,
		TesteYurts.class, TesteMelhorias.class, TesteEntrega.class, TesteCartasAbertas.class, TesteCartasSecretas.class,
		TesteColunas.class, TesteFimDeJogo.class, TesteApuracao.class, TesteHerdeiros.class, TesteMapaPadrao.class })
public class TodosOsTestes {
}
