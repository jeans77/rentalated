package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.LazyList;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.JsonHelper;

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentApiController {

	public static final Route details = (Request req, Response res) -> {
		int id = Integer.parseInt(req.params("id"));
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			res.header("Content-Type", "application/json");
			return Apartment.findById(id).toJson(true);
		}
	};

	public static final Route create = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = new Apartment();
			apartment.fromMap(JsonHelper.toMap(req.body()));
			apartment.saveIt();
			res.status(201);
			return apartment.toJson(true);
		}
	};

	public static final Route index = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			LazyList<Apartment> apartments = Apartment.where("is_active = ?", true);
			res.header("Content-Type", "application/json");
			return apartments.toJson(true);
		}
	};
	
	public static final Route myindex = (Request req, Response res) -> {
		User currentUser = req.session().attribute("currentUser");
		long Id = (long) currentUser.getId();
		System.out.println("Id: " + Id);
		try (AutoCloseableDb db = new AutoCloseableDb()) {
		LazyList<Apartment> myListings = Apartment.where("user_id = ?", Id);
//		LazyList<Apartment> myActiveListings = Apartment.where("user_id = ? and is_active = ?", id, true);
//		LazyList<Apartment> myInactiveListings = Apartment.where("user_id = ? and is_active = ?", id, false);
		res.header("Content-Type", "application/json");
		return myListings.toJson(true);

		}
	};

	public static Route activate = (Request req, Response res) -> {
		System.out.println("Id activate: " + req.params("id"));
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			String idAsString = req.params("id");
			int id = Integer.parseInt(idAsString);
			Apartment apartment = Apartment.findById(id);
			apartment.set("is_active", true);
			apartment.saveIt();
//			res.status(201);
//			res.redirect("/apartment/" + id);
			res.header("Content-Type", "application/json");
			return apartment.toJson(true);
		}
	};
	
	public static Route deactivate = (Request req, Response res) -> {
		System.out.println("Id: deactivate" + req.params("id"));
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			String idAsString = req.params("id");
			int id = Integer.parseInt(idAsString);
			Apartment apartment = Apartment.findById(id);
			apartment.set("is_active", false);
			apartment.saveIt();
//			res.status(201);
			res.header("Content-Type", "application/json");
			return apartment.toJson(true);
		}
		
	};
}
