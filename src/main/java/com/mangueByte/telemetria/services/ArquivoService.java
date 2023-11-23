package com.mangueByte.telemetria.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface ArquivoService {

    void save(MultipartFile arquivo);
    
}
