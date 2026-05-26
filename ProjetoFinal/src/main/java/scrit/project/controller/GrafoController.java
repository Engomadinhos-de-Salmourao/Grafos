package scrit.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scrit.project.dto.ArestaRequest;
import scrit.project.dto.LugarRequest;
import scrit.project.service.GrafoService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/grafo")
@RequiredArgsConstructor
public class GrafoController {

    private final GrafoService grafoService;

    
    @GetMapping("/show")
    public ResponseEntity<Map<String, Object>> show() throws IOException {
        var grafo = grafoService.obterGrafo();
        grafoService.showGrafo();
        return ResponseEntity.ok(Map.of(
                "vertices", grafo.getN(),
                "arestas", grafo.getM() / 2,
                "conexo", grafoService.conexidade()
        ));
    }

    
    @GetMapping("/conexidade")
    public ResponseEntity<Map<String, String>> getConexidade() throws IOException {
        return ResponseEntity.ok(Map.of("resultado", grafoService.conexidade()));
    }

    
    @PostMapping("/lugar")
    public ResponseEntity<Map<String, String>> adicionarLugar(
            @RequestBody LugarRequest request) throws IOException {
        grafoService.adicionarLugar(request.toLugar());
        return ResponseEntity.ok(Map.of("mensagem", "Lugar adicionado ao grafo."));
    }

    
    @DeleteMapping("/lugar/{id}")
    public ResponseEntity<Map<String, String>> deletarLugar(@PathVariable int id) throws IOException {
        grafoService.removerLugar(id);
        return ResponseEntity.ok(Map.of("mensagem", "Lugar id=" + id + " removido do grafo."));
    }

    
    @PostMapping("/aresta")
    public ResponseEntity<Map<String, String>> adicionarAresta(
            @RequestBody ArestaRequest request) throws IOException {
        grafoService.adicionarAresta(request.getV(), request.getW(), request.getDist(), request.getTempo());
        return ResponseEntity.ok(Map.of("mensagem",
                "Aresta adicionada entre " + request.getV() + " e " + request.getW()));
    }

    
    @DeleteMapping("/aresta")
    public ResponseEntity<Map<String, String>> deletarAresta(
            @RequestParam Integer v, @RequestParam Integer w) throws IOException {
        grafoService.removerAresta(v, w);
        return ResponseEntity.ok(Map.of("mensagem", "Aresta entre " + v + " e " + w + " removida."));
    }

    
    @GetMapping("/arquivo")
    public ResponseEntity<String> mostrarConteudoArquivo() throws IOException {
        return ResponseEntity.ok(grafoService.mostrarConteudoArquivo());
    }

    
    @PostMapping("/salvar")
    public ResponseEntity<Map<String, String>> salvar() throws IOException {
        grafoService.gravarGrafo();
        return ResponseEntity.ok(Map.of("mensagem", "Grafo salvo em grafo.txt com sucesso."));
    }

    
    @PostMapping("/carregar")
    public ResponseEntity<Map<String, Object>> carregar() throws IOException {
        var grafo = grafoService.carregarGrafo();
        return ResponseEntity.ok(Map.of(
                "mensagem", "Grafo recarregado do arquivo.",
                "vertices", grafo.getN(),
                "arestas", grafo.getM() / 2
        ));
    }
}
