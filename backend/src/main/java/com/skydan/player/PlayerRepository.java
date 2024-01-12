package com.skydan.player;

import com.skydan.user.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Integer> {

    List<Player> findPlayersByTeam(Team team);
    List<Player> findPlayersByPosition(Position position);
}
