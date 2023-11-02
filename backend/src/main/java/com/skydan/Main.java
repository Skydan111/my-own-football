package com.skydan;

import com.skydan.user.AppUser;
import com.skydan.user.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(AppUserRepository appUserRepository) {
        return args -> {
            AppUser oleg = new AppUser("Oleg Skydan", "olegskidan@gmail.com", 37);
            AppUser nick = new AppUser("Nick Skydan", "nickskidan@gmail.com", 36);

            List<AppUser> users = List.of(oleg, nick);
            appUserRepository.saveAll(users);
        };
    }
}
