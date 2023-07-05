package com.example.api;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class ApiConsulta {
    private final JdbcTemplate jdbcTemplate;
    private final Pattern consultaSelectPattern = Pattern.compile("^(?i)SELECT.*$");

    public ApiConsulta(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/consulta")
    public List<Map<String, Object>> realizarConsulta(@RequestBody Map<String, Object> request) {
        String consulta = (String) request.get("consulta");
        int pagina = (int) request.getOrDefault("pagina", 1);
        int elementosPorPagina = (int) request.getOrDefault("elementosPorPagina", 100);

        if (!esConsultaSelect(consulta)) {
            throw new IllegalArgumentException("Solo se permiten consultas de selección.");
        }
        int offset = (pagina - 1) * elementosPorPagina;
        String consultaPaginada = consulta + " LIMIT " + elementosPorPagina + " OFFSET " + offset;

        return jdbcTemplate.queryForList(consultaPaginada);
    }

    private boolean esConsultaSelect(String consulta) {
        return consultaSelectPattern.matcher(consulta).matches();
    }
}
