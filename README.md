# Herdeiros de Khan — Model (1ª iteração, INF1636)

Componente **Model** (pacote `model`) do jogo "Herdeiros de Khan", com testes JUnit 4. View e Controller ficam para as próximas iterações.

## Estrutura

- `src/model/`: o Model. Só são públicos `Jogo` (a API), `JogadaInvalidaException` e os enums usados na API (`Cor`, `Herdeiro`, `Regiao`, `TipoTesouro`, `TipoTributo`, `Coluna`, `TipoMelhoria`, `TipoBonus`).
- `test/model/`: testes JUnit 4, no mesmo pacote. A suíte é `TodosOsTestes`.
- `lib/`: `junit-4.13.2.jar` e `hamcrest-core-1.3.jar`, para rodar pela linha de comando.
- `.project` e `.classpath`: projeto Eclipse com `src` e `test` como pastas de código e a biblioteca JUnit 4 do próprio Eclipse.

## Como compilar e rodar

Linux/macOS:

```
javac -d bin -cp lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar src/model/*.java test/model/*.java
java -cp bin:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore model.TodosOsTestes
```

Windows: troque `:` por `;` no classpath.

No Eclipse: *File > Import > Existing Projects into Workspace*, depois clique com o botão direito em `TodosOsTestes` e escolha *Run As > JUnit Test*.

Resultado atual: **140 testes, todos passando**.

## Arquitetura

- **API:** `Jogo` reúne toda a API. O construtor público usa `MapaPadrao` e `SorteadorAleatorio`. O construtor de pacote recebe `Mapa` e `Sorteador`, o que permite testar de forma determinística. A API pode ser convertida em Singleton/Façade na 3ª iteração.
- **Aberto/fechado:** novos elementos entram como novas subclasses, sem alterar as classes de regras:
  - pedidos de conselheiro: `Demanda`;
  - cartas abertas: `CartaConselheiroAberto`;
  - cartas secretas: `CartaConselheiroSecreto`;
  - habilidades: `HabilidadeHerdeiro`.

  A topologia do mapa fica isolada em `MapaPadrao`, e a composição das pilhas fica em `Componentes`.
- **Sorteio:** fica atrás da interface `Sorteador`. `Math.random()` aparece apenas em `SorteadorAleatorio`. Para embaralhar, `Pilha<X>` retira repetidamente um índice sorteado.
- **Coleções:** usa `HashMap` em todos os lugares onde cabe: elementos do mapa por id, tributos, tesouros, estoque, colunas do tabuleiro, recursos virtuais e ataques por cidade. `List` é usada onde a ordem importa, e as buscas em largura usam `LinkedList` como `Queue`.

## Regras implementadas

- **Preparação:**
  - embaralha cidades, tesouros, cartas, melhorias e bônus;
  - revela 3 cidades com 4 tesouros cada e coloca 1 bônus por conselheiro;
  - revela 4 cartas abertas e 5 melhorias;
  - coloca 1 tributo por província e distribui as moedas iniciais (1, 1, 2, 2, 2);
  - entrega 2 cartas secretas por jogador. É preciso escolher uma antes do 1º turno.
- **Turno:**
  - ativação de colunas com as ações impressas mais as das melhorias;
  - a coluna ocupada não pode ser reativada;
  - a coluna branca recupera todas as peças;
  - a melhoria comprada só vale na próxima ativação.
- **Movimento:**
  - cada cavalo vale 1 movimento;
  - pular cadeias de yurts próprios não custa movimento, e a parada pulada não conta como visitada;
  - pode-se passar por parada ocupada, mas não terminar o turno nela (a parada dupla aceita 2 peões e Karakorum aceita todos).
- **Tributo:** só de províncias adjacentes a paradas visitadas, limitado pelos punhos.
- **Khan:**
  - é obrigatório nas colunas amarela e branca e não pode ficar onde está;
  - numa província, abastece ela e as 2 vizinhas, respeitando o máximo de 3 e o estoque;
  - na área de melhorias, dá moeda/espada se os espaços correspondentes estiverem vazios e depois reabastece a área.
- **Ataque:**
  - custa 1 espada e, depois, 2 por tesouro da mesma cidade no mesmo turno;
  - quem toma o último tesouro conquista a cidade: coloca seu yurt nela e revela a próxima.
