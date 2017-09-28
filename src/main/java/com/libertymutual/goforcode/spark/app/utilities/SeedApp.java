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

		User user = new User("I@ibm.com", encrytedPassword, "I", "IBM");
				user.saveIt();
		Apartment apartment = new Apartment(600, 4, 4.0, 600, "444 Main Str4", "San Francisco", "CA", 95104, true);
				user.add(apartment);
				apartment.saveIt();
				
				apartment = new Apartment(700, 5, 5.0, 700, "555 Main Str5", "San Francisco", "CA", 95105, true);
				user.add(apartment);
				apartment.saveIt();

		
		user = new User("jean@lm.com", encrytedPassword, "Jean", "S");
				user.saveIt();
		
				apartment = new Apartment(300, 1, 1.0, 300, "111 Main Str1", "San Francisco", "CA", 95101, true);
				user.add(apartment);
				apartment.saveIt();
		
				apartment = new Apartment(400, 2, 2.0, 400, "222 Main Str2", "San Francisco", "CA", 95102, false);
				user.add(apartment);
				apartment.saveIt();
		
				apartment = new Apartment(500, 3, 3.0, 500, "333 Main Str3", "San Francisco", "CA", 95103, false);
				user.add(apartment);
				apartment.saveIt();
		}
	}
	
}
