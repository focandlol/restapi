package rest.restapi.events;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import rest.restapi.accounts.Account;
import rest.restapi.accounts.AccountSerializer;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="id")
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    @ManyToOne
    @JsonSerialize(using = AccountSerializer.class)
    private Account account;

    public void update(){
        if(basePrice == 0 && maxPrice ==0){
            this.free = true;
        } else{
            this.free = false;
        }

        if(location == null || location.isBlank()){
            this.offline = false;
        } else{
            this.offline = true;
        }
    }

}
