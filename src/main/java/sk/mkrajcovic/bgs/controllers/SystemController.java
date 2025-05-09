package sk.mkrajcovic.bgs.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import sk.mkrajcovic.bgs.dto.PingDtoOut;

@RestController
public class SystemController {

	@GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public PingDtoOut ping() {
		return new PingDtoOut("OK");
	}

}

