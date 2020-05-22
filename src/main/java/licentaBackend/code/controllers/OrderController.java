package licentaBackend.code.controllers;


import licentaBackend.code.Exception.ResourceNotFoundException;
import licentaBackend.code.Security.Services.OrderProductService;
import licentaBackend.code.Security.Services.OrderService;
import licentaBackend.code.Security.Services.ProductService;
import licentaBackend.code.Security.Services.UserDetailsServiceImpl;
import licentaBackend.code.models.*;
import licentaBackend.code.payload.response.MessageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    ProductService productService;
    OrderService orderService;
    OrderProductService orderProductService;
    UserDetailsServiceImpl userDetailsService;

    public OrderController(ProductService productService, OrderService orderService, OrderProductService orderProductService) {
        this.productService = productService;
        this.orderService = orderService;
        this.orderProductService = orderProductService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public @NotNull List<Order> getAllOrders(){
        return this.orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable("id") Long id){

        try {
            List<Order> userOrder = orderService.getOrdersByUserId(id);
            System.out.println("Dimensiune:" + userOrder.size());
            return  new ResponseEntity<>(userOrder, HttpStatus.OK);
        }
        catch(Exception e) {
            System.out.println(e);
            return  new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Long id,@RequestBody Order o){

        if(orderService.verifyStatus(o)){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(" The order is already honored"));
        }

        try{
            Order _order = orderService.updateStatus(id,o);
            return new ResponseEntity<>(_order,HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody OrderForm form){

        List<OrderProductDTO> formDTO = form.getProductOrders();
        validateProductsExistence(formDTO);

        System.out.println(form.getUser_id());
        //User user= userDetailsService.findById(form.getUser_id());
        User user =new User();
        System.out.println(user);
        user.setId(form.getUser_id());


        Order order = new Order();
        order.setStatus(OrderStatus.PROCESSING.name());

        order.setAdress(form.getAdress());
        order.setTelephone(form.getTelephone());

        order.setUser(user);

        order = this.orderService.create(order);

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderProductDTO dto: formDTO){
            orderProducts.add(orderProductService.create(new OrderProduct(order,productService.getProductById(dto.getProduct().getId()),dto.getQuantity())));
        }
        order.setOrderProducts(orderProducts);

        this.orderService.update(order);

        String uri = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/orders/{id}")
                .buildAndExpand(order.getId())
                .toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", uri);

        return new ResponseEntity<>(order, headers, HttpStatus.CREATED);
    }

    private void validateProductsExistence(List<OrderProductDTO> orderProducts) {
        List<OrderProductDTO> list = orderProducts
                .stream()
                .filter(op -> Objects.isNull(productService.getProductById(op
                        .getProduct()
                        .getId())))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(list)) {
            new ResourceNotFoundException("Product not found");
        }
    }
    public static class OrderForm {

        private List<OrderProductDTO> productOrders;

        private Long user_id;

        public String getAdress() {
            return adress;
        }

        public void setAdress(String adress) {
            this.adress = adress;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        private String adress;
        private String telephone;

        public List<OrderProductDTO> getProductOrders() {
            return productOrders;
        }

        public void setProductOrders(List<OrderProductDTO> productOrders) {
            this.productOrders = productOrders;
        }

        public Long getUser_id() {
            return user_id;
        }

        public void setUser_id(Long user_id) {
            this.user_id = user_id;
        }
    }
}
