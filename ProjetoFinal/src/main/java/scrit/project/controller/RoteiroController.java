package scrit.project.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scrit.project.dto.RoteiroRequest;
import scrit.project.dto.RoteiroResponse;
import scrit.project.service.RoteiroService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/roteiros")
@RequiredArgsConstructor
public class RoteiroController {

    private final RoteiroService roteiroService;

    
    @PostMapping("/gerar")
    public ResponseEntity<RoteiroResponse> gerar(@RequestBody RoteiroRequest request)
            throws IOException {
        RoteiroResponse response = roteiroService.gerar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<RoteiroResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(roteiroService.buscarPorId(id));
    }

    
    @GetMapping
    public ResponseEntity<List<RoteiroResponse>> listar(@RequestParam Integer destinoId) {
        return ResponseEntity.ok(roteiroService.listarPorDestino(destinoId));
    }
}
