package rest.restapi.events;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("rest api")
                .description("rest api development")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        Event event = new Event();
        String name = "event";
        String spring = "spring";

        event.setName(name);
        event.setDescription(spring);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(spring);
    }
}