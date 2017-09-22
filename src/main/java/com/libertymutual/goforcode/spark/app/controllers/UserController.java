package com.libertymutual.goforcode.spark.app.controllers;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class UserController {

	public static final Route newForm = (Request req, Response res) -> {
		return MustacheRenderer.getInstance().render("users/newForm.html", null);
	};

	public static Route create = (Request req, Response res) -> {
		String email = req.queryParams("email");
		String password = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
		String firstName = req.queryParams("first_name");
		String lastName = req.queryParams("last_name");
		
		User user  = new User (email, password, firstName, lastName);
		
		if (user != null) {
			
			try (AutoCloseableDb db = new AutoCloseableDb()) {
				user.saveIt();
				req.session().attribute("currentUser", user);
			}
		}
		res.redirect("/");
		return "";
	};
	
	
//	public static final Route create = (Request req, Response res) -> {
//		Map<String, String> map = req.queryMap("user")
//				.toMap()
//				.entrySet()
//				.stream()
//				.map(entry -> new AbstractMap.SimpleEntry<String, String>(entry.getKey(), entry.getValue()[0]))
//				.peek(entry -> entry.setValue(entry.getKey() == "password"
//								? BCrypt.hashpw(entry.getValue(), BCrypt.gensalt())
//								: entry.getValue()))
//				.collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
//		User user = new User();
//		user.fromMap(map);
//		try (AutoCloseableDb db = new AutoCloseableDb()) {
//			user.saveIt();
//			req.session().attribute("currentUser", user);
//			res.redirect("/");
//			return "";
//		}
//	};

}