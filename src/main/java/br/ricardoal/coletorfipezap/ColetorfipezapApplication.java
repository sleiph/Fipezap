package br.ricardoal.coletorfipezap;

import br.ricardoal.coletorfipezap.service.FipeZapService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ColetorfipezapApplication implements CommandLineRunner {

	private final FipeZapService fipeZapService;
	private final ApplicationContext contexto;

	public ColetorfipezapApplication(FipeZapService fipeZapService, ApplicationContext contexto) {
		this.fipeZapService = fipeZapService;
		this.contexto = contexto;
	}

	public static void main(String[] args) {
		SpringApplication.run(ColetorfipezapApplication.class, args);
	}

	@Override
	public void run(String... args) {
		fipeZapService.executarColeta();
		System.exit(SpringApplication.exit(contexto, () -> 0));
	}

}
