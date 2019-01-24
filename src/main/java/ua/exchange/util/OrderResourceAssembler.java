package ua.exchange.util;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import ua.exchange.controller.OrderController;
import ua.exchange.entity.Orderr;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class OrderResourceAssembler implements ResourceAssembler<Orderr, Resource<Orderr>> {


    @Override
    public Resource<Orderr> toResource(Orderr order) {

        return new Resource<>(order,
                linkTo(methodOn(OrderController.class).one(order.getIdd())).withSelfRel());
    }
}
