package com.ldap.ldapbackend.Repository;

import com.ldap.ldapbackend.Model.User;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryJPA extends JpaRepository<User, Long> {
    
    //public User findbyEmail(@Param("email") String email);

    //public User findById(@Param("id") int id);

    //public User findByUsername(@Param("username") String username);
}
