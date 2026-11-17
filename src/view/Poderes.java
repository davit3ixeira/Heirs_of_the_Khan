package view;

final class Poderes {
    static final String[] NOMES = {"Altani", "Jochi", "Chagatai", "Tolui", "Ogedei"};

    static final String[] DESCRICOES = {
        "Você tem 2 peões. Todo turno, escolha um para mover e fazer ações. O outro não pode fazer nada.",
        "No início do jogo, pegue 2 melhorias ao acaso e coloque uma delas na sua coluna branca. "
                + "Você não precisa seguir a cor das peças de melhoria quando pegá-las.",
        "1x por turno, você pode deixar de usar 1 cavalo para ganhar 1 peça de espada.",
        "No início do jogo, coloque 1 yurt em Karakorum. Ele não pertence a nenhuma região. "
                + "Você pode colocar yurts em espaços adjacentes a você.",
        "1x por turno, você pode pagar uma moeda para pegar um tesouro de qualquer cidade no mapa "
                + "(desde que não seja o último tesouro da cidade)."
    };

    static final String[] ROTULOS = {
        "Trocar peão ativo", "Poder: melhorias iniciais", "Cavalo por espada",
        "Poder: yurts adjacentes", "Poder: botão direito no tesouro"
    };

    private Poderes() {
    }

    static String dica(int numeroDoTabuleiro) {
        int i = numeroDoTabuleiro - 1;
        return "<html><body style='width:260px'><b>" + NOMES[i] + "</b><br>" + DESCRICOES[i] + "</body></html>";
    }
}
