package ua.exchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ua.exchange.entity.Orderr;
import ua.exchange.entity.Participant;
import ua.exchange.entity.Product;
import ua.exchange.exeption.OrderNotFoundException;
import ua.exchange.service.MainService;
import ua.exchange.util.OrderResourceAssembler;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/orders", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
@ExposesResourceFor(Orderr.class)
public class OrderController {

    private MainService mainService;
    private OrderResourceAssembler assembler;

    @Autowired
    public OrderController(MainService mainService, OrderResourceAssembler assembler) {
        this.mainService = mainService;
        this.assembler = assembler;
    }

    @GetMapping("/{name}/{price}/{quantity}/{isSeller}")
    public Resources<Resource<Orderr>> order(@PathVariable String name,
                                             @PathVariable String price,
                                             @PathVariable String quantity,
                                             @PathVariable Boolean isSeller) {

        Product product = mainService.productByName(name);
        List<Resource<Orderr>> orders;
        if (isSeller) {
            orders = mainService.ordersForSeller(product, new BigDecimal(price)).stream()
                    .map(assembler::toResource).collect(Collectors.toList());
        } else {
            orders = mainService.ordersForBuyer(product, new BigDecimal(price)).stream()
                    .map(assembler::toResource).collect(Collectors.toList());
        }
        return new Resources<>(orders,
                linkTo(methodOn(OrderController.class).order(name, price, quantity, isSeller)).withSelfRel());
    }

    @GetMapping("/{id}")
    public Resource<Orderr> one(@PathVariable Long id) {
        return assembler.toResource(mainService.orderById(id).orElseThrow(() -> new OrderNotFoundException(id)));
    }

    @GetMapping("/own")
    public Resources<Resource<Orderr>> myOrders(Principal principal) {
        List<Resource<Orderr>> orders = mainService.ordersByParticipantName(principal.getName())
                .stream().map(assembler::toResource).collect(Collectors.toList());
        return new Resources<>(orders, linkTo(methodOn(OrderController.class).myOrders(principal)).withSelfRel());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResourceSupport> updateOrder(@PathVariable Long id, @RequestBody Orderr resource) {

        Product product = mainService.productByName(resource.getProduct().getName());
        if (product == null) {
            Product p = new Product();
            p.setName(resource.getProduct().getName());
            mainService.saveProduct(p);
            resource.setProduct(p);
        } else {
            resource.setProduct(product);
        }
        mainService.saveOrder(resource);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteOrder(@PathVariable Long id, Principal principal) {

        Orderr order = mainService.orderById(id).orElseThrow(() -> new OrderNotFoundException(id));
        if (order.getParticipant().getName().equals(principal.getName())) {
            mainService.deleteOrder(order);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Resource<Orderr>> saveOrder(@RequestBody Orderr orderDto) {

        Product product = mainService.productByName(orderDto.getProduct().getName());
        if (product == null) {
            product = new Product();
            product.setName(orderDto.getProduct().getName());
            mainService.saveProduct(product);
        }
        Participant participant = mainService.participantByName(orderDto.getParticipant().getName())
                .orElseThrow(() -> new UsernameNotFoundException(orderDto.getParticipant().getName()));

        Orderr orderr = new Orderr(orderDto.getSideOfSell(), orderDto.getPrice(), orderDto.getSize(),
                LocalDateTime.now(), participant, product);

        Orderr newOrder = mainService.saveOrder(orderr);

        return ResponseEntity
                .created(linkTo(methodOn(OrderController.class).one(newOrder.getIdd())).toUri())
                .body(assembler.toResource(newOrder));
    }
}
