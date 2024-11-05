package com.masonord.harmonyhound.repository;

import com.masonord.harmonyhound.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long id);

    @Modifying
    @Query (value = "UPDATE users SET lang = ?2 WHERE users.user_id = ?1", nativeQuery = true)
    int updateUserLang(Long id, String lang);
}
