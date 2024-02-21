package rest.restapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;


import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


public class EventResource extends EntityModel<Event>{ //RepresentationModel {

    /*@JsonUnwrapped
    private Event event;

    public EventResource(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }*/

    public EventResource(Event event, Link... links) {
        super(event, List.of(links));
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
