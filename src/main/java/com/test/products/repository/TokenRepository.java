package com.test.products.repository;

import com.test.products.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Query(value = """
            SELECT t FROM tokens t INNER JOIN users u\s  
            ON t.user.id = u.id\s
            WHERE u.id = :id AND (t.expired = false OR t.revoked = false)  
            """)
    List<Token> findAllValidTokensByUser(Long id);
}
