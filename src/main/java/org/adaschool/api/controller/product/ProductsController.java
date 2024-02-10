package org.adaschool.api.controller.product;

import org.adaschool.api.exception.ProductNotFoundException;
import org.adaschool.api.repository.product.Product;
import org.adaschool.api.repository.product.ProductDto;
import org.adaschool.api.repository.user.UserDto;
import org.adaschool.api.service.product.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/products/")
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(@Autowired ProductsService productsService) {
        this.productsService = productsService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct() {
        Product product = new Product();
        Product procuctSave = productsService.save(product);
        URI createdProductUri = URI.create("/v1/products/" + procuctSave);
        return ResponseEntity.created(createdProductUri).body(null);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productsService.all();
        return ResponseEntity.ok(products);
    }

    @GetMapping("{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") String id) {
        Optional<Product> product = productsService.findById(id);
        if (product.isPresent()){
            return ResponseEntity.ok(product.get());
        }
        else{
            throw new ProductNotFoundException(id);
        }

    }

    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") String id, @RequestBody ProductDto productDto) {
        Optional<Product> productOptional = productsService.findById(id);
        if (productOptional.isPresent()){
            Product product = productOptional.get();
            product.update(productDto);
            Product productSaved = productsService.save(product);
            return ResponseEntity.ok(productSaved);
        }
        else{
            throw new ProductNotFoundException(id);
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        Optional<Product> productOptional = productsService.findById(id);
        if (productOptional.isPresent()){
            productsService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        else{
            throw new ProductNotFoundException(id);
        }
    }
}
