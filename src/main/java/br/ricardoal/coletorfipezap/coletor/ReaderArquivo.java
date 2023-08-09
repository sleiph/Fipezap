package br.ricardoal.coletorfipezap.coletor;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReaderArquivo {

    private final String URL_ARQUIVO = "https://downloads.fipe.org.br/indices/fipezap/fipezap-serieshistoricas.xlsx";

    private final String PASTA_ARQUIVOS = "src\\main\\resources\\arquivos\\";
    private final String ARQUIVO_BAIXADO = "fipezap_desse-mes.xlsx";
    private final String ARQUIVO_CONVERTIDO = "fipezap_desse-mes.csv";

    private String cidade;

    DateFormat dataFormat = new SimpleDateFormat("yyyy-MM");

    public void criarDiretorio() {
        try {
            Files.createDirectories(Paths.get(System.getProperty("user.dir") + "\\" + PASTA_ARQUIVOS));
        } catch (IOException e) {
            System.out.println("Erro criando o diretorio para download:" + e);
            throw new RuntimeException(e);
        }
    }

    public void baixar() {

        try(BufferedInputStream in = new BufferedInputStream(new URL(URL_ARQUIVO).openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(PASTA_ARQUIVOS + ARQUIVO_BAIXADO)) {



            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.out.println("Erro baixando o arquivo:" + e);
            //TODO: deletar o arquivo se der erro
            throw new RuntimeException(e);
        }
    }

    public File converter() {

        File convertido = new File(PASTA_ARQUIVOS + ARQUIVO_CONVERTIDO);

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

            Sheet planilha = workbook.getSheetAt(3);
            cidade = planilha.getRow(0).getCell(1).toString();

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
