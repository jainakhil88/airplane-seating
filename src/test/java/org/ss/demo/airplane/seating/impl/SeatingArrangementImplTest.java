package org.ss.demo.airplane.seating.impl;

import static org.mockito.Mockito.withSettings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.ss.demo.airplane.seating.arrange.SeatingArrangement;
import org.ss.demo.airplane.seating.arrange.impl.SeatingArrangementImpl;

@ExtendWith(SpringExtension.class)
public class SeatingArrangementImplTest{

	@Test
	public void testConstructor() {
		Mockito.mock(SeatingArrangementImpl.class, withSettings().useConstructor());
	}
	
	@Test
	public void testArrange() {
		String input="[[3,2],	[4,3],	[2,3],	[3,4]] 32";		
		SeatingArrangement seat=new SeatingArrangementImpl();
		seat.arrange(input);
	}
	
	@Test
	public void testPrint() {
		SeatingArrangementImpl seat=new SeatingArrangementImpl();
		seat.print();
	}
	
}
