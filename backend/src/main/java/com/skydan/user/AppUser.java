package com.skydan.user;

import jakarta.persistence.*;

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
public class AppUser {

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
    private Integer age;
    @Column(
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Team team;

    public AppUser() {
    }

    public AppUser(Integer id, String name, String email, Integer age, Team team) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.team = team;
    }

    public AppUser(String name, String email, Integer age, Team team) {
        this.name = name;
        this.email = email;
        this.age = age;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id) &&
                Objects.equals(name, appUser.name) &&
                Objects.equals(email, appUser.email) &&
                Objects.equals(age, appUser.age) && team == appUser.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age, team);
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", team=" + team +
                '}';
    }
}
