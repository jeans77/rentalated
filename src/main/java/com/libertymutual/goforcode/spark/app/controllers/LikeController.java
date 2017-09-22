package com.libertymutual.goforcode.spark.app.controllers;

import org.javalite.activejdbc.Model;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;

import spark.Request;
import spark.Response;
import spark.Route;

public class LikeController extends Model {
	
	public static final Route create = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User user = req.session().attribute("currentUser");
//			Apartment apartment = Apartment.findById(Long.value(req.params("id")));
			Apartment apartment = Apartment.findById(Long.valueOf(req.params("id")));
			apartment.add(user);
		}
		res.redirect("/apartments/" + req.params("id"));
		return "";
	};
}
