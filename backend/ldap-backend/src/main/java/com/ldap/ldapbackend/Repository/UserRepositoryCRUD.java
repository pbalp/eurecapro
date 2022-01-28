package com.ldap.ldapbackend.Repository;

import org.springframework.data.repository.CrudRepository;

import com.ldap.ldapbackend.Model.User;

public interface UserRepositoryCRUD extends CrudRepository<User, String>{
    
}
