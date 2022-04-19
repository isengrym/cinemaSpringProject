package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;

@Repository
public interface UserDetailsRepository extends CrudRepository<User, Integer> {
    User getUserByEmail(@Param("email") String email);
}
