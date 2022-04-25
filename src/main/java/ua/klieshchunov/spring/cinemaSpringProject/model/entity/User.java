package ua.klieshchunov.spring.cinemaSpringProject.model.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ua.klieshchunov.spring.cinemaSpringProject.config.security.ApplicationUserRole;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="users_spring")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {
    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    @Pattern(regexp = "^[a-zA-Zа-яА-Яєїё']+$", message = "Name should contain only letters")
    @Length(min = 1, max = 45, message = "Name should be no longer than 45 letters," +
            "no shorter than 1 letter")
    private String name;

    @Column(name="surname")
    @Pattern(regexp = "^[a-zA-Zа-яА-Яєїё']+$", message = "Surname should contain only letters")
    @Length(min = 1, max = 45, message = "Surname should be no longer than 45 letters," +
            "no shorter than 1 letter")
    private String surname;

    @Column(name="email")
    @Email(message = "It doesn't look like email")
    @Length(min = 1, max = 100)
    private String email;

    @Column(name="password")
    @Pattern(regexp = "^(?=.*?[0-9]).{8,}$", message = "Password should be at least 8 symbols long " +
            "and contain at least one digit")
    private String password;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private ApplicationUserRole role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
