package com.libertymutual.goforcode.spark.app.filters;

import static spark.Spark.halt;

import java.util.UUID;

import spark.Filter;
import spark.Request;
import spark.Response;

public class SecurityFilters {
	public static Filter isAuthenticated = (Request req, Response res) -> {
		if (req.session().attribute("currentUser") == null) {
			if (req.pathInfo().equals("/apartments/mine")) {
				res.redirect("/login");
				halt();
			}
			res.redirect("/login?returnPath=" + req.pathInfo());
			halt();
		}	
	};

		
	private static UUID newCSRF() {
		UUID newCSRF = UUID.randomUUID();
		return newCSRF;
	}
	
	public static UUID getNewCSRF() {
		return newCSRF();
	}
	
	
	public static final Filter checkSubmittedCsrfToken = (Request req, Response res) -> {
		if (req.requestMethod() == "POST") {
			String serverToken = req.session().attribute("csrf_token");
			String submittedToken = req.queryParams("csrf");
			System.out.println("Server: " + serverToken);
			System.out.println("Submitted: " + submittedToken);

			if (!serverToken.equals(submittedToken)) {
				res.redirect("/");
				System.out.println("Tokens are not the same");
				halt(404);
			} else {
				System.out.println("Tokens are the same");
			}
		};

	};
}
