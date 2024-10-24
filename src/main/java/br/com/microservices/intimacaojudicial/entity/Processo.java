package br.com.microservices.intimacaojudicial.entity;

import jakarta.persistence.*;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Processo")
@ToString
public class Processo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nroProcesso;

    @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reu> reus = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNroProcesso() {
        return nroProcesso;
    }

    public void setNroProcesso(String nroProcesso) {
        this.nroProcesso = nroProcesso;
    }

    public List<Reu> getReus() {
        return reus;
    }

    public void setReus(List<Reu> reus) {
        this.reus = reus;
    }
}
