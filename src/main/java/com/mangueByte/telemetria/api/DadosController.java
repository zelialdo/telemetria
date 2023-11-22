package com.mangueByte.telemetria.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mangueByte.telemetria.domain.model.Dados;
import com.mangueByte.telemetria.repository.DadosRepository;
import com.mangueByte.telemetria.services.DadosService;


@SpringBootApplication
@RestController
@RequestMapping(value = "/dados")
public class DadosController {
    
    @Autowired
    private DadosService dadosService;
    private DadosRepository dadosRepository;

    @GetMapping
    public List<Dados> findAll() {
        List<Dados> result = dadosService.findAll();
        return result;
    }

    @PostMapping
    public ResponseEntity<String> processarArquivoTxt(@RequestPart("file") MultipartFile file) {
        try {
            if (!file.getOriginalFilename().endsWith(".txt")) {
                return new ResponseEntity<>("O arquivo fornecido não é um arquivo de texto (.txt)", HttpStatus.BAD_REQUEST);
            }

            String conteudo = new BufferedReader(new InputStreamReader(file.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // Processar o conteúdo do arquivo e salvar no banco de dados
            processarESalvarDados(conteudo);

            return new ResponseEntity<>("Dados processados e salvos com sucesso.", HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>("Erro ao processar o arquivo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void processarESalvarDados(String conteudo) {
        // Usar expressões regulares para encontrar padrões no conteúdo do arquivo
        Pattern pattern = Pattern.compile("(\\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\s+(\\d+)");
        Matcher matcher = pattern.matcher(conteudo);
    
        List<Dados> dadosList = new ArrayList<>();
    
        int curvaEsquerdaCount = 0;
        int curvaDireitaCount = 0;
    
        while (matcher.find()) {
            int valor = Integer.parseInt(matcher.group(2));
    
            // Verificar se o valor está na faixa de curva para esquerda ou direita
            if (valor >= 0 && valor <= 3500) {
                curvaEsquerdaCount++;
            } else if (valor >= 3501 && valor <= 7000) {
                curvaDireitaCount++;
            }
        }
    
        // Calcular o total de curvas
        int totalCurvas = curvaEsquerdaCount + curvaDireitaCount;
    
        // Criar um objeto Dados com as contagens
        Dados dados = new Dados(curvaEsquerdaCount, curvaDireitaCount, totalCurvas);
    
        // Adicionar o objeto Dados à lista
        dadosList.add(dados);
    
        // Salvar no banco de dados
        dadosRepository.saveAll(dadosList);
    }
    
}
