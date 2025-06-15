package dev.lunna.mayte.service;

import dev.lunna.mayte.authority.ApplicationRole;
import dev.lunna.mayte.database.model.Card;
import dev.lunna.mayte.database.model.User;
import dev.lunna.mayte.database.repository.CardRepository;
import dev.lunna.mayte.database.repository.UserRepository;
import dev.lunna.mayte.dto.UserCreateCardRequest;
import dev.lunna.mayte.dto.UserUpdateCardRequest;
import dev.lunna.mayte.exception.SimpleJsonException;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class CardService {
  private static final Logger log = LoggerFactory.getLogger(CardService.class);
  private final CardRepository cardRepository;
  private final FileStorageService fileStorageService;
  private final UserRepository userRepository;

  @Autowired
  public CardService(
      @NotNull final CardRepository cardRepository,
      @NotNull final FileStorageService fileStorageService,
      @NotNull final UserRepository userRepository
  ) {
    this.cardRepository = cardRepository;
    this.fileStorageService = fileStorageService;
    this.userRepository = userRepository;
  }

  @Transactional
  public Card createCard(@NotNull final UserCreateCardRequest userCreateCardRequest, @NotNull final User user) {
    long cardCount = cardRepository.countByUser(user);

    if (cardCount > 1 && !user.hasRole(ApplicationRole.ROLE_ADMIN)) {
      throw new IllegalStateException("User cannot create more than 2 cards");
    }

    User managedUser = userRepository.findById(user.getId())
        .orElseThrow(() -> new SimpleJsonException("User not found", 404, "ID: " + user.getId()));

    String result = fileStorageService.storeFile(
        userCreateCardRequest.image(),
        "cards/" + user.getUsername(),
        UUID.randomUUID() + ".png",
        true
    );

    Card card = new Card(
        userCreateCardRequest.cardHolderName(),
        userCreateCardRequest.dedication(),
        result,
        managedUser
    );

    cardRepository.save(card);

    return card;
  }

  @Transactional
  public Card getCardById(long id, @NotNull final User user) {
    Card card;

    if (id == -1) {
      card = cardRepository.findFirstByUserOrderByIdAsc(user)
          .orElseThrow(() -> new SimpleJsonException("Card not found", 404, "Card with ID " + id + " The user has no cards"));
    } else {
      card = cardRepository.findById(id)
          .orElseThrow(() -> new SimpleJsonException("Card not found", 404, "Card with ID " + id + " not found"));
    }

    if (user.hasRole(ApplicationRole.ROLE_ADMIN)) {
      return card; // Admins can access any card
    }

    if (!card.getUser().equals(user)) {
      throw new SimpleJsonException("Card not found", 404, "Card with ID " + id + " not found");
    }

    return card;
  }

  @Transactional
  public Card updateCard(@NotNull final UserUpdateCardRequest userUpdateCardRequest, long id, @NotNull final User user) {
    Card card = getCardById(id, user);

    if (!card.getUser().equals(user)) {
      throw new IllegalStateException("User does not own this card");
    }

    if (userUpdateCardRequest.image() != null) {
      String result = fileStorageService.storeFile(
          userUpdateCardRequest.image(),
          "cards/" + user.getUsername(),
          UUID.randomUUID() + ".png",
          true
      );

      card.setPhotoSrc(result);
    }

    if (userUpdateCardRequest.cardHolderName() != null) {
      card.setCardHolderName(userUpdateCardRequest.cardHolderName());
    }

    if (userUpdateCardRequest.dedication() != null) {
      card.setDedication(userUpdateCardRequest.dedication());
    }

    if (userUpdateCardRequest.theme() != null) {
      card.setTheme(userUpdateCardRequest.theme());
    }

    cardRepository.save(card);

    return card;
  }

  public List<Card> getAllUserCards(@NotNull final User user) {
    return cardRepository.findByUser(user);
  }

  @Transactional
  public byte[] getCardImage(
      @NotNull final Long id,
      @NotNull final User user
  ) {
    Card card = getCardById(id, user);

    if (card.getPhotoSrc() == null) {
      throw new SimpleJsonException("Card image not found", 404, "Card with ID " + id + " has no image");
    }

    try {
      return fileStorageService.getFile(card.getPhotoSrc()).getContentAsByteArray();
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new SimpleJsonException("Card not found", 404, "Card with ID " + id + " has no image");
    }
  }
}
