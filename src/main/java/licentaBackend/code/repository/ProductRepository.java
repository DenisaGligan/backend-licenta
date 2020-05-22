package licentaBackend.code.repository;

import licentaBackend.code.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByType(String type);
    List<Product> findByName( String name);

}
