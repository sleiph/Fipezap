package br.ricardoal.coletorfipezap.coletor;

import br.ricardoal.coletorfipezap.model.CidadeType;
import br.ricardoal.coletorfipezap.model.ValorInteresseType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static br.ricardoal.coletorfipezap.coletor.ReaderArquivo.ARQUIVO_BAIXADO;
import static br.ricardoal.coletorfipezap.coletor.ReaderArquivo.PASTA_ARQUIVOS;

public class ConversorArquivo {

    private static final Date inicioColeta = new GregorianCalendar(2008, Calendar.JANUARY, 1).getTime();

    private final DateFormat dataFormat = new SimpleDateFormat("yyyy-MM");

    public File converter(CidadeType cidadeType) {

        String nomeSaida = "FipeZap" + cidadeType.getSigla() + "_" + dataFormat.format(inicioColeta) + "_" + dataFormat.format(new Date());
        File convertido = new File(PASTA_ARQUIVOS + nomeSaida + ".csv");

        List<String> cabecalho = List.of(
                "Data",
                "Indice de vendas residenciais","Variacao mensal de vendas residenciais","Preco medio de vendas residenciais (R$/m²)",
                "Indice de alugueis residenciais","Variacao mensal de alugueis residenciais","Preco medio de alugueis residenciais (R$/m²)",
                "Rentabilidade dos alugueis residenciais",
                "Indice de vendas comerciais","Variacao mensal de vendas comerciais","Preco medio de vendas comerciais (R$/m²)",
                "Indice de alugueis comerciais","Variacao mensal de alugueis comerciais","Preco medio de alugueis comerciais (R$/m²)",
                "Rentabilidade dos alugueis comerciais"
        );
        int i = 0;

        try(FileInputStream baixado = new FileInputStream(PASTA_ARQUIVOS + ARQUIVO_BAIXADO);
            Workbook workbook = new XSSFWorkbook(baixado);
            PrintWriter pw = new PrintWriter(convertido)) {

            Sheet planilha = workbook.getSheetAt(cidadeType.getIndiceTab());

            List<List<String>> linhas = new ArrayList<>();
            linhas.add(cabecalho);

            for (Row linha : planilha) {
                i++;

                if (linha.getCell(1).getCellType() != CellType.NUMERIC)
                    continue;

                List<String> dados = this.converteLinha(linha);
                linhas.add(dados);
            }

            linhas.stream()
                    .map(l -> convertToCSV(l))
                    .forEach(pw::println);

            return convertido;
        } catch (IOException e) {
            System.out.println("Erro convertendo a linha:" + i);
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.out.println("Erro convertendo o arquivo:" + e);
            //TODO: deletar o arquivo se der erro
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
