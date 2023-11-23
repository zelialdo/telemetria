package com.mangueByte.telemetria.services;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ArquivoServiceImplementacao implements ArquivoService {

    @Value("${file.path}")
    private String filePath;

    @Override
    public void save(MultipartFile arquivo) {
        String dir = System.getProperty("user.dir") + "/" + filePath;
        try {
            arquivo.transferTo(new File(dir + arquivo.getOriginalFilename()));
        } catch (IOException e) {
            System.out.println(e.getMessage());;
        }
    }
}