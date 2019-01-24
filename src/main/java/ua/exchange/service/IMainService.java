package ua.exchange.service;

import ua.exchange.entity.Orderr;
import ua.exchange.entity.Participant;
import ua.exchange.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IMainService {

    List<Orderr> ordersByParticipantName(String name);

    List<Orderr> ordersForSeller(Product product, BigDecimal price);

    List<Orderr> ordersForBuyer(Product product, BigDecimal price);

    Orderr saveOrder(Orderr order);

    void deleteOrder(Orderr order);

    Optional<Orderr> orderById(Long id);

    Optional<Participant> participantByName(String name);

    Product productByName(String name);

    Product saveProduct(Product product);


}
