package br.com.microservices.intimacaojudicial.service;

import br.com.microservices.intimacaojudicial.entity.Processo;
import br.com.microservices.intimacaojudicial.entity.Reu;
import br.com.microservices.intimacaojudicial.repository.ProcessoRepository;
import br.com.microservices.intimacaojudicial.repository.ReuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class IntimacaoJudicialService {

    @Autowired
    ProcessoRepository processoRepository;
    @Autowired
    ReuRepository reuRepository;

    public void criarProcesso(Processo processo) {

        var existeNroProcesso = processoRepository.existsBynroProcesso(processo.getNroProcesso());

        if (existeNroProcesso) {
            throw new IllegalArgumentException("Número de processo já cadastrado.");
        }

        processoRepository.save(processo);
    }

    public Processo buscarProcesso(String nroProcesso) {

        var processo = processoRepository.findBynroProcesso(nroProcesso);

        if (processo == null) {
            throw new IllegalArgumentException("Número do processo não encontrado.");
        }
        return processo;
    }

    public void excluirProcesso(String nroProcesso) {

        var processo = processoRepository.findBynroProcesso(nroProcesso);

        if (processo == null) {
            throw new IllegalArgumentException("Número do processo não existe.");
        }

        processoRepository.delete(processo);
    }

    public Reu incluirReu(String nroProcesso, String nome) {

        var processo = processoRepository.findBynroProcesso(nroProcesso);

        if (processo == null) {
            throw new IllegalArgumentException("Não foi póssivel incluir o réu. Número de processo não encontrado.");
        }

        boolean reuExiste = processo.getReus().stream().anyMatch(reu -> reu.getNome().equalsIgnoreCase(nome));

        if (reuExiste) {
            throw new IllegalArgumentException("Já existe um réu cadastrado com o nome: " + nome);
        }
        var reu = new Reu(nome, processo);
        return reuRepository.save(reu);

    }
}
