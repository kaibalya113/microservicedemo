package com.product.productservice.service;

import com.product.productservice.dto.ProductRequest;
import com.product.productservice.dto.ProductResponse;
import com.product.productservice.model.Product;
import com.product.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void storeProduct(ProductRequest product){
        if(product != null){
            Product product1 = Product.builder()
                            .name(product.getName())
                                    .description(product.getDescription())
                                            .price(product.getPrice())
                                                    .build();

            productRepository.save(product1);
            log.info("Product {} is saved", product1.getName());
        }
    }

    public List<ProductResponse> getProductList() {
        List<Product> products = productRepository.findAll  ();
        return products.stream().map(this:: mapTpProductResponse).toList();
    }

    private ProductResponse mapTpProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice()).build();
    }
}
