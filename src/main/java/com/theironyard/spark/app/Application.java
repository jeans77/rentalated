package com.theironyard.spark.app;

import static spark.Spark.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.theironyard.spark.app.controllers.ApartmentApiController;
import com.theironyard.spark.app.controllers.ApartmentController;
import com.theironyard.spark.app.controllers.HomeController;
import com.theironyard.spark.app.controllers.SessionController;
import com.theironyard.spark.app.filters.SecurityFilters;
import com.theironyard.spark.app.models.Apartment;
import com.theironyard.spark.app.models.User;
import com.theironyard.spark.app.utilities.AutoCloseableDb;
import com.theironyard.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;

public class Application {

	public static void main(String[] args) {

		String encryptedPassword = BCrypt.hashpw("password", BCrypt.gensalt());
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User.deleteAll();
			new User("curtis.schlak@theironyard.com", encryptedPassword, "Curtis", "Schlak").saveIt();
			new User("jeans771@hotmail.com", encryptedPassword, "Jean", "S").saveIt();
			Apartment.deleteAll();
			new Apartment(6200, 1, 0, 350, "123 Main Str", "San Francisco", "CA", "95125").saveIt();
			new Apartment(550, 1, 0, 350, "456 Other Str", "San Francisco", "CA", "77006").saveIt();
			new Apartment(123, 1, 0, 350, "250 SW 196th Place", "Seattle", "WA", "98101").saveIt();
		};

		path("/apartments", () -> {

			before("/new", SecurityFilters.isAuthenticated);
			get("/new", 	ApartmentController.newForm);
			get("/:id", 	ApartmentController.details);
			
			before("", SecurityFilters.isAuthenticated);
			post("",        ApartmentController.create);
		});
		
		get("/", 				HomeController.index);
		get("/login",			SessionController.newForm);
		post("/login",			SessionController.create);
		
		path("/api", () -> {
			get("/apartments/:id", 	ApartmentApiController.details);
			post("/apartments", 	ApartmentApiController.create);
		});
	}
}
