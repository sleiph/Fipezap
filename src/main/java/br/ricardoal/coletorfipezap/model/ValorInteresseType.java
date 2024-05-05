package br.ricardoal.coletorfipezap.model;

/**
 * Posicao das colunas de interesse do coletor
 */
public enum ValorInteresseType {

    NUMERO_INDICE_RES_V(2),
    VAR_MENSAL_RES_V(7),
    PRECO_MEDIO_RES_V(17),

    NUMERO_INDICE_RES_L(22),
    VAR_MENSAL_RES_L(27),
    PRECO_MEDIO_RES_L(37),

    RENTABILIDADE_RES(42),

    NUMERO_INDICE_COM_V(47),
    VAR_MENSAL_COM_V(48),
    PRECO_MEDIO_COM_V(50),

    NUMERO_INDICE_COM_L(51),
    VAR_MENSAL_COM_L(52),
    PRECO_MEDIO_COM_L(54),

    RENTABILIDADE_COM(55);

    private final int posicaoColuna;

    ValorInteresseType(int posicaoColuna) {
        this.posicaoColuna = posicaoColuna;
    }

    public int getPosicaoColuna() {
        return this.posicaoColuna;
    }

}
