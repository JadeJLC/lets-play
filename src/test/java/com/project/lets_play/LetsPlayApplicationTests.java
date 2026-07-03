package com.project.lets_play;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class LetsPlayApplicationTests {

	@Test
	void contextLoads() {
	}

	public PasswordEncoder createPasswordEncoder() {
		PasswordEncoder passwordEncoder =
		PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return passwordEncoder;
	}
}

