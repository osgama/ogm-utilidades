package com.example.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.File;
import java.io.FileInputStream;
import com.example.model.EspecificaArchivosRequest;

@RestController
public class ApiEspecificaArchivos {

    @PostMapping("/api/especifica")
    public ResponseEntity<byte[]> zipSelectedFiles(@RequestBody EspecificaArchivosRequest request) {
        String directorio = request.getDirectorio();
        List<String> nombres = request.getNombres();
        HttpHeaders headersFolder = new HttpHeaders();
        HttpHeaders headersTamaño = new HttpHeaders();
        
        File folder = new File(directorio);
        if (!folder.exists() || !folder.isDirectory()) {
            headersFolder.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Archivos.zip");
            headersFolder.add("X-Error-Message", "El directorio especificado no existe o no es válido.");
            return ResponseEntity.badRequest().headers(headersFolder).build();
        }

        File[] files = folder.listFiles();

        if (files != null) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 ZipOutputStream zos = new ZipOutputStream(baos)) {
                for (File file : files) {
                    String nombreArchivo = file.getName();
                    String nombreArchivoSinExtension = nombreArchivo.substring(0, nombreArchivo.lastIndexOf('.'));
                    if (nombres.contains(nombreArchivo) || nombres.contains(nombreArchivoSinExtension)) {
                        addToZip(file, zos);
                    }
                }
                zos.finish();
                zos.close();
                byte[] zipBytes = baos.toByteArray();
                int maxZipSize = 1024 * 1024 * 1024; // 1gb como límite de tamaño
                if (zipBytes.length > maxZipSize) {
                	
                    headersTamaño.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Archivos.zip");
                    headersTamaño.add("X-Error-Message", "El tamaño del archivo ZIP excede el límite de 1GB permitido.");
                    return ResponseEntity.badRequest().headers(headersTamaño).build();
                }

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ArchivosEspe.zip");

                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(zipBytes.length)
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(zipBytes);

            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
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