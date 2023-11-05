package br.ricardoal.coletorfipezap;

import br.ricardoal.coletorfipezap.coletor.ConversorArquivo;
import br.ricardoal.coletorfipezap.coletor.ReaderArquivo;
import br.ricardoal.coletorfipezap.model.Cidade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ColetorfipezapApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColetorfipezapApplication.class, args);

		ReaderArquivo readerArquivo = new ReaderArquivo();
		ConversorArquivo conversorArquivo = new ConversorArquivo();

		readerArquivo.criarDiretorio();
		readerArquivo.baixar();
		conversorArquivo.converter(Cidade.SAOPAULO);
		conversorArquivo.converter(Cidade.GUARULHOS);

	}

}