- **Yurts:**
  - só em parada visitada;
  - capacidade de 1 por parada simples e 2 por dupla, e nenhum em Karakorum;
  - exige yurt na reserva.
- **Melhorias:**
  - custam 2 moedas e podem vir da área ou do topo da pilha;
  - vão para a coluna da cor de fundo, com no máximo 4;
  - depois da compra, a área desliza para a direita;
  - completar uma linha dá uma melhoria gratuita na coluna branca, e essa escolha pendente impede finalizar o turno.
- **Recursos virtuais:** são gastos antes das peças. As peças gastas voltam ao estoque. O tesouro virtual completa uma única entrega.
- **Entregas:**
  - pedidos "2 iguais" (1 voto), "3 quaisquer" (1 voto) e Karakorum com "3 específicos" (2 votos);
  - o bônus vai para quem entrega primeiro.
- **Bônus:** os 7 tipos estão implementados. "Três tesouros" deixa uma escolha pendente.
- **Cartas abertas:** as 14 estão implementadas. São verificadas depois de cada ação, só para o jogador da vez.
- **Cartas secretas:** as 10 estão implementadas, com 1 voto por limite atingido.
- **Votos e fim de jogo:**
  - todo voto avança também a ficha neutra;
  - o fim é disparado pelo limite de votos (10, 14, 16 ou 17) ou pela conquista de todas as cidades;
  - depois do disparo, termina-se a rodada atual e joga-se mais uma rodada completa.
- **Apuração:**
  - influência por região, com a tabela indexada pelas melhorias da coluna da região e as regras de empate;
  - votos da carta secreta;
  - desempates por tesouros não entregues e, depois, pela distância até Karakorum. Se o empate persistir, a vitória é compartilhada.

## Suposições (provisórias)

1. **Topologia do `MapaPadrao`:** o manual escaneado não permite ler as rotas, então a topologia é provisória.
   - Seguem o manual: as cidades, as regiões e os 7 conselheiros.
   - O resto foi inventado:
     - em cada região, um anel de 6 paradas (as 3ª e 6ª são duplas);
     - Karakorum ligada a uma parada de cada região, mais 3 ligações de fronteira;
     - em cada região, 3 províncias comuns e 1 do Khan, mais uma província central do Khan junto a Karakorum.
   - A topologia definitiva deve ser transcrita só em `MapaPadrao`.
2. **Cores de fundo das 44 melhorias:** distribuídas em rodízio vermelha/verde/amarela, em `Componentes`.
3. **Tesouros e Karakorum:** os tipos `boi`, `especiaria` e `seda` são provisórios. O pedido de Karakorum (pele, especiaria e seda no mapa padrão) é configurável pelo construtor de `DemandaTresEspecificos`.
4. **Área de melhorias:** os espaços com ícone de moeda e de espada são os índices 0 e 2 (`AreaMelhorias.ESPACO_MOEDA` e `AreaMelhorias.ESPACO_ESPADA`). As melhorias deslizam em direção ao índice 4.
5. **Decisões de interpretação:**
   - O yurt inicial do Tolui sai da reserva dele, que fica com 11.
   - O bônus de voto é aplicado assim que é recebido.
   - Os bônus de duas espadas, duas moedas e dois yurts dão peças do estoque, respeitando o que estiver disponível.
   - Na apuração, jogador sem yurts na região não fica em 1º nem em 2º.
   - A carta "4 espadas" conta só peças. A carta "2 colunas com 2 melhorias" considera as 4 colunas.
   - O yurt da cidade conquistada só é colocado se ainda houver yurt na reserva.
   - Tesouros entregues saem do jogo.

## Habilidades de herdeiros pendentes

- **Tolui** (implementado):
  - começa com um yurt em Karakorum, que ajuda no movimento, conta como yurt no mapa e não conta para a influência;
  - pode construir em parada adjacente à sua posição.
- **Jochi** (implementado): o 1º espaço da coluna branca já vem impresso, então a 1ª linha não dá melhoria gratuita. O efeito do ícone impresso nesse espaço não foi modelado, porque o manual não o descreve.
- **Altani, Chagatai e Ogedei:** têm as classes `HabilidadeAltani`, `HabilidadeChagatai` e `HabilidadeOgedei` como pontos de extensão, ainda sem comportamento próprio.
