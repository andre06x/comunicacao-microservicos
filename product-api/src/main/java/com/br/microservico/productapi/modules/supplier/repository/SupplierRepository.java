package com.br.microservico.productapi.modules.supplier.repository;

import com.br.microservico.productapi.modules.category.model.Category;
import com.br.microservico.productapi.modules.supplier.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    List<Supplier> findByNameIgnoreCaseContaining(String name);
}
