package br.ricardoal.coletorfipezap.coletor;

import br.ricardoal.coletorfipezap.model.CidadeType;
import br.ricardoal.coletorfipezap.model.ColunaType;
import br.ricardoal.coletorfipezap.model.ValorInteresseType;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ConversorArquivo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversorArquivo.class);

    private static final LocalDate INICIO_COLETA = LocalDate.of(2008, Month.JANUARY, 1);
    private static final DateTimeFormatter DATA_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");

    public Path converter(CidadeType cidadeType, Path arquivoExcel) {

        LOGGER.info("Convertendo planilha {}", cidadeType);

        String nomeSaida = String.format("FipeZap%s_%s_%s.csv",
                cidadeType.getSigla(),
                DATA_FORMAT.format(INICIO_COLETA),
                DATA_FORMAT.format(LocalDate.now()));

        Path arquivoSaida = arquivoExcel.getParent().resolve(nomeSaida);

        try (Workbook workbook = WorkbookFactory.create(arquivoExcel.toFile());
             PrintWriter pw = new PrintWriter(Files.newBufferedWriter(arquivoSaida, StandardCharsets.UTF_8))) {

            Sheet planilha = workbook.getSheetAt(cidadeType.getIndiceTab());
            pw.println(convertToCSV(ColunaType.getListaColunas()));

            for (Row linha : planilha) {
                Cell celulaData = linha.getCell(1);
                if (celulaData==null || celulaData.getCellType() != CellType.NUMERIC)
                    continue;

                List<String> dados = this.converteLinha(linha);
                pw.println(convertToCSV(dados));
            }

            return arquivoSaida;

        } catch (Exception e) {
            LOGGER.error("Erro convertendo o arquivo do tipo {}: ", cidadeType, e);

            try {
                Files.deleteIfExists(arquivoSaida);
            } catch (IOException f) {
                LOGGER.error("E mais um erro deletando o arquivo: ", f);
            }

            throw new RuntimeException(e);
        }
    }

    protected List<String> converteLinha(Row linha) {
        List<String> dados = new ArrayList<>();

        LocalDate data = linha.getCell(1).getLocalDateTimeCellValue().toLocalDate();
        dados.add(data.format(DATA_FORMAT));

        for (ValorInteresseType valorInteresse : ValorInteresseType.values()) {
            dados.add(trataCelula(linha.getCell(valorInteresse.getPosicaoColuna())));
        }

        return dados;
    }

    private String convertToCSV(List<String> data) {
        return data.stream()
                .map(this::trataCaracteresEspeciais)
                .collect(Collectors.joining(","));
    }

    private String trataCelula(Cell celula) {
        if (celula == null) {
            return "";
        }

        if (celula.getCellType() == CellType.NUMERIC) {
            double valor = celula.getNumericCellValue();
            if (valor == (long) valor) {
                return String.valueOf((long) valor);
            }
            return String.valueOf(valor);
        }

        return "";
    }

    private String trataCaracteresEspeciais(String dados) {
        if (dados == null) {
            return "";
        }
        String escapedData = dados.replaceAll("\\R", " "); // Remove quebras de linha dentro da célula
        if (dados.contains(",") || dados.contains("\"") || dados.contains("'")) {
            escapedData = escapedData.replace("\"", "\"\"");
            return "\"" + escapedData + "\"";
        }
        return escapedData;
    }

}
