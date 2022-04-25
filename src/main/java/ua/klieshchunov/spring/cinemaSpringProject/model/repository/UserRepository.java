package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByEmail(@Param("email") String email);
    User getUserByEmail(@Param("email") String email);

    @Override
    <S extends User> S save(S user);
}
