package rest.restapi.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rest.restapi.common.RestDocsConfiguration;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

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
                .andExpect(jsonPath("offline").value(Matchers.not(false)))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-events").exists())
                .andDo(document("create-event",
                       links(linkWithRel("self").description("link to self"),
                               linkWithRel("query-events").description("link to query events"),
                               linkWithRel("update-events").description("link to query update an existing event"),
                               linkWithRel("profile").description("link to profile event")
                               ),
                       requestHeaders (
                            headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                            headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")

                       ),
                        requestFields(
                            fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of new event"),
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("free").description("it tells is this event is free or not"),
                                fieldWithPath("offline").description("it tells is this event is offline or not"),
                                fieldWithPath("eventStatus").description("eventStatus"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query-events"),
                                fieldWithPath("_links.update-events.href").description("link to update existing event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ));
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                //.andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists());
                //.andExpect(jsonPath("$[0].rejectedValue").exists());

    }

    @Test
    @DisplayName("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception{
        IntStream.range(0,30).forEach(i ->{
            generateEvent(i);
        });

        mockMvc.perform(get("/api/events")
                        .param("page","1")
                        .param("size","10")
                        .param("sort","name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andDo(document("query-events"));

    }

    @Test
    @DisplayName("기존의 이벤트를 하나 조회")
    public void getEvent() throws Exception {
        Event event = generateEvent(100);
        mockMvc.perform(get("/api/events/{id}",event.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"));
    }

    @Test
    @DisplayName("없는 이벤트는 조회했을 때 404 응답받기")
    public void getEvent404() throws Exception {
        mockMvc.perform(get("/api/events/1188"))
                .andDo(print())
                .andExpect(status().isNotFound())
               ;
    }

    @Test
    @DisplayName("이벤트 수정하기")
    public void updateEvent() throws Exception {
        Event event = generateEvent(1000);

        EventDto eventDto = EventDto.builder()
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

        mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value("kkm"))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("update-event"));

    }

    @Test
    @DisplayName("수정하려는 이벤트가 없는 경우")
    public void updateEvent404() throws Exception {
        EventDto eventDto = EventDto.builder()
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

        mockMvc.perform(put("/api/events/123123")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("입력 데이터(데이터 바인팅)가 이상한 경우")
    public void updateEvent400() throws Exception {
        Event event = generateEvent(1000);

        EventDto eventDto = EventDto.builder()
                //.id(100)
                .name("kkm")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2011,11,11,11,11))
                .closeEnrollmentDateTime(LocalDateTime.of(2012,12,12,11,11))
                .beginEventDateTime(LocalDateTime.of(2013,12,12,11,11))
                //.endEventDateTime(LocalDateTime.of(2014,12,12,11,11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("asdfasdf")
                // .free(true)
                // .offline(false)
                .build();

        mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("도메인 로직으로 데이터 검증 실패")
    public void updateEvent400_domain() throws Exception {
        Event event = generateEvent(1000);

        EventDto eventDto = EventDto.builder()
                //.id(100)
                .name("kkm")
                .description("rest api")
                .beginEnrollmentDateTime(LocalDateTime.of(2011,11,11,11,11))
                .closeEnrollmentDateTime(LocalDateTime.of(2012,12,12,11,11))
                .beginEventDateTime(LocalDateTime.of(2014,12,12,11,11))
                .endEventDateTime(LocalDateTime.of(2013,12,12,11,11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("asdfasdf")
                // .free(true)
                // .offline(false)
                .build();

        mockMvc.perform(put("/api/events/{id}",event.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("test event")
                .build();
        return eventRepository.save(event);
    }

}
