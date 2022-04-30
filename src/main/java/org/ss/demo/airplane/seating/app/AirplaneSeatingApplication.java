package org.ss.demo.airplane.seating.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.ss.demo.airplane.seating.io.ReadFromConsole;

@SpringBootApplication(scanBasePackages = {"org.ss.demo.airplane.seating"})
public class AirplaneSeatingApplication {

	
	public static void main(String[] args) {	
		SpringApplication.run(AirplaneSeatingApplication.class, args);		
	}
	
	@Autowired
	public void context(ApplicationContext context) {
		ReadFromConsole readFromConsole=context.getBean(ReadFromConsole.class);		
		readFromConsole.mainMenu();
	}

}
