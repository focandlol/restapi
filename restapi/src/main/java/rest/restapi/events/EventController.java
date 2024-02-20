package rest.restapi.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Slf4j
@Controller
@RequestMapping(value = "/api/events",produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody EventDto eventDto){
        log.info("EventController createEvent");
        Event event = modelMapper.map(eventDto, Event.class);
        Event newEvent = eventRepository.save(event);
        URI createUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();

        return ResponseEntity.created(createUri).body(event);
    }
}
