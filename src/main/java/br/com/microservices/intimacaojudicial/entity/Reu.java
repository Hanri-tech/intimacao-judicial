package br.com.microservices.intimacaojudicial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "Reu")
@ToString
@Getter
@Setter
public class Reu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "processo_id")
    @JsonIgnore
    private Processo processo;

    public Reu() {
    }


    public Reu(String nome, Processo processo) {
        this.nome = nome;
        this.processo = processo;
    }

}
