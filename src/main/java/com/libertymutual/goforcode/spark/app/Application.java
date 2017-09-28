package com.libertymutual.goforcode.spark.app;

import static spark.Spark.*;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.controllers.ActivateController;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentApiController;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentController;
import com.libertymutual.goforcode.spark.app.controllers.HomeController;
import com.libertymutual.goforcode.spark.app.controllers.LikeController;
import com.libertymutual.goforcode.spark.app.controllers.SessionApiController;
import com.libertymutual.goforcode.spark.app.controllers.SessionController;
import com.libertymutual.goforcode.spark.app.controllers.UserApiController;
import com.libertymutual.goforcode.spark.app.controllers.UserController;
import com.libertymutual.goforcode.spark.app.filters.SecurityFilters;

import com.libertymutual.goforcode.spark.app.utilities.SeedApp;

public class Application {

	
	
	public static void main(String[] args) {
		
		enableCORS("http://localhost:4200", "*", "*");
		
		
		SeedApp.create();
		
		get("/", HomeController.index);

		get("/login",			SessionController.newForm);
		post("/login",			SessionController.create);
		post("/logout", 		SessionController.destroy);
		
	
		get("/users/new", 		UserController.newForm);
		post("users/new", 		UserController.create);

		
	path("/apartments", () -> {
		
		before("/new", 			SecurityFilters.isAuthenticated);
		get("/new", 			ApartmentController.newForm);
		
		before("/mine", 			SecurityFilters.isAuthenticated);		
		get("/mine",    		ApartmentController.index);	
		
		get("/:id", 			ApartmentController.details);
		
		before("/:id/likes", 	SecurityFilters.isAuthenticated);
		post("/:id/likes", 		LikeController.create);
		
		before("/:id/deactivations", SecurityFilters.isAuthenticated);
		post("/:id/deactivations", 	ActivateController.update);
		
		before("/:id/activations", 	SecurityFilters.isAuthenticated);
		post("/:id/activations", 	ActivateController.update);
		
		before("", 				SecurityFilters.isAuthenticated);		
		post("", 				ApartmentController.create);

	});	
		
	path("/api", () -> {
		get("/apartments/mine",		ApartmentApiController.myindex);
		get("/apartments/:id", 		ApartmentApiController.details);
//		before("/apartments", 		SecurityFilters.isAuthenticated);
		post("/apartments", 		ApartmentApiController.create);
		get("/apartments", 			ApartmentApiController.index);

		get("/users/:id", 			UserApiController.details);
		post("/users", 				UserApiController.create);
		post("/sessions", 			SessionApiController.create);
		delete("/sessions/mine",	SessionApiController.destroy);

	});
	
	}
	
	private static void enableCORS(final String origin, final String methods, final String headers) {

	    options("/*", (request, response) -> {

	        String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
	        if (accessControlRequestHeaders != null) {
	            response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
	        }

	        String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
	        if (accessControlRequestMethod != null) {
	            response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
	        }

	        return "OK";
	    });

	    before((request, response) -> {
	        response.header("Access-Control-Allow-Origin", origin);
	        response.header("Access-Control-Request-Method", methods);
	        response.header("Access-Control-Allow-Headers", headers);
	        response.header("Access-Control-Allow-Credentials", "true");
	        // Note: this may or may not be necessary in your particular application
//	        response.type("application/json");
	    });
	}
}
