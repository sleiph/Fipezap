package br.ricardoal.coletorfipezap.coletor;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TesteConversorArquivo {

    @Spy
    ConversorArquivo spyConversorArquivo;

    AutoCloseable autoCloseable;

    @BeforeEach
    public void init() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void destroy() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testaConverteLinha() throws IOException {

        Workbook arquivo = new XSSFWorkbook("src/test/resources/arquivos/fipezap_desse-mes.xlsx");
        Sheet planilhaSP = arquivo.getSheetAt(3);
        Row jan2018 = planilhaSP.getRow(4);

        List<String> valoresEsperados = List.of(
                "2018-01",
                "203.96565288895","0.00165447928762426","8641.25686656284",
                "137.267825446188","0.00630383511986405","35.715565978464",
                "0.00409336453227626",
                "94.9365127844187","0.00692546938218916","10009.2076227462",
                "90.4524097717629","0.00129231095896456","43.0726715225146",
                "0.00456113749214862"
        );
        List<String> valoresRetornados = spyConversorArquivo.converteLinha(jan2018);
        assertEquals(valoresEsperados, valoresRetornados);

    }

}