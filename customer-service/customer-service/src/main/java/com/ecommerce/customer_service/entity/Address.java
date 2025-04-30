package com.ecommerce.customer_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "address_line", nullable = false)
    private String addressLine;

    @Column(nullable = false)
    private String pin;
}
