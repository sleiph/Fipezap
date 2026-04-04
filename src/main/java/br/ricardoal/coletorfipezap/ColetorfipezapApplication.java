package br.ricardoal.coletorfipezap;

import br.ricardoal.coletorfipezap.service.FipeZapService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ColetorfipezapApplication implements CommandLineRunner {

	private final FipeZapService fipeZapService;

	public ColetorfipezapApplication(FipeZapService fipeZapService) {
		this.fipeZapService = fipeZapService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ColetorfipezapApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fipeZapService.executarColeta();
	}

}
