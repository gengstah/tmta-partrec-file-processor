package services.tman;

import org.quartz.SchedulerException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
	public static void main(String[] args) throws SchedulerException {		
		new ClassPathXmlApplicationContext("classpath:spring-beans.xml");
	}
}