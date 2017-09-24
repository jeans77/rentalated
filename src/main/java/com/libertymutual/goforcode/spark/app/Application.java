package com.libertymutual.goforcode.spark.app;

import static spark.Spark.*;

import com.libertymutual.goforcode.spark.app.controllers.ActivateController;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentApiController;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentController;
import com.libertymutual.goforcode.spark.app.controllers.HomeController;
import com.libertymutual.goforcode.spark.app.controllers.LikeController;
import com.libertymutual.goforcode.spark.app.controllers.SessionController;
import com.libertymutual.goforcode.spark.app.controllers.UserApiController;
import com.libertymutual.goforcode.spark.app.controllers.UserController;
import com.libertymutual.goforcode.spark.app.filters.SecurityFilters;

import com.libertymutual.goforcode.spark.app.utilities.SeedApp;

public class Application {

	public static void main(String[] args) {
		SeedApp.create();
		get("/", HomeController.myindex);
		
//		before("/*", 			SecurityFilters.checkIfSessionIsNew);
//		get("/", 				HomeController.index);
		get("/login",			SessionController.newForm);
//		before("/login", 		SecurityFilters.checkSubmittedCsrfToken);
		post("/login",			SessionController.create);
		post("/logout", 		SessionController.destroy);
		
	path("/users", () -> {
//		get("/signup",			UserController.newForm);
		get("/new", 			UserController.newForm);
//		before("new", 			SecurityFilters.checkSubmittedCsrfToken);
		post("/new", 			UserController.create);
	});
		
	path("/apartments", () -> {
		
//		before("new", 			SecurityFilters.checkSubmittedCsrfToken);
		before("new", 			SecurityFilters.isAuthenticated);
		get("/new", 			ApartmentController.newForm);
		
		before("", 				SecurityFilters.isAuthenticated);		
		post("", 				ApartmentController.create);
		
		before("mine", 			SecurityFilters.isAuthenticated);		
		get("/mine",    		ApartmentController.index);	
		
		get("/:id", 			ApartmentController.details);
		
//		before("/:id/likes", 	SecurityFilters.checkSubmittedCsrfToken);
		before("/:id/likes", 	SecurityFilters.isAuthenticated);
		post("/:id/likes", 		LikeController.create);
		
//		before("/:id/deactivations", SecurityFilters.checkSubmittedCsrfToken);
		before("/:id/deactivations", SecurityFilters.isAuthenticated);
		post("/:id/deactivations", 	ActivateController.update);
		
//		before("/:id/activations", 	SecurityFilters.checkSubmittedCsrfToken);
		before("/:id/activations", 	SecurityFilters.isAuthenticated);
		post("/:id/activations", 	ActivateController.update);

	});	
		
	path("/api", () -> {
		get("/apartments/:id", 		ApartmentApiController.details);
//		before("/apartments", 		SecurityFilters.checkSubmittedCsrfToken);
//		before("/apartments", 		SecurityFilters.isAuthenticated);
		post("/apartments", 		ApartmentApiController.create);
		get("/users/:id", 			UserApiController.details);
//		before("/apartments", 		SecurityFilters.checkSubmittedCsrfToken);
		post("/users", 				UserApiController.create);
	});
	
	}
}
