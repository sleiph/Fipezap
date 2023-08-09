package br.ricardoal.coletorfipezap;

import br.ricardoal.coletorfipezap.coletor.ReaderArquivo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class ColetorfipezapApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColetorfipezapApplication.class, args);

		ReaderArquivo readerArquivo = new ReaderArquivo();
		readerArquivo.baixar();
		File arquivoMes = readerArquivo.converter();
		System.out.println(arquivoMes);

	}

}
