package com.ldap.ldapbackend.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

//import javax.naming.Name;

//import org.springframework.ldap.odm.annotations.Attribute;
//import org.springframework.ldap.odm.annotations.Entry;
//import org.springframework.ldap.odm.annotations.Id;

//@Entry(base = "ou=users", objectClasses = { "person", "inetOrgPerson", "top" })
@Entity
public class User  {
        
    /*@Id
    private int id;
    
    private @Attribute(name = "cn") String username;
    private @Attribute(name = "sn") String password;*/
    
	@Id
    private String name;
    private String surname;
    //private String ssn;
	private String email;
    private String university;
	private String role;
	private String status;
    private String cardNumber;
	private String picture;
    //private String expirationDate;

    public User() {
    }

    /*public User(String username, String password) {
        this.username = username;
        this.password = password;
    }*/
    
    public User(String newName, String newSurname, String newEmail, String newUniversity, 
		String newRole, String newStatus, String newCardNumber, String newPicture) {
    	name = newName;
    	surname = newSurname;
		email = newEmail;
    	university = newUniversity;
		role = newRole;
		status = newStatus;
    	cardNumber = newCardNumber;
    	picture = newPicture;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/*public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}*/

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	/*public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}*/

	/*public int getId() {
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
    }*/

}
