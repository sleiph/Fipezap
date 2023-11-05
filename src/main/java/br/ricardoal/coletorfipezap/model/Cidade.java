package br.ricardoal.coletorfipezap.model;

public enum Cidade {

    SAOPAULO("sao-paulo", 3),
    GUARULHOS("guarulhos", 8);

    private String nomeArquivo;

    private int indiceTab;

    Cidade(String nomeArquivo, int indiceTab) {
        this.nomeArquivo = nomeArquivo;
        this.indiceTab = indiceTab;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public int getIndiceTab() {
        return indiceTab;
    }

}
