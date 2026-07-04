package br.ricardoal.coletorfipezap.coletor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class ReaderArquivo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderArquivo.class);

    private static String URL_ARQUIVO = "https://downloads.fipe.org.br/indices/fipezap/fipezap-serieshistoricas.xlsx";
    private static final String ARQUIVO_BAIXADO_PREFIX = "fipezap_";
    private static final String ARQUIVO_BAIXADO_SUFFIX = ".xlsx";

    public File baixarArquivo() {
        return baixar().toFile();
    }

    private Path baixar() {

        LOGGER.info("Baixando arquivo FipeZap...");
        Path arquivoTemp = null;

        try {
            arquivoTemp = Files.createTempFile(ARQUIVO_BAIXADO_PREFIX, ARQUIVO_BAIXADO_SUFFIX);

            arquivoTemp.toFile().deleteOnExit();
            LOGGER.info("Arquivo temporário criado em: {}", arquivoTemp);

            URI uri = URI.create(URL_ARQUIVO);
            URL url = uri.toURL();

            try(InputStream in = url.openStream()) {
                Files.copy(in, arquivoTemp, StandardCopyOption.REPLACE_EXISTING);
            }

            LOGGER.info("Download terminado.");
            return arquivoTemp;

        } catch (IOException e) {
            LOGGER.error("Erro baixando o arquivo: ", e);
            if (arquivoTemp != null) {
                try {
                    Files.deleteIfExists(arquivoTemp);
                } catch (IOException ex) {
                    LOGGER.error("Erro ao deletar o arquivo temporário corrompido: ", ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

}
