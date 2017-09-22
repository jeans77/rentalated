package com.libertymutual.goforcode.spark.app.utilities;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.ApartmentsUsers;
import com.libertymutual.goforcode.spark.app.models.User;

public class SeedApp {

	public static void create() {
	
	String password = "password";
	String encrytedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
	
	try (AutoCloseableDb db = new AutoCloseableDb()) {
		User.deleteAll();
		Apartment.deleteAll();
		ApartmentsUsers.deleteAll();

		User user = new User("jean@lm.com", encrytedPassword, "Jean", "S");
		user.saveIt();
			user = new User("irma@ibm.com", encrytedPassword, "Irma", "Maria");
		user.saveIt();
		
		Apartment apartment = new Apartment(300, 1, 1.0, 350, "111 Main Str1", "San Francisco", "CA", 95101, false);
				user.add(apartment);
				apartment.saveIt();
		
				apartment = new Apartment(400, 2, 2.0, 450, "222 Main Str2", "San Francisco", "CA", 95102, false);
				user.add(apartment);
				apartment.saveIt();
		
				apartment = new Apartment(500, 3, 3.0, 550, "333 Main Str3", "San Francisco", "CA", 95103, false);
				user.add(apartment);
				apartment.saveIt();
		}
	}
	
}
