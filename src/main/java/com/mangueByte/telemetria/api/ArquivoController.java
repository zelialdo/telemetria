package com.mangueByte.telemetria.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mangueByte.telemetria.services.ArquivoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/arquivo")
@RequiredArgsConstructor
public class ArquivoController {

    private ArquivoService arquivoService;

    @PostMapping("/upload")
    public void upload(@RequestParam("arquivo") MultipartFile arquivo) {
        arquivoService.save(arquivo);
    } 
    
}
