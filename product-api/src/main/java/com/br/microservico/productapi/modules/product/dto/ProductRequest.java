package com.br.microservico.productapi.modules.product.dto;

import com.br.microservico.productapi.modules.category.dto.CategoryResponse;
import com.br.microservico.productapi.modules.supplier.dto.SupplierResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    @JsonProperty("quantity_available")
    private Integer quantityAvailable;
    private Integer supplierId;
    private Integer categoryId;
}
