package fr.agilit.contrat.entities;


import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class BaseEntityClass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
