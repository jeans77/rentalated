package com.libertymutual.goforcode.spark.app;

import static spark.Spark.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentApiController;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentController;
import com.libertymutual.goforcode.spark.app.controllers.HomeController;
import com.libertymutual.goforcode.spark.app.controllers.SessionController;
import com.libertymutual.goforcode.spark.app.controllers.UserController;
import com.libertymutual.goforcode.spark.app.filters.SecurityFilters;
import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;

public class Application {

	public static void main(String[] args) {

		String encryptedPassword = BCrypt.hashpw("password", BCrypt.gensalt());
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User.deleteAll();
			User curtis = new User("curtis@tir.com", encryptedPassword, "Curtis", "S");
			curtis.saveIt();
			User jean = new User("jean@lm.com", encryptedPassword, "Jean", "S");
			jean.saveIt();
			User irma = new User("irma@hm.com", encryptedPassword, "Irma", "M");
			irma.saveIt();
			
			Apartment.deleteAll();

			Apartment apartment1 = new Apartment(300, 1, 1, 350, "111 Main Str1", "San Francisco", "CA", "95101",1, false);
			apartment1.saveIt();
			curtis.add(apartment1);
			
			Apartment apartment2 = new Apartment(400, 2, 2, 450, "222 Main Str2", "San Francisco", "CA", "95102",2, false);
			apartment2.saveIt();
			jean.add(apartment2);
			
			Apartment apartment3 = new Apartment(500, 3, 3, 550, "333 Main Str3", "San Francisco", "CA", "95103",3, false);
			apartment3.saveIt();
			jean.add(apartment3);
		}
		
		path("/apartments", () -> {

			before("/new", SecurityFilters.isAuthenticated);
			get("/new", 	ApartmentController.newForm);
			
			before ("/mine, SecurityFilters.isAuthenticated");
			get("/mine",    ApartmentController.index);
			
			get("/:id", 	ApartmentController.details);
			
			before("", SecurityFilters.isAuthenticated);
			post("",        ApartmentController.create);
		});
		
		get("/", 				HomeController.index);
		
		get("/login",			SessionController.newForm);
//		get("/logout", 			SessionController.destroy);
		post("/logout", 		SessionController.destroy);
		post("/login",			SessionController.create);
		
		
		get("/signup",			UserController.newForm);
		get("/users/new",		UserController.newForm);
		post("/users",			UserController.create);
		
		get("/signup",			ApartmentController.newForm);
		get("/users/new",		ApartmentController.details);
		post("/users",			ApartmentController.create);
		post("/users",			ApartmentController.index);
		
		
		path("/api", () -> {
			get("/apartments/:id", 	ApartmentApiController.details);
			post("/apartments", 	ApartmentApiController.create);
		});
	}
}
