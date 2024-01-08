package com.skydan;

import com.skydan.player.Player;
import com.skydan.playerStatistics.*;
import com.skydan.user.AppUser;
import com.skydan.user.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.skydan.player.Position.*;
import static com.skydan.user.Team.*;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            PlayerStatisticsRepository playerStatisticsRepository
            ) {
        return args -> {
            AppUser oleg = new AppUser(
                    "Oleg Skydan",
                    "olegskidan@gmail.com",
                    passwordEncoder.encode("password"),
                    SHAKHTAR);
            AppUser nick = new AppUser(
                    "Nick Skydan",
                    "nickskidan@gmail.com",
                    passwordEncoder.encode("password"),
                    DYNAMO);

            Player sikan = new Player("Danylo Sikan", FORWARD, SHAKHTAR);
            Player shaparenko = new Player("Mykola Shaparenko", MIDFIELDER, DYNAMO);
            Player danchenko = new Player("Oleh Danchenko", DEFENDER, ZORIA);

            PlayerStatistics sikanStatistics = new ForwardStatistics(10, 8, 6);
            PlayerStatistics shaparenkoStatistics = new MidfielderStatistics(10, 7, 3);
            PlayerStatistics danchenkoStatistics = new DefenderStatistics(10, 5, 43);

            playerStatisticsRepository.saveAll(List.of(shaparenkoStatistics, sikanStatistics, danchenkoStatistics));

            sikan.setPlayerStatistics(sikanStatistics);
            shaparenko.setPlayerStatistics(shaparenkoStatistics);
            danchenko.setPlayerStatistics(danchenkoStatistics);

            oleg.addPlayer(sikan);
            oleg.addPlayer(danchenko);
            nick.addPlayer(shaparenko);
            nick.addPlayer(danchenko);

            List<AppUser> users = List.of(oleg, nick);
            appUserRepository.saveAll(users);
        };
    }
}
