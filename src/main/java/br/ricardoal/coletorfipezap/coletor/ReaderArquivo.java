package br.ricardoal.coletorfipezap.coletor;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ReaderArquivo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderArquivo.class);

    private static final Path PASTA_ARQUIVOS = Paths.get("src", "main", "resources", "arquivos");
    private static final String ARQUIVO_BAIXADO = "fipezap_desse-mes.xlsx";

    public File baixarArquivo() {
        Path diretorio = criarDiretorio();
        return baixar(diretorio);
    }

    private Path criarDiretorio() {
        try {
            Path absoluto = Paths.get(System.getProperty("user.dir"));
            Path caminho = absoluto.resolve(PASTA_ARQUIVOS);
            LOGGER.info("Criando diretorio {} se não existir", caminho);
            return Files.createDirectories(caminho);
        } catch (IOException e) {
            LOGGER.error("Erro criando o diretorio para download:", e);
            throw new RuntimeException("Erro criando o diretorio para download:", e);
        }
    }

    private File baixar(Path diretorio) {

        LOGGER.info("Baixando arquivo FipeZap");
        String URL_ARQUIVO = "https://downloads.fipe.org.br/indices/fipezap/fipezap-serieshistoricas.xlsx";

        File arquivo = new File(new File(diretorio.toUri()), ARQUIVO_BAIXADO);

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
