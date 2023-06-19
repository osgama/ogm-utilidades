package com.example.api;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public ApiConsulta(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/consulta")
    public List<Map<String, Object>> realizarConsulta(@RequestBody Map<String, String> request) {
        String consulta = request.get("consulta");

        if (!esConsultaSelect(consulta)) {
            throw new IllegalArgumentException("Solo se permiten consultas de selección.");
        }

        return jdbcTemplate.queryForList(consulta);
    }

    private boolean esConsultaSelect(String consulta) {
        return consultaSelectPattern.matcher(consulta).matches();
    }
}

