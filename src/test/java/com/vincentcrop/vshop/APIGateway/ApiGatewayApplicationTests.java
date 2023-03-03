package com.vincentcrop.vshop.APIGateway;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiGatewayApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void Test()
	{
		String path = "/user/1/ok";

		assertTrue(Pattern.matches("/user/\\d/ok", path));
	}

}
