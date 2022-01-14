package com.ldap.ldapbackend;

import java.util.Properties;

import javax.naming.*;
import javax.naming.directory.*;
//import javax.naming.Context;
//import javax.naming.NamingException;
//import javax.naming.directory.DirContext;
//import javax.naming.directory.InitialDirContext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LdapBackendApplication {

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

	public static void main(String[] args) {
		//SpringApplication.run(LdapBackendApplication.class, args);

		LdapBackendApplication app = new LdapBackendApplication();

		app.Connection();

		app.getAllUsers();

	}

}
