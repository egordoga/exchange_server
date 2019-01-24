package ua.exchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.exchange.dao.OrderRepository;
import ua.exchange.dao.ParticipantRepository;
import ua.exchange.dao.ProductRepository;
import ua.exchange.entity.Orderr;
import ua.exchange.entity.Participant;
import ua.exchange.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

// Т.к. логики не много все сведено в один класс
@Service
public class MainService implements IMainService {

    private OrderRepository orderRepository;
    private ParticipantRepository participantRepository;
    private ProductRepository productRepository;

    @Autowired
    public MainService(OrderRepository orderRepository, ParticipantRepository participantRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.participantRepository = participantRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Orderr> ordersByParticipantName(String name) {
        return orderRepository.findAllByParticipant_Name(name);
    }

    @Override
    public List<Orderr> ordersForSeller(Product product, BigDecimal price) {
        return orderRepository.findAllByProductAndPriceIsGreaterThanEqualAndSideOfSell(product, price, false);
    }

    @Override
    public List<Orderr> ordersForBuyer(Product product, BigDecimal price) {
        return orderRepository.findAllByProductAndPriceIsLessThanEqualAndSideOfSell(product, price, true);
    }

    @Override
    public Orderr saveOrder(Orderr order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Orderr order) {
        orderRepository.delete(order);
    }

    @Override
    public Optional<Orderr> orderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Optional<Participant> participantByName(String name) {
        return participantRepository.findByName(name);
    }

    @Override
    public Product productByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
