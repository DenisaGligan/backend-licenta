package licentaBackend.code.Security.Services;

import licentaBackend.code.models.Order;
import licentaBackend.code.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return this.orderRepository.findAll();
    }

    @Override
    public Order create(Order order) {
        order.setDateCreated(LocalDate.now());

        return this.orderRepository.save(order);
    }

    @Override
    public void update(Order order) {
        this.orderRepository.save(order);
    }


    public List<Order> getOrdersByUserId(Long id){

        List<Order> userOrders = new ArrayList<>();
        List<Order> allOrders = getAllOrders();

         for(Order o : allOrders){
             if(o.getUser().getId() == id){
                    System.out.println(o.getUser().getId());

                 userOrders.add(o);
             }
         }
         return userOrders;
    }

}