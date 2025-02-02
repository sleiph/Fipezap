package br.ricardoal.coletorfipezap;

import br.ricardoal.coletorfipezap.coletor.ConversorArquivo;
import br.ricardoal.coletorfipezap.coletor.ReaderArquivo;
import br.ricardoal.coletorfipezap.model.CidadeType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ColetorfipezapApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColetorfipezapApplication.class, args);

		ReaderArquivo readerArquivo = new ReaderArquivo();
		ConversorArquivo conversorArquivo = new ConversorArquivo();

		//TODO: separar essa parte pra só executar se precisar recoletar
		File arquivoFipeZap = readerArquivo.baixarArquivo();

		List<File> arquivosConvertidos = new ArrayList<>();
		for (CidadeType cidade : CidadeType.values()) {
			File convertido = conversorArquivo.converter(cidade, arquivoFipeZap);
			arquivosConvertidos.add(convertido);
		}
		System.out.println(arquivosConvertidos.size());
	}

}
