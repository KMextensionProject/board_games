package sk.mkrajcovic.bgs.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

	@GetMapping("/home")
	public String getCreatePage() {
		return "/index.html";
	}

	@GetMapping("/detail")
	public String getDetailPage() {
		return "/detail.html";
	}

}
