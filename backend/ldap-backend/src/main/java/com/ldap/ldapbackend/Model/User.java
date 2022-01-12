package com.ldap.ldapbackend.Model;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Entry(base = "ou=users", objectClasses = { "person", "inetOrgPerson", "top" })
public class User  {
        
    @Id
    private int id;
    
    private @Attribute(name = "cn") String username;
    private @Attribute(name = "sn") String password;
    
    private String name;
    private String surname;
    private String ssn;
    private String university;
    private String cardNumber;
    private String expirationDate;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public User(String newName, String newSurname, String newSSN, String newUniversity, String newCardNumber, String newExpirationDate) {
    	name = newName;
    	surname = newSurname;
    	ssn = newSSN;
    	university = newUniversity;
    	cardNumber = newCardNumber;
    	expirationDate = newExpirationDate;
    }
    

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return username;
    }

}
