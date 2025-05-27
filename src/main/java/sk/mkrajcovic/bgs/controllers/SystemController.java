package sk.mkrajcovic.bgs.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import sk.mkrajcovic.bgs.api.SystemApi;
import sk.mkrajcovic.bgs.dto.PingDtoOut;

@RestController
class SystemController implements SystemApi {

	@GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
	public PingDtoOut ping() {
		return new PingDtoOut("OK");
	}

}

