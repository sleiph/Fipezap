package br.ricardoal.coletorfipezap.service;

import br.ricardoal.coletorfipezap.coletor.ConversorArquivo;
import br.ricardoal.coletorfipezap.coletor.ReaderArquivo;
import br.ricardoal.coletorfipezap.model.CidadeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class FipeZapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FipeZapService.class);

    private final ReaderArquivo readerArquivo;
    private final ConversorArquivo conversorArquivo;

    public FipeZapService(ReaderArquivo readerArquivo, ConversorArquivo conversorArquivo) {
        this.readerArquivo = readerArquivo;
        this.conversorArquivo = conversorArquivo;
    }

    public void executarColeta() {

        //TODO: separar essa parte pra só executar se precisar recoletar
        File arquivoFipeZap = readerArquivo.baixarArquivo();

        List<File> arquivosConvertidos = new ArrayList<>();
        for (CidadeType cidade : CidadeType.values()) {
            if (!cidade.isColetada()) continue;

            File convertido = conversorArquivo.converter(cidade, arquivoFipeZap);
            arquivosConvertidos.add(convertido);
        }
        LOGGER.info("Arquivos coletados: {}", arquivosConvertidos.size());
    }

}
