package com.example.model;

import java.util.List;

public class EspecificaArchivosRequest {
	
    private String directorio;
    private List<String> nombres;
    
	public String getDirectorio() {
		return directorio;
	}
	public void setDirectorio(String directorio) {
		this.directorio = directorio;
	}
	public List<String> getNombres() {
		return nombres;
	}
	public void setNombres(List<String> nombres) {
		this.nombres = nombres;
	}
}