package com.skydan;

import com.skydan.player.Player;
import com.skydan.player.PlayerService;
import com.skydan.playerStatistics.*;
import com.skydan.user.AppUser;
import com.skydan.user.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
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
            PlayerService playerService
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

            PlayerStatistics sikanStats1 = new ForwardStatistics(1, 1, 2, 1);
            PlayerStatistics sikanStats2 = new ForwardStatistics(2, 0, 1, 0);
            PlayerStatistics sikanStats3 = new ForwardStatistics(3, 1, 0, 2);

            PlayerStatistics shaparenkoStats1 = new MidfielderStatistics(1, 1, 1, 2);
            PlayerStatistics shaparenkoStats2 = new MidfielderStatistics(2, 1, 0, 1);
            PlayerStatistics shaparenkoStats3 = new MidfielderStatistics(3, 1, 0, 3);

            PlayerStatistics danchenkoStats1 = new DefenderStatistics(1, 0, 0, 4);
            PlayerStatistics danchenkoStats2 = new DefenderStatistics(2, 0, 0, 5);
            PlayerStatistics danchenkoStats3 = new DefenderStatistics(3, 1, 1, 4);

            sikan.addStatistics(sikanStats1);
            sikan.addStatistics(sikanStats2);
            sikan.addStatistics(sikanStats3);

            shaparenko.addStatistics(shaparenkoStats1);
            shaparenko.addStatistics(shaparenkoStats2);
            shaparenko.addStatistics(shaparenkoStats3);

            danchenko.addStatistics(danchenkoStats1);
            danchenko.addStatistics(danchenkoStats2);
            danchenko.addStatistics(danchenkoStats3);


            sikan.setStatsList(List.of(sikanStats1, sikanStats2, sikanStats3));
            shaparenko.setStatsList(List.of(shaparenkoStats1, shaparenkoStats2, shaparenkoStats3));
            danchenko.setStatsList(List.of(danchenkoStats1, danchenkoStats2, danchenkoStats3));

            oleg.addPlayer(sikan);
            oleg.addPlayer(danchenko);
            nick.addPlayer(shaparenko);
            nick.addPlayer(danchenko);

            List<AppUser> users = List.of(oleg, nick);
            appUserRepository.saveAll(users);

            System.out.println("----------------------");
            System.out.println(sikan.getTotalPoints(playerService));
            System.out.println("----------------------");
            System.out.println(sikan.calculatePointsForCurrentTour(playerService, 1));
            System.out.println("----------------------");
            System.out.println(sikan.calculatePointsForCurrentTour(playerService, 2));
            System.out.println("----------------------");
            System.out.println(sikan.calculatePointsForCurrentTour(playerService, 3));
        };
    }
}
