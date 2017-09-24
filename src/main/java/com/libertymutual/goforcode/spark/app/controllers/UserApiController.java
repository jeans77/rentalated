package com.libertymutual.goforcode.spark.app.controllers;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.JsonHelper;

import static spark.Spark.notFound;

import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import spark.Request;
import spark.Response;
import spark.Route;


public class UserApiController {

	public static final Route details = (Request req, Response res) -> {
		try	(AutoCloseableDb db = new AutoCloseableDb()){
			String idAsString =  req.params("id");
			int id = Integer.parseInt(idAsString);
			User user = User.findById(id);
//			Apartment apartment = Apartment.findById(id);
			
			if (user != null) {
				res.header("Content-Type", "application/json");
				return user.toJson(true);
			}
			notFound("Did not find that user");
			return "";
		}
	};
	
	public static Route create = (Request req, Response res)  -> {
		String json = req.body();
		Map map = JsonHelper.toMap(json);
		User user = new User();
		user.fromMap(map);
		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
		
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			user.saveIt();
			res.status(201);

		}
		return user.toJson(true);
	};
}
