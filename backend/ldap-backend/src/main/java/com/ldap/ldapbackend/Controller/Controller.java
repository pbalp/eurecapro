package com.ldap.ldapbackend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ldap.ldapbackend2.Model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.*;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

import javax.naming.Name;
import javax.naming.directory.Attributes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/ldap")
public class Controller {

	@GetMapping("/security")
	public String securityMethod()
	{
		return "secure rest endpoints with ldap";
	}

	@Autowired
	private Environment env;

	@Autowired
	private ContextSource contextSource;

	@Autowired
	private LdapTemplate ldapTemplate;

	/*public void authenticate(final String username, final String password) {
		contextSource.getContext("cn=" + username + ",ou=users," + env.getRequiredProperty("ldap.partitionSuffix"), password);
	}*/

	@GetMapping("/users")
	@ResponseBody
	public List<String> search(final String username) {
		System.out.println("Buscar");
		return ldapTemplate.search(
				"ou=users",
				"cn=" + username,
				(AttributesMapper<String>) attrs -> (String) attrs
				.get("cn")
				.get());
	}
	
	@GetMapping("/buscar")
	@ResponseBody
	public List<String> getAllPersonNames() {
	      return ldapTemplate.search(
	         query().where("objectclass").is("person"),
	         new AttributesMapper<String>() {
	            public String mapFromAttributes(Attributes attrs)
	               throws javax.naming.NamingException {
	               return attrs.get("cn").get().toString();
	            }
	         });
	   }
	
	@GetMapping("/user")
	@ResponseBody
	public String getUser() {
	
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User) {
			return ((User) principal).getUsername();
		} else {
			String[] query = principal.toString().toLowerCase().split("username");
			String username = query[query.length-1].split(";")[0];
			if (username.contains("=")) username = username.split("=")[1];
			return username;
		}
	}

	public void create(final String username, final String password) {
		System.out.println("Crear");
		Name dn = LdapNameBuilder
				.newInstance()
				.add("ou", "users")
				.add("cn", username)
				.build();
		DirContextAdapter context = new DirContextAdapter(dn);

		context.setAttributeValues("objectclass", new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
		context.setAttributeValue("cn", username);
		context.setAttributeValue("sn", username);
		context.setAttributeValue("userPassword", digestSHA(password));

		ldapTemplate.bind(context);
	}

	//@GetMapping("/users/")
	public void modify(final String username, final String password) {
		Name dn = LdapNameBuilder
				.newInstance()
				.add("ou", "users")
				.add("cn", username)
				.build();
		DirContextOperations context = ldapTemplate.lookupContext(dn);

		context.setAttributeValues("objectclass", new String[] { "top", "person", "organizationalPerson", "inetOrgPerson" });
		context.setAttributeValue("cn", username);
		context.setAttributeValue("sn", username);
		context.setAttributeValue("userPassword", digestSHA(password));

		ldapTemplate.modifyAttributes(context);
	}

	private String digestSHA(final String password) {
		String base64;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update(password.getBytes());
			base64 = Base64
					.getEncoder()
					.encodeToString(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return "{SHA}" + base64;
	}
}