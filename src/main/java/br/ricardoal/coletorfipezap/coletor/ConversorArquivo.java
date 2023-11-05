package br.ricardoal.coletorfipezap.coletor;

import br.ricardoal.coletorfipezap.model.Cidade;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static br.ricardoal.coletorfipezap.coletor.ReaderArquivo.ARQUIVO_BAIXADO;
import static br.ricardoal.coletorfipezap.coletor.ReaderArquivo.PASTA_ARQUIVOS;

public class ConversorArquivo {

    private final DateFormat dataFormat = new SimpleDateFormat("yyyy-MM");

    public File converter(Cidade cidade) {

        File convertido = new File(PASTA_ARQUIVOS + "fipezap_" + dataFormat.format(new Date()) + "_" + cidade.getNomeArquivo() + ".csv");

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

            Sheet planilha = workbook.getSheetAt(cidade.getIndiceTab());
            String nomeCidade = planilha.getRow(0).getCell(1).toString(); //TODO: fazer algo com isso

            List<List<String>> linhas = new ArrayList<>();
            linhas.add(cabecalho);

            for (Row linha : planilha) {
                i++;

                if (linha.getCell(1).getCellType() != CellType.NUMERIC)
                    continue;

                List<String> dados = new ArrayList<>();

                Date data = linha.getCell(1).getDateCellValue();
                dados.add(dataFormat.format(data));

                dados.add(trataCelula(linha.getCell(2)));
                dados.add(trataCelula(linha.getCell(7)));
                dados.add(trataCelula(linha.getCell(17)));

                dados.add(trataCelula(linha.getCell(22)));
                dados.add(trataCelula(linha.getCell(27)));
                dados.add(trataCelula(linha.getCell(37)));

                dados.add(trataCelula(linha.getCell(42)));

                dados.add(trataCelula(linha.getCell(47)));
                dados.add(trataCelula(linha.getCell(48)));
                dados.add(trataCelula(linha.getCell(50)));

                dados.add(trataCelula(linha.getCell(51)));
                dados.add(trataCelula(linha.getCell(52)));
                dados.add(trataCelula(linha.getCell(54)));

                dados.add(trataCelula(linha.getCell(55)));

                linhas.add(dados);

            }

            //TODO: separar essa parte
            linhas.stream()
                    .map(this::convertToCSV)
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
