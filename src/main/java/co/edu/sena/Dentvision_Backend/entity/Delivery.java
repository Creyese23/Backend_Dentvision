package co.edu.sena.Dentvision_Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "entregas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;
/*
    @Column(nullable = false, length = 50)
    private String estado;
    @Column(length = 500)
    private String observaciones;
*/
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_orden", nullable = false)
    private OrderEntity orden;
}

