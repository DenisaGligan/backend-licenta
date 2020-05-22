package licentaBackend.code.Security.Services;

import licentaBackend.code.controllers.DTO;
import licentaBackend.code.models.Product;
import licentaBackend.code.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;


    //get all products
    public List<Product> getAllProducts(){
        List<Product> products = new ArrayList<>();

        productRepository.findAll().forEach(products::add);

        return products;
    }

    //get all products by type
    public List<Product> getAllByType(String type)
    {
        List<Product> productsType= new ArrayList<>();
        List<Product> products = getAllProducts();

        productsType = products.stream().filter(p-> p.getType().equals(type)).collect(Collectors.toList());

        return productsType;
    }

    //get fruits
    public List<Product> getFruits()
    {
        List<Product> productsType= new ArrayList<>();
        List<Product> products = getAllProducts();

        productsType = products.stream().filter(p-> p.getType().equals("fruits")).collect(Collectors.toList());

        return productsType;
    }

    //get vegetables
    public List<Product> getVegetables()
    {
        List<Product> productsType= new ArrayList<>();
        List<Product> products = getAllProducts();

        productsType = products.stream().filter(p-> p.getType().equals("vegetables")).collect(Collectors.toList());

        return productsType;
    }

    //get product by name
    public List<Product> getByName(String name)
    {
        List<Product> productsType= new ArrayList<>();
        List<Product> products = getAllProducts();

        productsType = products.stream().filter(p-> p.getName().equals(name)).collect(Collectors.toList());

        return productsType;
    }

    //choose filter
    public List<Product> getProductsByNameOrType(DTO dto){

        List<Product> products = new ArrayList<>();

        if(dto.getType() == null){

            products = getByName(dto.getName());
        }
        else{

            products = getAllByType(dto.getType());
        }
        return products;
    }

    //get product by id
     public Product getProductById( long id) {

         Optional<Product> productData = productRepository.findById(id);

         /*if(productData.isPresent()) {*/
             return productData.get();
         /*}
         else {
            throw new Exception("not found!");
         }*/

     }

     //create product
    public Product createProduct(Product product)
    {
            Product _product = productRepository.save(new Product(product.getType(), product.getName(), product.getPrice(), product.getImgUrl()));
            return _product;
    }

    //update product
    public Product updateProductPriceImgUrl(long id, Product p) throws Exception{
        Optional<Product> productData = productRepository.findById(id);

        if(productData.isPresent()) {
            Product _product = productData.get();
            _product.setPrice(p.getPrice());
            _product.setImgUrl(p.getImgUrl());
            productRepository.save(_product);
            return _product;
        }
        else { throw new Exception("not found");}
    }

    //delete product
    public void deleteProduct(long id){
        productRepository.deleteById(id);
    }

    //validate price
    public boolean validatePrice(Product product){
        if(product.getPrice() <= 0 ){
            return false;
        }
        return true;
    }

}
