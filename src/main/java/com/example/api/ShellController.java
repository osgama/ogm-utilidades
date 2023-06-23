package com.example.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api")
public class ShellController {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @GetMapping("/execute/stream/{param1}/{param2}/{param3}")
    public ResponseBodyEmitter executeShellScriptStream(@PathVariable String param1, @PathVariable String param2, @PathVariable String param3) {
        ResponseBodyEmitter emitter = new SseEmitter();

        executorService.execute(() -> {
            try {
                String[] command = {"/bin/bash", "-c", "path/to/your/script.sh " + param1 + " " + param2 + " " + param3};
                Process process = Runtime.getRuntime().exec(command);
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    emitter.send(line, MediaType.TEXT_PLAIN);
                }

                process.waitFor();
                reader.close();
                emitter.complete();
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }
        });

        return emitter;
    }
}
