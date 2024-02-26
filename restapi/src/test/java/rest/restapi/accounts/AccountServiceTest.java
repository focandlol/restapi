package rest.restapi.accounts;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {

    @Rule
    ExpectedException expectedException = ExpectedException.none();

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void findByUsername(){
        String password = "kkm";
        String username = "asdasdsd";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN,AccountRole.USER))
                .build();
        accountService.saveAccount(account);

        UserDetailsService userDetailService = accountService;
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        assertThat(passwordEncoder.matches(password,userDetails.getPassword())).isTrue();
    }

    @Test
    public void findByUsernameFail(){
        String username = "kkm";

       //expectedException.expect(UsernameNotFoundException.class);
       // expectedException.expectMessage(Matchers.containsString(username));


       // accountService.loadUserByUsername(username);



        assertThrows(UsernameNotFoundException.class,() -> accountService.loadUserByUsername(username));
    }
}