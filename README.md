# Model parcial - movimento e tributos

Este pacote substitui as classes homônimas do Model e inclui 14 testes JUnit 4.
Ele implementa um recorte das regras do manual:

- movimento por um percurso de paradas adjacentes, limitado pela quantidade de movimentos
  fornecida pelo Controller;
- passagem por paradas ocupadas, desde que o jogador não termine numa parada sem espaço;
- registro das paradas visitadas durante o turno;
- associação de províncias às paradas e limite de três tributos por província;
- coleta de tributos de províncias adjacentes ao percurso, respeitando o limite informado;
- armazenamento dos tributos do jogador e validação de gastos.

## Importar no Eclipse

1. Extraia a pasta `src/model` para a raiz do projeto Eclipse, substituindo as classes atuais
   de mesmo nome.
2. O pacote `model` deve aparecer dentro da pasta de código-fonte `src`.
3. Adicione **JUnit 4** ao Build Path: botão direito no projeto > **Build Path > Add Libraries...**
   > **JUnit** > **JUnit 4** > **Finish**.
4. Execute `JogoTest.java` com **Run As > JUnit Test**.

## API usada pelo Controller

- Configure o grafo usando `adicionarParada`, `ligarParadas`, `adicionarProvincia` e
  `associarProvincia`.
- Use `iniciarTurno(cor)` no começo do turno.
- Chame `moverJogador(cor, percurso, limiteMovimentos)`. O Controller calcula o limite
  a partir da ação ativada e informa-o ao Model.
- Para cada ação de coleta, passe uma escolha por peça a
  `coletarTributos(cor, limite, escolhas)`. Cada escolha informa o ID da província e o tipo
  (`ESPADA`, `YURT` ou `MOEDA`).
- `gastarTributos` verifica se o jogador possui as peças solicitadas.

O construtor mantém `P1`, `P2` e `P3` como uma rede demonstrativa para preservar o exemplo
`Main.java` e os testes anteriores. Esses pontos não são o mapa completo do jogo. Ataques,
conquista de cidades, yurts no tabuleiro, ação do Khan, melhorias, cartas, votos e condição
de vitória continuam fora deste recorte.
