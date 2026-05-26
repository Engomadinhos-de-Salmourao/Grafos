package scrit.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scrit.project.dto.DestinoRequest;
import scrit.project.dto.DestinoResponse;
import scrit.project.service.DestinoService;

import java.util.List;

@RestController
@RequestMapping("/api/destinos")
@RequiredArgsConstructor
public class DestinoController {

    private final DestinoService destinoService;

    
    @PostMapping("/cadastrar")
    public ResponseEntity<DestinoResponse> cadastrarDestino(@RequestBody DestinoRequest request) {
        DestinoResponse response = destinoService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    
    @GetMapping
    public ResponseEntity<List<DestinoResponse>> listar() {
        return ResponseEntity.ok(destinoService.listarTodos());
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<DestinoResponse> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(destinoService.buscarPorId(id));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDestino(@PathVariable Integer id) {
        destinoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
