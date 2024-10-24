package br.com.microservices.intimacaojudicial.controller;

import br.com.microservices.intimacaojudicial.dto.ProcessoDTO;
import br.com.microservices.intimacaojudicial.dto.ResponseProcessoDTO;
import br.com.microservices.intimacaojudicial.dto.ResponseReuDTO;
import br.com.microservices.intimacaojudicial.dto.ReuDTO;
import br.com.microservices.intimacaojudicial.entity.Processo;
import br.com.microservices.intimacaojudicial.entity.Reu;
import br.com.microservices.intimacaojudicial.service.IntimacaoJudicialService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("intimacao-judicial/processo/api/v1")
public class IntimacaoJudicialController {

    @Autowired
    private IntimacaoJudicialService processoService;

    @PostMapping("/salvar")
    public ResponseEntity<?> salvarProcesso(@RequestBody @Valid ProcessoDTO processoDTO) {
        var processo = new Processo();
        BeanUtils.copyProperties(processoDTO, processo);

        try {
            processoService.criarProcesso(processo);
            return ResponseEntity.ok(processo);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar/{nroProcesso}")
    public ResponseEntity<?> buscarProcesso(@PathVariable String nroProcesso) {
        try {
            var processo = processoService.buscarProcesso(nroProcesso);
            ResponseProcessoDTO responseProcessoDTO = new ResponseProcessoDTO(nroProcesso, processo.getReus());
            return ResponseEntity.ok(responseProcessoDTO);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/excluir/{nroProcesso}")
    public ResponseEntity<?> deletarProcesso(@PathVariable String nroProcesso) {
        try {
            processoService.excluirProcesso(nroProcesso);
            return ResponseEntity.status(HttpStatus.OK).body("Excluido com sucesso");
        }catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/incluirReu/{nroProcesso}")
    public ResponseEntity<?> adicionarReuProcesso(@PathVariable String nroProcesso, @RequestBody @Valid  ReuDTO reuDTO) {
        try {
            var reu = processoService.incluirReu(nroProcesso, reuDTO.nome());
            var responseReuDTO = new ResponseReuDTO(nroProcesso, reu.getNome(), "Réu adicionado com sucesso.");
            return ResponseEntity.ok(responseReuDTO);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
