package services.tman;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	@SuppressWarnings("resource")
	public static void main(String[] args) {		
		new ClassPathXmlApplicationContext("classpath:spring-beans.xml");
	}
}