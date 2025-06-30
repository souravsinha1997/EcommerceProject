package com.ecommerce.order_service.client.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Address {


    private Integer id;

    private String addressLine;

    private String pin;
}
