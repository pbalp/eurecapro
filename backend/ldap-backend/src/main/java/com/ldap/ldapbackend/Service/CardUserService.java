package com.ldap.ldapbackend.Service;

import java.util.List;

import javax.transaction.Transactional;

import com.ldap.ldapbackend.Model.CardUser;
import com.ldap.ldapbackend.Repository.CardUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CardUserService {

    @Autowired
    CardUserRepository cardUserRepository;

    public CardUser getCardUserByEmail(String email) {
        return cardUserRepository.findByEmail(email);
    }

    public CardUser getCardUser(String email, String role) {
        return cardUserRepository.findByEmailAndRole(email, role);
    }

    public CardUser getCardUserByCardNumber(String cardNumber) {
        return cardUserRepository.findByCardNumber(cardNumber);
    }

    public List<CardUser> getCardUserByUniversity(String university) {
        return cardUserRepository.findByUniversity(university);
    }

    public void saveCardUser(CardUser cardUser){
        cardUserRepository.save(cardUser);
    }
    
}
