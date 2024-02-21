package rest.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    //@MockBean
    //EventRepository eventRepository;

    @Test
    @DisplayName("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                //.id(100)
                .name("kkm")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2011,11,11,11,11))
                .closeEnrollmentDateTime(LocalDateTime.of(2012,12,12,11,11))
                .beginEventDateTime(LocalDateTime.of(2013,12,12,11,11))
                .endEventDateTime(LocalDateTime.of(2014,12,12,11,11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("asdfasdf")
               // .free(true)
               // .offline(false)
                .build();

        //event.setId(10);
        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("id").value(Matchers.not(100)));
    }

    @Test
    @DisplayName("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("kkm")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2011,11,11,11,11))
                .closeEnrollmentDateTime(LocalDateTime.of(2012,12,12,11,11))
                .beginEventDateTime(LocalDateTime.of(2013,12,12,11,11))
                .endEventDateTime(LocalDateTime.of(2014,12,12,11,11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("asdfasdf")
                .free(true)
                .offline(false)
                .build();



        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto event = EventDto.builder()
                .name("kkm")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2011,11,12,11,11))
                .closeEnrollmentDateTime(LocalDateTime.of(2012,12,11,11,11))
                .beginEventDateTime(LocalDateTime.of(2013,12,12,11,11))
                .endEventDateTime(LocalDateTime.of(2011,12,11,11,11))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("asdfasdf")
                .build();

        //event.setId(10);
        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
