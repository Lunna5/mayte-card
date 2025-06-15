package dev.lunna.mayte.controller;

import dev.lunna.mayte.database.model.Card;
import dev.lunna.mayte.dto.UserCreateCardRequest;
import dev.lunna.mayte.dto.UserUpdateCardRequest;
import dev.lunna.mayte.security.AuthenticatedUser;
import dev.lunna.mayte.service.CardService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/card")
public class CardController {
  private final CardService cardService;

  @Autowired
  public CardController(@NotNull final CardService cardService) {
    this.cardService = cardService;
  }

  @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Card createCard(
      @ModelAttribute final UserCreateCardRequest createCardRequest,
      @AuthenticationPrincipal final AuthenticatedUser user
  ) {
    if (user == null) {
      throw new IllegalStateException("User must be authenticated to create a card");
    }

    return cardService.createCard(createCardRequest, user.getUser());
  }

  @PatchMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Card updateCard(
      @ModelAttribute final UserUpdateCardRequest updateCardRequest,
      @AuthenticationPrincipal final AuthenticatedUser user,
      @RequestParam(value = "id", required = false) final Long id
  ) {
    if (user == null) {
      throw new IllegalStateException("User must be authenticated to update a card");
    }

    return cardService.updateCard(updateCardRequest, id == null ? -1 : id, user.getUser());
  }

  @GetMapping("/get/{id}")
  public Card getCardById(@PathVariable("id") final Long id,
                          @AuthenticationPrincipal final AuthenticatedUser user) {
    if (user == null) {
      throw new IllegalStateException("User must be authenticated to get a card");
    }

    return cardService.getCardById(id, user.getUser());
  }

  @GetMapping("/get-all")
  public List<Card> getAllCards(@AuthenticationPrincipal final AuthenticatedUser user) {
    if (user == null) {
      throw new IllegalStateException("User must be authenticated to get cards");
    }

    return cardService.getAllUserCards(user.getUser());
  }

  @GetMapping("/get/me")
  public Card getFirstCard(@AuthenticationPrincipal final AuthenticatedUser user) {
    if (user == null) {
      throw new IllegalStateException("User must be authenticated to get a card");
    }

    return cardService.getCardById(-1, user.getUser());
  }

  @GetMapping("/image/{id}")
  public ResponseEntity<byte[]> getCardImage(@PathVariable("id") final Long id,
                      @AuthenticationPrincipal final AuthenticatedUser user
  ) {
    if (user == null) {
      throw new IllegalStateException("User must be authenticated to get a card image");
    }

    return ResponseEntity.ok(cardService.getCardImage(id, user.getUser()));
  }
}
