package br.ricardoal.coletorfipezap;

import br.ricardoal.coletorfipezap.coletor.ReaderArquivo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ColetorfipezapApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColetorfipezapApplication.class, args);

		ReaderArquivo readerArquivo = new ReaderArquivo();
		//readerArquivo.baixar();
		readerArquivo.converter();
	}

}
