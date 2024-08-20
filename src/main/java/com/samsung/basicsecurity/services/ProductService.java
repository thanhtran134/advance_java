package com.samsung.basicsecurity.services;

import com.samsung.basicsecurity.repositories.ProductRepository;
import com.samsung.basicsecurity.repositories.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    public List<Product> getProductsByCatalogId(Long catalogId){
        return productRepository.findProductByCatalog_Id(catalogId);
    }

    public List<Product> searchProductsByName(String name){
        return productRepository.findProductByNameContaining(name);
    }

    public void saveAll(List<Product> products){
        productRepository.saveAll(products);
    }
}
