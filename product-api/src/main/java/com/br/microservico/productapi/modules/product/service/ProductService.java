package com.br.microservico.productapi.modules.product.service;

import com.br.microservico.productapi.config.exception.SuccessResponse;
import com.br.microservico.productapi.config.exception.ValidationExcpetion;

import com.br.microservico.productapi.modules.category.service.CategoryService;
import com.br.microservico.productapi.modules.product.dto.ProductRequest;
import com.br.microservico.productapi.modules.product.dto.ProductResponse;
import com.br.microservico.productapi.modules.product.model.Product;
import com.br.microservico.productapi.modules.product.repository.ProductRepository;

import com.br.microservico.productapi.modules.supplier.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ProductService {

    private static final Integer ZERO = 0;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CategoryService categoryService;

    public ProductResponse save(ProductRequest request) {
        validateProductDataInformed(request);
        validateCategoryAndSupplierInformed(request);

        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());

        var product = productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }

    public ProductResponse update(ProductRequest request, Integer id) {
        validateProductDataInformed(request);
        validateCategoryAndSupplierInformed(request);
        validateInformedId(id);

        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());

        var product = Product.of(request, supplier, category);
        product.setId(id);

        productRepository.save(product);
        return ProductResponse.of(product);
    }

    private void validateProductDataInformed(ProductRequest request) {
        if (isEmpty(request.getName())) {
            throw new ValidationExcpetion("The product's name was not informed.");
        }

        if (isEmpty(request.getQuantityAvailable())) {
            throw new ValidationExcpetion("The product's quantity was not informed");
        }

        if (request.getQuantityAvailable() <= ZERO) {
            throw new ValidationExcpetion("The quantity should not be less or equal to zero");
        }
    }

    private void validateCategoryAndSupplierInformed(ProductRequest request) {
        if (isEmpty(request.getCategoryId())) {
            throw new ValidationExcpetion("The category ID was not informed.");
        }

        if (isEmpty(request.getSupplierId())) {
            //System.out.println(request);
            throw new ValidationExcpetion("The supplier ID was not informed.");
        }
    }


    public List<ProductResponse> findAll() {

        return productRepository
                .findAll()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByName(String name) {
        if (isEmpty(name)) {
            throw new ValidationExcpetion("The product name must be informed.");
        }
        return productRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId) {
        if (isEmpty(supplierId)) {
            throw new ValidationExcpetion("The supplier name must be informed.");
        }
        return productRepository
                .findBySupplierId(supplierId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId) {
        if (isEmpty(categoryId)) {
            throw new ValidationExcpetion("The category name must be informed.");
        }
        return productRepository
                .findByCategoryId(categoryId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public ProductResponse findByIdResponse(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationExcpetion("The supplier id must be informed.");
        }
        return ProductResponse.of(findById(id));
    }

    public Product findById(Integer id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ValidationExcpetion("There's no supplier for the giver ID"));
    }
//
    public Boolean existsByCategoryId(Integer categoryId){
        return productRepository.existsByCategoryId(categoryId);
    }

    public Boolean existsBySupplierId(Integer supplierId){
        return productRepository.existsBySupplierId(supplierId);
    }

    public SuccessResponse delete(Integer id) {
        validateInformedId(id);
        findById(id);

        productRepository.deleteById(id);
        return SuccessResponse.create("The supplier was deleted.");
    }

    public void validateInformedId(Integer id) {
        if (isEmpty(id)) {
            throw new ValidationExcpetion("The supplier ID must be informed.");
        }
    }

}
