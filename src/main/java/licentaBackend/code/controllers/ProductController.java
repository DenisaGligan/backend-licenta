package licentaBackend.code.controllers;


import licentaBackend.code.Security.Services.ProductService;
import licentaBackend.code.models.Product;
import licentaBackend.code.payload.response.MessageResponse;
import licentaBackend.code.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    //get all products
    @GetMapping("/products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false) String name){
        try{
            List<Product> products = new ArrayList<>();

            if( name == null) {
                products = productService.getAllProducts();
            }
            else{ products = productService.getByName(name);}

            if(products.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return  new ResponseEntity<>(products, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //get fruits
    @GetMapping("/products/fruits")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Product>> findFruits(){

        try{
            List<Product> productsType= productService.getFruits();

            if(productsType.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productsType, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get vegetables
    //get fruits
    @GetMapping("/products/vegetables")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<Product>> findVegetables(){

        try{
            List<Product> productsType= productService.getVegetables();

            if(productsType.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productsType, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get products by type
    @GetMapping("/productsby/{type}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Product>> findByType(@PathVariable("type") String type){

        try{
            List<Product> productsType= productService.getAllByType(type);

            if(productsType.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productsType, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/name")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Product>> findByName(@RequestBody DTO dto){

        try{
            List<Product> productsType= productService.getProductsByNameOrType(dto);

            if(productsType.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productsType, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //get product by id
    @GetMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> getProductById(@PathVariable("id") long id){

        try {
            Product productData = productService.getProductById(id);
            return  new ResponseEntity<Product>(productData, HttpStatus.OK);
        }
        catch(Exception e) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //create product
    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProduct(@RequestBody Product product){

        List<Product> product1=productService.getByName(product.getName());
        if(product1.size() != 0) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(" This name of product is already in database!"));
        }
        if(!productService.validatePrice(product)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(" Price must be grater than 0!"));
        }
        try{
            Product _product = productService.createProduct(product);
            return new ResponseEntity<>(_product,HttpStatus.CREATED);
        } catch(Exception e) {

            return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    //update product price
    @PutMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProductPriceImgUrl(@PathVariable("id") long id, @RequestBody Product p){

        if(!productService.validatePrice(p)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(" Price must be grater than 0!"));
        }
        try{
            Product _product = productService.updateProductPriceImgUrl(id,p);
            return new ResponseEntity<>(_product,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") long id){
        try{
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

}
