package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;


public class UserController {
	
	public static final Route newForm = (Request req, Response res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		System.out.println("UsernewForm Current : " + (req.session().attribute("currentUser")));
		System.out.println("UsernewForm noUser  : " + (req.session().attribute("currentUser") == null));
		return MustacheRenderer.getInstance().render("session/signup.html", model);
	};
	
	public static final Route create = (Request req, Response res) -> {
		String encryptedPassword = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
		System.out.println("UserCreate Current : " + (req.session().attribute("currentUser")));
		System.out.println("UserCreate noUser  : " + (req.session().attribute("currentUser") == null));
		User user = new User(
				req.queryParams("email"),
				encryptedPassword,
				req.queryParams("first_name"),
				req.queryParams("last_name")
				);

			try (AutoCloseableDb db = new AutoCloseableDb()) {
			user.saveIt();
			req.session().attribute("currentUser", user);
			req.session().attribute("pwMessage", " ");
			res.redirect("/");
			return "";
		}
	};
}
