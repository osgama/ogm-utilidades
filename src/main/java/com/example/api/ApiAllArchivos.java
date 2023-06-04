package com.example.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.example.model.AllArchivosRequest;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class ApiAllArchivos {

    @PostMapping("/api/all")
    public ResponseEntity<StreamingResponseBody> zipFiles(@RequestBody AllArchivosRequest request) {
        String directory = request.getDirectorio();
        String parameter = request.getParametro();
        HttpHeaders headersFolder = new HttpHeaders();

        File folder = new File(directory);
        if (!folder.exists() || !folder.isDirectory()) {
            headersFolder.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ArchivosAll.zip");
            headersFolder.add("X-Error-Message", "El directorio especificado no existe o no es válido.");
            return ResponseEntity.badRequest().headers(headersFolder).build();
        }
        File[] files = folder.listFiles();

        if (files != null) {
            StreamingResponseBody responseBody = outputStream -> {
                try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
                    for (File file : files) {
                        if (parameter.equals("*") || matchesExtension(file.getName(), parameter) || file.getName().contains(parameter)) {
                            addToZip(file, zos);
                        }
                    }
                } catch (Exception e) {
                    throw new IOException("Error creando el archivo zip.", e);
                }
            };
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ArchivosAll.zip");
            return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);   
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private boolean matchesExtension(String fileName, String pattern) {
        String[] parts = pattern.split("\\.");
        if (parts.length == 2 && parts[1].equals("*")) {
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            return fileExtension.equalsIgnoreCase(parts[0]);
        }
        return false;
    }

    private void addToZip(File file, ZipOutputStream zos) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
        fis.close();
    }
}
