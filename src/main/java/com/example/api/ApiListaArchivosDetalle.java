package com.example.api;

import java.text.SimpleDateFormat;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.*;
import com.example.model.*;

@RestController
public class ApiListaArchivosDetalle {

    @PostMapping("/api/lista/detalle")
    public ResponseEntity<List<String>> listFiles(@RequestBody ListaArchivosRequest request) {
        String directorio = request.getDirectorio();
        String parametro = request.getParametro();
        HttpHeaders headersFolder = new HttpHeaders();

        File folder = new File(directorio);
        if (!folder.exists() || !folder.isDirectory()) {
            headersFolder.add("X-Error-Message", "El directorio especificado no existe o no es válido.");
            return ResponseEntity.badRequest().headers(headersFolder).build();
        } else {
            File[] files = folder.listFiles();
            List<String> matchingFiles = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    if (parametro.equals("*") || file.getName().contains(parametro)) {
                        String detalle = getFormattedFileDetails(file);
                        matchingFiles.add(detalle);
                    }
                }
                return new ResponseEntity<>(matchingFiles, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    private String getFormattedFileDetails(File file) {
        StringBuilder builder = new StringBuilder();
        if (file.isDirectory()) {
            builder.append("d");
        } else {
            builder.append("-");
        }
        builder.append(getPermissionString(file));
        builder.append(" ");
        builder.append(String.format("%-8s", getOwner(file)));
        builder.append(String.format("%-8s", getGroup(file)));
        builder.append(String.format("%-10s", getFormattedFileSize(file.length())));
        builder.append(" ");
        builder.append(new SimpleDateFormat("dd-MM-yyyy").format(new Date(file.lastModified())));
        builder.append(" ");
        builder.append(file.getName());
        builder.append(" ");

        return builder.toString();
    }

    private String getOwner(File file) {
        try {
            FileOwnerAttributeView ownerAttributeView = Files.getFileAttributeView(file.toPath(),
                    FileOwnerAttributeView.class);
            UserPrincipal owner = ownerAttributeView.getOwner();
            return owner.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown Owner";
        }
    }

    private String getGroup(File file) {
        try {
            PosixFileAttributeView posixAttributeView = Files.getFileAttributeView(file.toPath(),
                    PosixFileAttributeView.class);
            GroupPrincipal group = posixAttributeView.readAttributes().group();
            return group.getName();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown Group";
        }
    }

    private String getPermissionString(File file) {
        if (System.getProperty("os.name").contains("Windows")) {
            // Si se ejecuta en Windows, no se obtienen los permisos.
            return "---------";
        }
        try {
            PosixFileAttributes fileAttributes = Files.readAttributes(file.toPath(), PosixFileAttributes.class);
            Set<PosixFilePermission> posixPermissions = fileAttributes.permissions();
            return PosixFilePermissions.toString(posixPermissions);
        } catch (Exception e) {
            e.printStackTrace();
            return "---------";
        }
    }

    private String getFormattedFileSize(long size) {
        String[] units = { "B", "KB", "MB", "GB", "TB" };
        int unitIndex = 0;
        double fileSize = size;

        while (fileSize >= 1024 && unitIndex < units.length - 1) {
            fileSize /= 1024;
            unitIndex++;
        }
        return String.format("%.2f %s", fileSize, units[unitIndex]);
    }
}