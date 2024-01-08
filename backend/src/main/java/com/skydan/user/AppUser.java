package com.skydan.user;

import com.skydan.player.Player;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "app_user",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "app_user_email_unique",
                        columnNames = "email"
                )
        }
)
public class AppUser implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "app_user_id_sequence",
            sequenceName = "app_user_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "app_user_id_sequence"
    )
    private Integer id;
    @Column(
            nullable = false
    )
    private String name;
    @Column(
            nullable = false
    )
    private String email;
    @Column(
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Team team;
    @Column(
            nullable = false
    )
    private String password;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    @JoinTable(
            name = "transfers",
            joinColumns = @JoinColumn(
                    name = "app_user_id",
                    foreignKey = @ForeignKey(name = "transfers_app_user_id_fk")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "player_id",
                    foreignKey = @ForeignKey(name = "transfers_player_id_fk")
            )
    )
    private List<Player> players = new ArrayList<>();

    public AppUser() {
    }

    public AppUser(Integer id,
                   String name,
                   String email,
                   String password,
                   Team team) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.team = team;
    }

    public AppUser(String name,
                   String email,
                   String password,
                   Team team) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.team = team;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            player.getUsers().add(this);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
        player.getUsers().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id) &&
                Objects.equals(name, appUser.name) &&
                Objects.equals(email, appUser.email) &&
                team == appUser.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, team);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", team=" + team +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
