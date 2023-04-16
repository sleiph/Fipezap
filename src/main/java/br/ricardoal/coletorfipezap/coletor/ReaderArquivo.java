package br.ricardoal.coletorfipezap.coletor;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReaderArquivo {

    private final String URL_ARQUIVO = "https://downloads.fipe.org.br/indices/fipezap/fipezap-serieshistoricas.xlsx";

    //TODO: fazer o nome variável
    private final String NOME_ARQUIVO = "D:\\repos\\coletorfipezap\\src\\main\\resources\\data\\fipezap_desse-mes.xlsx";

    private String cidade = "";

    public void baixar() {
        try(BufferedInputStream in = new BufferedInputStream(new URL(URL_ARQUIVO).openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(NOME_ARQUIVO)) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.out.println("Erro baixando o arquivo:" + e);
        }
    }

    public Map<Integer, List<String>> converter() {
        Map<Integer, List<String>> data = new HashMap<>();

        try(FileInputStream file = new FileInputStream(new File(NOME_ARQUIVO));
            Workbook workbook = new XSSFWorkbook(file)) {

            Sheet planilha = workbook.getSheetAt(3);
            cidade = planilha.getRow(0).getCell(1).toString();
            int i = 4;

            for (Row linha : planilha) {
                data.put(i, new ArrayList<>());
                for (Cell celula : linha) {
                    switch (celula.getCellType()) {
                        case STRING:
                            System.out.println(celula);
                            break;
                        case NUMERIC:
                            System.out.println(celula);
                            break;
                        case BOOLEAN:
                            System.out.println(celula);
                            break;
                        case FORMULA:
                            System.out.println(celula);
                            break;
                        default:
                            data.get(new Integer(i)).add(" ");
                    }
                }
                i++;
            }
            return data;
        } catch (IOException e) {
            System.out.println("Erro convertendo o arquivo:" + e);
        }
        return Collections.emptyMap();
    }
}
