package com.br.microservico.productapi.modules.supplier.service;

import com.br.microservico.productapi.config.exception.SuccessResponse;
import com.br.microservico.productapi.config.exception.ValidationExcpetion;
import com.br.microservico.productapi.modules.category.dto.CategoryResponse;
import com.br.microservico.productapi.modules.category.model.Category;
import com.br.microservico.productapi.modules.product.service.ProductService;
import com.br.microservico.productapi.modules.supplier.dto.SupplierRequest;
import com.br.microservico.productapi.modules.supplier.dto.SupplierResponse;
import com.br.microservico.productapi.modules.supplier.model.Supplier;
import com.br.microservico.productapi.modules.supplier.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductService productService;

    public List<SupplierResponse> findAll(){

        return supplierRepository
                .findAll()
                .stream()
                .map(SupplierResponse::of)
                .collect(Collectors.toList());
    }

    public List<SupplierResponse> findByName(String name){
        if(isEmpty(name)){
            throw new ValidationExcpetion("The supplier name must be informed.");
        }
        return supplierRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(SupplierResponse::of)
                .collect(Collectors.toList());
    }

    public SupplierResponse findByIdResponse(Integer id){
        if(isEmpty(id)){
            throw new ValidationExcpetion("The supplier id must be informed.");
        }
        return SupplierResponse.of(findById(id));
    }

    public Supplier findById(Integer id){
        return supplierRepository
                .findById(id)
                .orElseThrow(() -> new ValidationExcpetion("There's no supplier for the giver ID"));
    }

    public SupplierResponse save(SupplierRequest request){
        validateSupplierNameInformed(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    private void validateSupplierNameInformed(SupplierRequest request){
        if(isEmpty(request.getName())){
            throw new ValidationExcpetion("The supplier's name was not informed.");
        }
    }

    public SupplierResponse update(SupplierRequest request, Integer id) {
        validateSupplierNameInformed(request);
        validateInformedId(id);

        var supplier = Supplier.of(request);
        supplier.setId(id);

        supplierRepository.save(supplier);
        return SupplierResponse.of(supplier);
    }

    public SuccessResponse delete(Integer id) {
        validateInformedId(id);
        if (productService.existsBySupplierId(id)) {
            throw new ValidationExcpetion("You cannot delete this supplier because it's already defined by a product");
        }
        supplierRepository.deleteById(id);
        return SuccessResponse.create("The supplier was deleted.");
    }

    public void validateInformedId(Integer id){
        if(isEmpty(id))
            throw new ValidationExcpetion("The supplier ID must be informed.");
    }
}
