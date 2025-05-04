package br.ricardoal.coletorfipezap.coletor;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReaderArquivo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderArquivo.class);

    public static final String PASTA_ARQUIVOS = "src\\main\\resources\\arquivos\\";
    public static final String ARQUIVO_BAIXADO = "fipezap_desse-mes.xlsx";

    public File baixarArquivo() {
        criarDiretorio();
        return baixar();
    }

    private void criarDiretorio() {
        try {
            String caminho = System.getProperty("user.dir") + "\\" + PASTA_ARQUIVOS;
            LOGGER.info("Criando diretorio {} se não existir", caminho);
            Files.createDirectories(Paths.get(caminho));
        } catch (IOException e) {
            LOGGER.error("Erro criando o diretorio para download:", e);
            throw new RuntimeException("Erro criando o diretorio para download:", e);
        }
    }

    private File baixar() {

        LOGGER.info("Baixando arquivo FipeZap");
        String URL_ARQUIVO = "https://downloads.fipe.org.br/indices/fipezap/fipezap-serieshistoricas.xlsx";

        File arquivo = new File(PASTA_ARQUIVOS + ARQUIVO_BAIXADO);

        try(
                BufferedInputStream in = new BufferedInputStream(new URL(URL_ARQUIVO).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(arquivo)
        ) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }

            return arquivo;
        } catch (IOException e) {
            LOGGER.error("Erro baixando o arquivo: ", e);

            try {
                FileUtils.delete(arquivo);
            } catch (IOException f) {
                LOGGER.error("E erro deletando o arquivo também: ", f);
            }

            throw new RuntimeException(e);
        }
    }

}
