package br.ricardoal.coletorfipezap.model;

public enum CidadeType {

    TOTAL(2, "TOTAL", true),
    SAOPAULO(3, "SP", true),
    GUARULHOS(8, "GRU", true),
    SANTOS(13, "SNT", false),
    RIODEJANEIRO(19, "RJ", false);

    private final int indiceTab;
    private final String sigla;
    private final boolean coletada;

    CidadeType(int indiceTab, String sigla, boolean coletada) {
        this.indiceTab = indiceTab;
        this.sigla = sigla;
        this.coletada = coletada;
    }

    public int getIndiceTab() {
        return indiceTab;
    }

    public String getSigla() {
        return sigla;
    }

    public boolean isColetada() {
        return coletada;
    }

}
