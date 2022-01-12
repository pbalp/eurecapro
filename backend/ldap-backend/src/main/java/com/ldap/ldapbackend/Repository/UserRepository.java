package com.ldap.ldapbackend.Repository;

import java.util.List;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import com.example.ldap.ldapbackend2.Model.User;

@Repository
public interface UserRepository extends LdapRepository<User> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    List<User> findByUsernameLikeIgnoreCase(String username);

}
