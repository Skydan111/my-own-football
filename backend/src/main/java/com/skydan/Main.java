package com.skydan;

import com.skydan.user.AppUser;
import com.skydan.user.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.skydan.user.Team.DYNAMO;
import static com.skydan.user.Team.SHAKHTAR;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            AppUser oleg = new AppUser(
                    "Oleg Skydan",
                    "olegskidan@gmail.com",
                    passwordEncoder.encode("password"),
                    37,
                    SHAKHTAR);
            AppUser nick = new AppUser(
                    "Nick Skydan",
                    "nickskidan@gmail.com",
                    passwordEncoder.encode("password"),
                    36,
                    DYNAMO);

            List<AppUser> users = List.of(oleg, nick);
            appUserRepository.saveAll(users);
        };
    }
}
