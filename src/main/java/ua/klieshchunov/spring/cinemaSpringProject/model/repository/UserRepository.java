package ua.klieshchunov.spring.cinemaSpringProject.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.Seance;
import ua.klieshchunov.spring.cinemaSpringProject.model.entity.User;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    boolean existsByEmail(@Param("email") String email);
    User getUserByEmail(@Param("email") String email);

    @Override
    <S extends User> S save(S user);

    @Modifying
    @Transactional
    @Query("update User u set u =:user where u = :user")
    void updateUser(User user);

    @Modifying
    @Transactional
    @Query("delete FROM User u where u = :user")
    void deleteUser(@Param("user") User user);

    Page<User> findAll(Pageable pageable);
}
