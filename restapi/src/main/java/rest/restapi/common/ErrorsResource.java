package rest.restapi.common;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;
import rest.restapi.events.Event;
import rest.restapi.events.EventController;
import rest.restapi.index.IndexController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Errors> {

    public ErrorsResource(Errors errors, Link... links) {
        super(errors, List.of(links));
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

}
