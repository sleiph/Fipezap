package br.ricardoal.coletorfipezap;

import br.ricardoal.coletorfipezap.coletor.ConversorArquivo;
import br.ricardoal.coletorfipezap.coletor.ReaderArquivo;
import br.ricardoal.coletorfipezap.model.CidadeType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ColetorfipezapApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColetorfipezapApplication.class, args);

		ReaderArquivo readerArquivo = new ReaderArquivo();
		ConversorArquivo conversorArquivo = new ConversorArquivo();

		//TODO: separar essa parte pra só executar se precisar recoletar
		readerArquivo.criarDiretorio();
		readerArquivo.baixar();

		for (CidadeType cidade : CidadeType.values()) {
			conversorArquivo.converter(cidade);
		}

	}

}
