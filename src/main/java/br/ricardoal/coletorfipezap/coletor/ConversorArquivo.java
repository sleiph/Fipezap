package br.ricardoal.coletorfipezap.coletor;

import br.ricardoal.coletorfipezap.model.CidadeType;
import br.ricardoal.coletorfipezap.model.ValorInteresseType;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static br.ricardoal.coletorfipezap.coletor.ReaderArquivo.PASTA_ARQUIVOS;

public class ConversorArquivo {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConversorArquivo.class);

    private static final Date inicioColeta = new GregorianCalendar(2008, Calendar.JANUARY, 1).getTime();

    private final DateFormat dataFormat = new SimpleDateFormat("yyyy-MM");

    public File converter(CidadeType cidadeType, File arquivo) {

        LOGGER.info("Convertendo planilha {}", cidadeType);

        String nomeSaida = "FipeZap" + cidadeType.getSigla() + "_" + dataFormat.format(inicioColeta) + "_" + dataFormat.format(new Date());
        File convertido = new File(PASTA_ARQUIVOS + nomeSaida + ".csv");

        List<String> cabecalho = List.of(
                "Data",
                "Indice de vendas residenciais","Variacao mensal de vendas residenciais","Preco medio de vendas residenciais (RS/m2)",
                "Indice de alugueis residenciais","Variacao mensal de alugueis residenciais","Preco medio de alugueis residenciais (RS/m2)",
                "Rentabilidade dos alugueis residenciais",
                "Indice de vendas comerciais","Variacao mensal de vendas comerciais","Preco medio de vendas comerciais (RS/m2)",
                "Indice de alugueis comerciais","Variacao mensal de alugueis comerciais","Preco medio de alugueis comerciais (RS/m2)",
                "Rentabilidade dos alugueis comerciais"
        );
        int i = 0;

        try(FileInputStream baixado = new FileInputStream(arquivo);
            Workbook workbook = new XSSFWorkbook(baixado);
            PrintWriter pw = new PrintWriter(convertido)) {

            Sheet planilha = workbook.getSheetAt(cidadeType.getIndiceTab());

            List<List<String>> linhas = new ArrayList<>();
            linhas.add(cabecalho);

            for (Row linha : planilha) {
                i++;

                if (linha.getCell(1)==null || linha.getCell(1).getCellType() != CellType.NUMERIC)
                    continue;

                List<String> dados = this.converteLinha(linha);
                linhas.add(dados);
            }

            linhas.stream()
                    .map(l -> convertToCSV(l))
                    .forEach(pw::println);

            return convertido;

        } catch (Exception e) {
            LOGGER.error("Erro convertendo o arquivo: ", e);

            try {
                FileUtils.delete(convertido);
            } catch (IOException f) {
                LOGGER.error("E mais um erro deletando o arquivo: ", f);
            }

            throw new RuntimeException(e);
        }
    }

    protected List<String> converteLinha(Row linha) {
        List<String> dados = new ArrayList<>();

        Date data = linha.getCell(1).getDateCellValue();
        dados.add(dataFormat.format(data));

        for (ValorInteresseType valorInteresse : ValorInteresseType.values()) {
            dados.add(trataCelula(linha.getCell(valorInteresse.getPosicaoColuna())));
        }

        return dados;
    }

    private String convertToCSV(List<String> data) {
        return String.join(",", data);
    }

    private String trataCelula(Cell celula) {
        if (celula.getCellType().equals(CellType.NUMERIC))
            return String.valueOf(celula.getNumericCellValue());
        else
            return "";
    }

}
