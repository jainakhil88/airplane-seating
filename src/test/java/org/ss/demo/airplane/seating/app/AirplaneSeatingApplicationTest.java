package org.ss.demo.airplane.seating.app;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.ss.demo.airplane.seating.io.ReadFromConsole;


@ExtendWith(SpringExtension.class)
public class AirplaneSeatingApplicationTest {

	@InjectMocks
	private AirplaneSeatingApplication airplaneSeatingApplication;
	
	@Autowired
    ApplicationContext context;
	
	@Test
	public void testContext() {
		ApplicationContext context = mock(ApplicationContext.class);
		ReadFromConsole readerFromConsole = mock(ReadFromConsole.class);
		when(context.getBean(ReadFromConsole.class)).thenReturn(readerFromConsole);

		airplaneSeatingApplication.context(context);
		
		Mockito.verify(readerFromConsole, Mockito.atLeastOnce()).mainMenu();;
	}

}
