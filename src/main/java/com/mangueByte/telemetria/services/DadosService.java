package com.mangueByte.telemetria.services;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mangueByte.telemetria.domain.model.Dados;
import com.mangueByte.telemetria.repository.DadosRepository;

@Service
public class DadosService {

    @Autowired
    private DadosRepository dadosRepository;

    public List<Dados> findAll() {
        var result = dadosRepository.findAll();
        return result;
    }

}
