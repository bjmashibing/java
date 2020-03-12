package org.springframework.context.annotation6;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.tests.sample.beans.TestBean;

@Configuration
public class ConfigForScanning {
	@Bean
	public TestBean testBean() {
		return new TestBean();
	}
}
