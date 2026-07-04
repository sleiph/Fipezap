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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ReaderArquivo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderArquivo.class);

    private static final String URL_ARQUIVO = "https://downloads.fipe.org.br/indices/fipezap/fipezap-serieshistoricas.xlsx";
    private static final String ARQUIVO_BAIXADO_NOME = "fipezap-serieshistoricas.xlsx";
    private static final DateTimeFormatter PASTA_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public Path baixarArquivo() {
        Path pastaDestino = criaDiretorioExecucao();
        return baixar(pastaDestino);
    }

    private Path baixar(Path diretorioAlvo) {

        LOGGER.info("Baixando arquivo FipeZap...");
        Path arquivoDestino = diretorioAlvo.resolve(ARQUIVO_BAIXADO_NOME);

        try {
            URI uri = URI.create(URL_ARQUIVO);
            URL url = uri.toURL();

            try(InputStream in = url.openStream()) {
                Files.copy(in, arquivoDestino, StandardCopyOption.REPLACE_EXISTING);
            }

            LOGGER.info("Download terminado.");
            return arquivoDestino;

        } catch (IOException e) {
            LOGGER.error("Erro baixando o arquivo: ", e);
            try {
                Files.deleteIfExists(arquivoDestino);
            } catch (IOException ex) {
                LOGGER.error("Erro ao deletar arquivo corrompido: ", ex);
            }
            throw new RuntimeException(e);
        }
    }

    private Path criaDiretorioExecucao() {
        try {
            String nomePasta = "fipezap_" + LocalDateTime.now().format(PASTA_FORMAT);
            Path diretorioBaseTemp = Path.of(System.getProperty("java.io.tmpdir"));
            Path diretorioExecucao = diretorioBaseTemp.resolve(nomePasta);

            Files.createDirectories(diretorioExecucao);
            LOGGER.info("Diretório de execução criado em: {}", diretorioExecucao);

            return diretorioExecucao;
        } catch (IOException e) {
            LOGGER.error("Erro ao criar diretório temporário de execução: ", e);
            throw new RuntimeException("Falha ao inicializar diretório", e);
        }
    }

}
