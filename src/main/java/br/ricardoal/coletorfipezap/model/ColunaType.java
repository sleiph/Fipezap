package br.ricardoal.coletorfipezap.model;

import java.util.ArrayList;
import java.util.List;

public enum ColunaType {

    DATA("data","Data"),
    VENDA_RES_IND("indice-venda-residencial","Indice de vendas residenciais"),
    VENDA_RES_VAR("variacao-venda-residencial","Variacao mensal de vendas residenciais"),
    VENDA_RES_MED("media-venda-residencial","Preco medio de vendas residenciais (RS/m2)"),
    ALUGUEL_RES_IND("indice-aluguel-residencial","Indice de alugueis residenciais"),
    ALUGUEL_RES_VAR("variacao-aluguel-residencial","Variacao mensal de alugueis residenciais"),
    ALUGUEL_RES_MED("media-aluguel-residencial","Preco medio de alugueis residenciais (RS/m2)"),
    ALUGUEL_RES_RENT("rentabilidade-aluguel-residencial","Rentabilidade dos alugueis residenciais"),
    VENDA_COM_IND("indice-venda-comercial","Indice de vendas comerciais"),
    VENDA_COM_VAR("variacao-venda-comercial","Variacao mensal de vendas comerciais"),
    VENDA_COM_MED("media-venda-comercial","Preco medio de vendas comerciais (RS/m2)"),
    ALUGUEL_COM_IND("indice-aluguel-comercial","Indice de alugueis comerciais"),
    ALUGUEL_COM_VAR("variacao-aluguel-comercial","Variacao mensal de alugueis comerciais"),
    ALUGUEL_COM_MED("media-aluguel-comercial","Preco medio de alugueis comerciais (RS/m2)"),
    ALUGUEL_COM_RENT("rentabilidade-aluguel-comercial","Rentabilidade dos alugueis comerciais");

    private final String nome;
    private final String descricao;

    private static final List<String> listaColunas = new ArrayList<>();

    ColunaType(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    static{
        for (ColunaType type : values()) {
            listaColunas.add(type.getNome());
        }
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public static List<String> getListaColunas() {
        return listaColunas;
    }

}
