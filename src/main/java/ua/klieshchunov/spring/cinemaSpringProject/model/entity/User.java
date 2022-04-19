package ua.klieshchunov.spring.cinemaSpringProject.model.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Table(name="users_spring")
public class User implements Serializable {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="email")
    @Email(message = "It doesn't look like email")
    @Length(min = 1, max = 100)
    private String email;

    @Column(name="name")
    @Pattern(regexp = "(a-zA-Zа-яА-Я`'єї)+", message = "It doesn't look like a name")
    @Length(min = 1, max = 45)
    private String name;

    @Column(name="surname")
    @Pattern(regexp = "(a-zA-Zа-яА-Я`'єї)+", message = "It doesn't look like a surname")
    @Length(min = 1, max = 45)
    private String surname;

    @Column(name="password")
    private String password;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
