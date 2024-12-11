package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.JoinDTO;
import com.example.demo.service.JoinService;

@RestController
public class JoinController {

	private final JoinService joinService;

	public JoinController(JoinService joinService) {
		this.joinService = joinService;
	}

	@PostMapping("/join")
	public String joinProcess(@RequestBody JoinDTO joinDTO) {
		return joinService.joinProcess(joinDTO) ? "ok" : "no";
	}

}
