package com.br.microservico.productapi.modules.sales.dto;


import com.br.microservico.productapi.modules.sales.enums.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesConfimationDTO {

    private String saledId;
    private SalesStatus status;
}
