package ua.exchange.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.exchange.entity.Orderr;
import ua.exchange.entity.Product;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orderr, Long> {

    List<Orderr> findAllByProductAndPriceIsLessThanEqualAndSideOfSell(Product product, BigDecimal price, Boolean sideOfSell);

    List<Orderr> findAllByProductAndPriceIsGreaterThanEqualAndSideOfSell(Product product, BigDecimal price, Boolean sideOfSell);

    List<Orderr> findAllByParticipant_Name(String name);
}
