package com.example.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
