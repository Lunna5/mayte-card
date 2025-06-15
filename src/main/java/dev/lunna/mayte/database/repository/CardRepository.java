package dev.lunna.mayte.database.repository;

import dev.lunna.mayte.database.model.Card;
import dev.lunna.mayte.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
  List<Card> findByUser(User user);

  long countByUser(User user);

  Optional<Card> findFirstByUserOrderByIdAsc(User user);

  boolean existsByUser(User user);
}