package com.ldap.ldapbackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import com.ldap.ldapbackend.Config.Config;
import com.ldap.ldapbackend.Model.CardUser;
import com.ldap.ldapbackend.Model.User;
import com.ldap.ldapbackend.Repository.UserRepositoryCRUD;
import com.ldap.ldapbackend.Repository.UserRepositoryJPA;
import com.ldap.ldapbackend.Service.CardUserService;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.ldap.NamingException;
//import org.springframework.ldap.core.*;
//import org.springframework.ldap.support.LdapNameBuilder;
//import org.springframework.security.core.context.SecurityContextHolder;
//import static org.springframework.ldap.query.LdapQueryBuilder.query;

//import javax.naming.Name;
//import javax.naming.directory.Attributes;

//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Base64;
//import java.util.List;

@RestController
@EnableJpaRepositories("com.ldap.ldapbackend.Repository")
//@RequestMapping("/ldap")
@RequestMapping("/user")
@CrossOrigin(origins = "*") //http://10.17.8.45:19006
public class Controller {

	@GetMapping("/security")
	public String securityMethod()
	{
		return "secure rest endpoints with ldap";
	}

	//@Autowired
	//private Environment env;

	//@Autowired
	//private ContextSource contextSource;

	//@Autowired
	//private LdapTemplate ldapTemplate;

	@Autowired
	private UserRepositoryJPA userRepositoryJPA;

	@Autowired
	private CardUserService cardUserService;

	private UserRepositoryCRUD userRepositoryCRUD;
	private Config config;

	//private final User user = null;

    public Controller(UserRepositoryJPA userRepositoryJPA) { //, UserRepositoryCRUD userRepositoryCRUD
        this.userRepositoryJPA = userRepositoryJPA;
		//this.userRepositoryCRUD = userRepositoryCRUD;
    }

	//public Controller() {}

	//@PostMapping(path="/user")
	public String postUserDatabase(String name, String surname, String email, String university, String role, String status, String cardNumber, String picture) { //@RequestBody
    	User user = new User(name, surname, email, university, role, status, cardNumber, picture);
		userRepositoryCRUD.save(user);
		return "Saved";
  	}


	@PostMapping(
		value = "/profile", consumes = "application/json", produces = "application/json")
	public User postUser(@RequestBody User user) {
		System.out.println("user name " + user.getName());
		if (user.getCardNumber().isEmpty() || user.getCardNumber()==null) {
			user.setCardNumber(generateCardNumber(user.getEmail(), user.getRole(), user.getUniversity()));
		}
		System.out.println("user post");
		if (userRepositoryJPA==null) System.out.println("userRepositoryJPA null");
		return userRepositoryJPA.save(user);
	}

	@GetMapping("/{username}")
    public User getUserDatabase(@PathVariable String username) {
		System.out.println("USER: " + username);
		User user = userRepositoryCRUD.findById(username).get();
        return user;
    }

	@GetMapping("/login")
	public void authenticateUser(@PathVariable String email, @PathVariable String password) { // @PathVariable String role,
		System.out.println("authenticate user");
		config.Authenticate(email, password); // role,

		User user = config.getUser(email);

		System.out.println("post User " + postUser(user).getName());
	}

	public String generateCardNumber(String email, String role, String university) {
		String cardNumber = cardUserService.getCardUser(email, role).getCardNumber();
		if (cardNumber.isEmpty() || cardNumber==null) {
			int expirationDate = LocalDate.now().getYear();
			if (role.contains("alum") || role.contains("student")) expirationDate+=5;
			else expirationDate+=50;
			int lastIndex = cardUserService.getCardUserByUniversity(university).size()-1;
			int autoincrement = Integer.parseInt(cardUserService.getCardUserByUniversity(university).get(lastIndex).getCardNumber().substring(7, 11)) + 1;
			cardNumber = String.valueOf(expirationDate) + getUniversityCode(university) + getRoleCode(role) + String.valueOf(autoincrement);
			cardUserService.saveCardUser(new CardUser(email, role, cardNumber, university));
		}
		return cardNumber;
	}

	public String getUniversityCode(String university) {
		switch (university) {
			case "MUL": return "00";
			case "TU BAF": return "01";
			case "UP": return "02";
			case "ULE": return "03";
			case "TUC": return "04";
			case "SUT": return "05";
			case "HSMW": return "06";
			default: return null;
		}
	}

	public String getRoleCode(String role) {
		switch(role) {
			case "student": return "00";
			case "faculty": return "01";
			case "staff": return "02";
			case "affiliate": return "03";
			case "alum": return "04";
			default: return null;
		}
	}

	/*public void authenticate(final String username, final String password) {
		contextSource.getContext("cn=" + username + ",ou=users," + env.getRequiredProperty("ldap.partitionSuffix"), password);
	}*/

	/*@GetMapping("/users")
	@ResponseBody
	public List<String> search(final String username) {
		System.out.println("Buscar");
		return ldapTemplate.search(
				"ou=users",
				"cn=" + username,
				(AttributesMapper<String>) attrs -> (String) attrs
				.get("cn")
				.get());
	}*/
	
	/*@GetMapping("/user")
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
	}*/
	
	/*@GetMapping("/user")
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
	}*/

	/*private String digestSHA(final String password) {
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
	}*/
}