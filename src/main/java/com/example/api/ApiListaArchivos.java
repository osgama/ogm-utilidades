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
        HttpHeaders headersFolder = new HttpHeaders();

        File folder = new File(directorio);
        if (!folder.exists() || !folder.isDirectory()) {
            headersFolder.add("X-Error-Message", "El directorio especificado no existe o no es válido.");
            return ResponseEntity.badRequest().headers(headersFolder).build();
        }else{
            File[] files = folder.listFiles();
            List<String> matchingFiles = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    if (parametro.equals("*") || file.getName().contains(parametro)) {
                        matchingFiles.add(file.getName());
                    }
                }
                return new ResponseEntity<>(matchingFiles, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}