package licentaBackend.code.repository;

import licentaBackend.code.models.OrderProduct;
import licentaBackend.code.models.OrderProductPK;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProductPK> {
}