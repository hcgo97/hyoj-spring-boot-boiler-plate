package org.hyoj.mysbbp.repository;

import org.hyoj.mysbbp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUserId(String userId);

    List<Users> findByIsDeleted(String isDeleted);

}
