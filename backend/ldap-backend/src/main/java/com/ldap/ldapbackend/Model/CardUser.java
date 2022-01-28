package com.ldap.ldapbackend.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cardUser")
public class CardUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String email;
  private String role;
  private String cardNumber;
  private String university;

  public CardUser() {}

  public CardUser(String email, String role, String cardNumber, String university) {
      this.email = email;
      this.role = role;
      this.cardNumber = cardNumber;
      this.university = university;
  }

  public String getEmail() {
	  return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

  public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

  public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

  public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}
    
}
