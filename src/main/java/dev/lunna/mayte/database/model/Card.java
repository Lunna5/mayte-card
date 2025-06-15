package dev.lunna.mayte.database.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class Card {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private Long cardNumber;

  @Column(nullable = false)
  private String cardHolderName;

  @Column(columnDefinition = "TEXT")
  private String dedication;

  @Column
  private String photoSrc;

  @Column
  private String theme;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public Card() {
    this.cardNumber = 0L;
  }

  public Card(String cardHolderName, String dedication, String photoSrc, User user) {
    this.cardNumber = 0L;
    this.cardHolderName = cardHolderName;
    this.dedication = dedication;
    this.photoSrc = photoSrc;
    this.user = user;
  }

  public Long getId() {
    return id;
  }

  public Long getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(Long cardNumber) {
    this.cardNumber = cardNumber;
  }

  public String getCardHolderName() {
    return cardHolderName;
  }

  public void setCardHolderName(String cardHolderName) {
    this.cardHolderName = cardHolderName;
  }

  public String getDedication() {
    return dedication;
  }

  public void setDedication(String dedication) {
    this.dedication = dedication;
  }

  public String getPhotoSrc() {
    return photoSrc;
  }

  public void setPhotoSrc(String photoSrc) {
    this.photoSrc = photoSrc;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }
}
