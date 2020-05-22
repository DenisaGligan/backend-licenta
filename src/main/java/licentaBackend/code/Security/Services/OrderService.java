package licentaBackend.code.Security.Services;

import licentaBackend.code.models.Order;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface OrderService {

    List<Order> getAllOrders();

    Order create(@NotNull(message = "The order cannot be null.") @Valid Order order);

    void update(@NotNull(message = "The order cannot be null.") @Valid Order order);
    Order updateStatus(Long id,Order order) throws Exception;

    Boolean verifyStatus(Order o);

    List<Order> getOrdersByUserId(Long id);

}