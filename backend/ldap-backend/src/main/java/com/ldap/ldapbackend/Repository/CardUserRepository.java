package com.ldap.ldapbackend.Repository;

import java.util.List;

import com.ldap.ldapbackend.Model.CardUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
public interface CardUserRepository extends JpaRepository<CardUser, Long> {

    CardUser findByEmail(String email);

    CardUser findByEmailAndRole(String email, String role);

    CardUser findByCardNumber(String cardNumber);
    
    List<CardUser> findByUniversity(String university);
}
