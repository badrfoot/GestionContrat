package fr.agilit.contrat.entities;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
