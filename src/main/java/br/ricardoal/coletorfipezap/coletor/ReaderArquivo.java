package br.ricardoal.coletorfipezap.coletor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReaderArquivo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderArquivo.class);

    private final String URL_ARQUIVO = "https://downloads.fipe.org.br/indices/fipezap/fipezap-serieshistoricas.xlsx";

    public static final String PASTA_ARQUIVOS = "src\\main\\resources\\arquivos\\";
    public static final String ARQUIVO_BAIXADO = "fipezap_desse-mes.xlsx";

    public void criarDiretorio() {
        try {
            LOGGER.info("Criando diretorios");
            Files.createDirectories(Paths.get(System.getProperty("user.dir") + "\\" + PASTA_ARQUIVOS));
        } catch (IOException e) {
            LOGGER.error("Erro criando o diretorio para download:", e);
            throw new RuntimeException("Erro criando o diretorio para download:", e);
        }
    }

    public void baixar() {

        LOGGER.info("Baixando arquivo FipeZap");

        try(BufferedInputStream in = new BufferedInputStream(new URL(URL_ARQUIVO).openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(PASTA_ARQUIVOS + ARQUIVO_BAIXADO)) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            LOGGER.error("Erro baixando o arquivo:", e);
            //TODO: deletar o arquivo se der erro
            throw new RuntimeException(e);
        }
    }

}
