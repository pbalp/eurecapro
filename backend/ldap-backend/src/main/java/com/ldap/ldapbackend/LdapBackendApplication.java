package com.ldap.ldapbackend;

import java.util.Properties;

import javax.naming.*;
import javax.naming.directory.*;
//import javax.naming.Context;
//import javax.naming.NamingException;
//import javax.naming.directory.DirContext;
//import javax.naming.directory.InitialDirContext;

import com.ldap.ldapbackend.Controller.Controller;
import com.ldap.ldapbackend.Model.User;
import com.ldap.ldapbackend.Repository.UserRepositoryJPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@RequestMapping(value = "/user/profile")
public class LdapBackendApplication {

	DirContext connection;

	//Controller controller = new Controller();

	public void Connection() {
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldaps://ep-dc01.eurecapro.tuc.gr:636");
		env.put(Context.SECURITY_PRINCIPAL, "cn=Pablo Baltuille,ou=staff,ou=personnel,ou=Domain User Accounts,dc=eurecapro,dc=tuc,dc=gr");
		env.put(Context.SECURITY_CREDENTIALS, "P@bl0!@#");

		System.out.println("llega aqui 1 ");

		try {
			connection = new InitialDirContext(env);
			System.out.println("prueba " + connection);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getAllUsers() {
		String searchFilter = "sAMAccountName=pbaltuille";
		String[] reqAtt = {"sAMAccountName", "sn", "givename", "iscHomeUniversity", "iscRoleInHomeUniversity", "iscRole", "iscExternalMailAddress"};
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(reqAtt);
		System.out.println("llega aqui 2 ");
		User user = new User();

		try {
			NamingEnumeration users = connection.search("ou=staff,ou=personnel,ou=Domain User Accounts,dc=eurecapro,dc=tuc,dc=gr", searchFilter, controls);
			SearchResult result = null;

			System.out.println("llega aqui 3 ");
			if (!users.hasMore()) System.out.println("users vacio");
			while (users.hasMore()) {
				result = (SearchResult) users.next();
				Attributes attr = result.getAttributes();
				System.out.println("llega aqui 4 ");
				System.out.println(attr.get("sAMAccountName"));
				System.out.println(attr.get("sn"));
				System.out.println(attr.get("givename"));
				System.out.println(attr.get("iscHomeUniversity"));
				System.out.println(attr.get("iscRoleInHomeUniversity"));
				System.out.println(attr.get("iscRole"));
				System.out.println(attr.get("iscExternalMailAddress"));

				user.setName("Pablo");
				user.setSurname(attr.get("sn").toString());
				user.setUniversity(attr.get("iscHomeUniversity").toString());
				user.setRole(attr.get("iscRoleInHomeUniversity").toString());
				user.setCardNumber("YYYYDDRRXXXX");
				user.setStatus("");
				user.setPicture("");
			}
			if (user.getName()==null) {System.out.println("user vacio");}
			else {
				System.out.println("user NAME: " + user.getName());
				System.out.println("user SURNAME: " + user.getSurname());
				System.out.println("user UNIVERSITY: " + user.getUniversity());
				System.out.println("user ROLE: " + user.getRole());
				System.out.println("user CARDNUMBER: " + user.getCardNumber());
				System.out.println("user STATUS: " + user.getStatus());
				System.out.println("user PICTURE: " + user.getPicture());
				//controller.postUser(user);
			}

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//SpringApplication.run(LdapBackendApplication.class, args);

		LdapBackendApplication app = new LdapBackendApplication();

		app.Connection();

		//app.getAllUsers();

		SpringApplication.run(LdapBackendApplication.class, args);

	}

	
	@PostMapping
    public void say() {
        System.out.println("axios post");
    }

	@Bean
	public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowCredentials(false);
            }
        };
    }

}
