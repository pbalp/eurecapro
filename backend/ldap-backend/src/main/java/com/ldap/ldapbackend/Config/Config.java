package com.ldap.ldapbackend.Config;

//import java.util.Arrays;

import javax.naming.directory.DirContext;

import com.ldap.ldapbackend.Controller.Controller;
import com.ldap.ldapbackend.Model.User;

//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
//import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

import javax.naming.*;
import javax.naming.directory.*;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Config extends WebSecurityConfigurerAdapter {

	Controller controller; 

	DirContext connection;

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

	public void Authenticate(String fullName, String password) { // String role,
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldaps://ep-dc01.eurecapro.tuc.gr:636");
		env.put(Context.SECURITY_PRINCIPAL, "cn="+fullName+",ou=Domain User Accounts,dc=eurecapro,dc=tuc,dc=gr"); //ou="+role+",
		env.put(Context.SECURITY_CREDENTIALS, password);

		System.out.println("llega aqui 1 ");

		try {
			connection = new InitialDirContext(env);
			System.out.println("prueba " + connection);
			//if (connection.bin)
		} catch (AuthenticationException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getAllUsers() {
		String searchFilter = "iscHomeUniversity=TUC";
		String[] reqAtt = {"sAMAccountName", "sn", "givename", "iscHomeUniversity", "iscRoleInHomeUniversity", "iscRole", "iscExternalMailAddress"};
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(reqAtt);
		System.out.println("llega aqui 2 ");

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
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public User getUser(String email) {
		String searchFilter = "iscExternalMailAddress="+email;
		String[] reqAtt = {"sAMAccountName", "sn", "givename", "iscHomeUniversity", "iscRoleInHomeUniversity", "iscRole", "iscExternalMailAddress"};
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(reqAtt);
		System.out.println("llega aqui 2 ");
		String[] ous = {"ou=alumni", "ou=staff", "ou=personnel", "ou=students"};

		User user = new User();

		try {
			NamingEnumeration users = null;
			
			for (int i=0; i<ous.length;i++) {
				users = connection.search("ou="+ous[i]+",ou=Domain User Accounts,dc=eurecapro,dc=tuc,dc=gr", searchFilter, controls);
				
				System.out.println("llega aqui 3 ");
				if (users.hasMore()) break;
			}
			
			SearchResult result = null;
			
			System.out.println("users vacio");
			while (users.hasMore()) {
				result = (SearchResult) users.next();
				Attributes attr = result.getAttributes();
				user.setName(attr.get("givename").toString());
				user.setSurname(attr.get("sn").toString());
				user.setEmail(attr.get("iscExternalMailAddress").toString());
				user.setUniversity(attr.get("iscHomeUniversity").toString());
				user.setRole(attr.get("iscRole").toString());
				user.setCardNumber(attr.get("employeeId").toString());

				/*System.out.println("llega aqui 4 ");
				System.out.println(attr.get("sAMAccountName"));
				System.out.println(attr.get("sn"));
				System.out.println(attr.get("givename"));
				System.out.println(attr.get("iscHomeUniversity"));
				System.out.println(attr.get("iscRoleInHomeUniversity"));
				System.out.println(attr.get("iscRole"));
				System.out.println(attr.get("iscExternalMailAddress"));*/
			}
			//controller.postUser(user);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	/*@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin();
	}*/
	
	/*@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication().userDnPatterns("pbaltuille").groupSearchBase("ou=unileon")
				.contextSource(contextSource()).passwordCompare().passwordEncoder(new LdapShaPasswordEncoder())
				.passwordAttribute("P@bl0!@#");
	}*/

	/*@Bean
	public DefaultSpringSecurityContextSource contextSource() {
		return new DefaultSpringSecurityContextSource(Arrays.asList("ldaps://147.27.47.5:636/"),
				"dc=eurecapro,dc=tuc,dc=gr");
	}*/
}