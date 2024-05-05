package br.ricardoal.coletorfipezap.model;

public enum CidadeType {

    TOTAL(2, "TOTAL"),
    SAOPAULO(3, "SP"),
    GUARULHOS(8, "GRU");

    private final int indiceTab;
    private final String sigla;

    CidadeType(int indiceTab, String sigla) {
        this.indiceTab = indiceTab;
        this.sigla = sigla;
    }

    public int getIndiceTab() {
        return indiceTab;
    }

    public String getSigla() {
        return sigla;
    }

}
