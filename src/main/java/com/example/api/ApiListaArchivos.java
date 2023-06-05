package com.example.api;

import java.io.*;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.model.*;

@RestController
public class ApiListaArchivos {

    @PostMapping("/api/lista")
    public ResponseEntity<List<String>> listFiles(@RequestBody ListaArchivosRequest request) {
        String directorio = request.getDirectorio();
        String parametro = request.getParametro();

        File folder = new File(directorio);
        File[] files = folder.listFiles();
        List<String> matchingFiles = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (parametro.equals("*") || file.getName().contains(parametro)) {
                    matchingFiles.add(file.getName());
                }
            }
        }
        return new ResponseEntity<>(matchingFiles, HttpStatus.OK);
    }
}