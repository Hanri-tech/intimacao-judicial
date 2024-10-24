package br.com.microservices.intimacaojudicial.repository;

import br.com.microservices.intimacaojudicial.entity.Processo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessoRepository extends JpaRepository<Processo, Integer> {

    boolean existsBynroProcesso ( String nroProcesso);

    Processo findBynroProcesso ( String nroProcesso);
}
