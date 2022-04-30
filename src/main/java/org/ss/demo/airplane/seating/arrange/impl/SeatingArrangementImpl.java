package org.ss.demo.airplane.seating.arrange.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.ss.demo.airplane.seating.arrange.SeatingArrangement;
import org.ss.demo.airplane.seating.model.Seat;
import org.ss.demo.airplane.seating.model.SeatingPlacement;

import de.vandermeer.asciitable.AT_Cell;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciithemes.a7.A7_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

@Component
public class SeatingArrangementImpl implements SeatingArrangement{

	private static final String SEATING_ARRANGEMENT = "SEATING ARRANGEMENT";
	private static final String SEATING_STATUS = "SEATING STATUS";
	private static final String NO_INPUT_RECIEVED = "Plese enter some data to print the seating arrangement.";
	private static final String NO_VACANT_SEATS = "All passengers occupied, no seats available on the plane.";
	private static final String VACANT_OCCUPANCY_MESSAGE = "All of %d passengers have been occupied and there are %d seats available on the plane.";
	private static final String MAXIMUM_OCCUPANCY_MESSAGE = "Out of %d passengers only %d where allocated seats and there are no more seats available.";
	private static final String REGEX_ARRAY_SIZE_PATTERN = "(\\d)+,(\\d)+";
	private static final int REPORT_PADDING_WIDTH = 2;
	
	private static final String SEAT_SEPARATOR = " ";
	private static final String COMMA_SEPARATOR = ",";

	private boolean isCleaned;

	private long noOfPassengers;
	private List<Seat> listOfSeats;

	private long maxRowSize;
	private long maxColSize;
	private long maxLaneSize;

	private Map<Seat, Seat> seatFinderMap;

	private List<long[]>laneSizeList;

	public SeatingArrangementImpl() {
		isCleaned = true;
	}

	public void arrange(String input) {
		cleanUp();
		isCleaned = false;

		String seatPlanText=input.substring(0, input.lastIndexOf(SEAT_SEPARATOR)).trim();
		noOfPassengers=Long.parseLong(input.substring(input.lastIndexOf(SEAT_SEPARATOR)).trim());

		Pattern pattern = Pattern.compile(REGEX_ARRAY_SIZE_PATTERN, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(seatPlanText);

		laneSizeList=new ArrayList<long[]>();
		listOfSeats=new ArrayList<Seat>();
		Queue<Seat> aisleQueue = new LinkedList<>();
		Queue<Seat> windowQueue = new LinkedList<>();
		Queue<Seat> centerQueue = new LinkedList<>();		
		seatFinderMap=new HashMap<Seat,Seat>();


		while (matcher.find()) {
			long[] numbers = Arrays.stream(matcher.group().split(COMMA_SEPARATOR)).mapToLong(Long::parseLong).toArray();
			laneSizeList.add(numbers);
		}

		maxLaneSize=laneSizeList.size();
		maxRowSize=-1;		
		maxColSize=-1;


		for(int laneNumber=0;laneNumber<maxLaneSize;laneNumber++) {
			long rowSize=laneSizeList.get(laneNumber)[1];
			long colSize=laneSizeList.get(laneNumber)[0];

			maxRowSize = rowSize>=maxRowSize?rowSize:maxRowSize;
			maxColSize = colSize>=maxColSize?colSize:maxColSize;

			for(int row=0;row<rowSize;row++) {				

				for(int col=0;col<colSize;col++) {
					Seat seat=new Seat();
					seat.setLane(laneNumber);
					seat.setRow(row);
					seat.setColumn(col);
					setSeatPlacement(seat, col, colSize, laneNumber, maxLaneSize);												

					listOfSeats.add(seat);
					seatFinderMap.put(seat, seat);
				}
			}
		}


		for(int row=0;row<maxRowSize;row++) {
			for(int laneNumber=0;laneNumber<maxLaneSize;laneNumber++) {
				long rowSizeOfPartLane=laneSizeList.get(laneNumber)[1];
				long colSizeofPartLane=laneSizeList.get(laneNumber)[0];

				for(int col=0;col<colSizeofPartLane;col++) {

					if(row>=rowSizeOfPartLane || col>=colSizeofPartLane) {
						continue;
					}

					Seat queryObject=new Seat();
					queryObject.setRow(row);
					queryObject.setColumn(col);
					queryObject.setLane(laneNumber);

					Seat responseSeat=seatFinderMap.get(queryObject);

					if(responseSeat!=null) {						
						if(responseSeat.getSeatPlacement()==SeatingPlacement.AISLE) {
							aisleQueue.add(responseSeat);						
						}else if(responseSeat.getSeatPlacement()==SeatingPlacement.WINDOW) {					
							windowQueue.add(responseSeat);
						}else {
							centerQueue.add(responseSeat);
						}						
					}
				}

			}
		}

		boolean haveSeats = listOfSeats.stream().anyMatch((obj) -> !obj.isOccupied());

		for(long i=0;i<noOfPassengers && haveSeats;i++) {

			if(aisleQueue.size()>0) {
				Seat aisle=aisleQueue.remove();
				aisle.setPassengerNumber((i+1));
				aisle.setOccupied(true);
			}else if(windowQueue.size()>0) {
				Seat window=windowQueue.remove();					
				window.setPassengerNumber((i+1));
				window.setOccupied(true);
			}else {
				Seat center=centerQueue.remove();					
				center.setPassengerNumber((i+1));
				center.setOccupied(true);
			}
			//			
			haveSeats = listOfSeats.stream().anyMatch((obj) -> !obj.isOccupied());
		}

		aisleQueue=null;
		centerQueue=null;
		windowQueue=null;
		print();
	}

	private void setSeatPlacement(Seat seat, int col, long colSize, int laneNumber, long laneSize) {
		if( (laneNumber==0 && col==0) || ((laneNumber+1)==laneSize && (col+1)==colSize)) {
			seat.setSeatPlacement(SeatingPlacement.WINDOW);
		}else {			
			if(col==0) {
				seat.setSeatPlacement(SeatingPlacement.AISLE);
			}else if((col+1)==colSize) {
				seat.setSeatPlacement(SeatingPlacement.AISLE);
			}else {
				seat.setSeatPlacement(SeatingPlacement.CENTER);
			}
		}
	}

	public void print() {
		if(isCleaned) {
			System.out.println(NO_INPUT_RECIEVED);
		}else{
			printStatus();
			printSeatArrangement();
		}
	}
	
	public void printStatus() {
		if(isCleaned) {
			System.out.println(NO_INPUT_RECIEVED);
		}else{			
			Stream.generate(() -> "_").limit(100).forEach(System.out::print);
			System.out.println();
			System.out.println(SEATING_STATUS);
			if(noOfPassengers>listOfSeats.size()) {
				System.out.println(String.format(MAXIMUM_OCCUPANCY_MESSAGE, noOfPassengers, listOfSeats.size()));
			}else if(noOfPassengers<listOfSeats.size()){
				System.out.println(String.format(VACANT_OCCUPANCY_MESSAGE, noOfPassengers, (listOfSeats.size()-noOfPassengers)));
			}else if(noOfPassengers == listOfSeats.size()){
				System.out.println(NO_VACANT_SEATS);
			}
			
			Stream.generate(() -> "_").limit(100).forEach(System.out::print);
			System.out.println();
		}
	}

	private void printSeatArrangement() {
		System.out.println(SEATING_ARRANGEMENT);
		
		AsciiTable asciiTable = new AsciiTable();
		asciiTable.getContext().setGrid(A7_Grids.minusBarPlus());
		for(int row=0;row<maxRowSize;row++) {
			asciiTable.addRule();
			List<String> strList=new ArrayList<String>();
			for(int laneNumber=0;laneNumber<maxLaneSize;laneNumber++) {
				StringBuilder rowBuilder=new StringBuilder(); 

				long rowSizeOfPartLane=laneSizeList.get(laneNumber)[1];
				long colSizeofPartLane=laneSizeList.get(laneNumber)[0];

				for(int col=0;col<maxColSize;col++) {

					if(row>=rowSizeOfPartLane || col>=colSizeofPartLane) {
						continue;
					}
					Seat queryObject=new Seat();
					queryObject.setRow(row);
					queryObject.setColumn(col);
					queryObject.setLane(laneNumber);

					Seat responseSeat=seatFinderMap.get(queryObject);
					if(responseSeat!=null) {
						if(responseSeat.getPassengerNumber()!=0) {
							rowBuilder.append(responseSeat.getPassengerNumber()).append(" ");								
						}

					}
				}
				strList.add(rowBuilder.toString());
			}
			AT_Row asciiRow=asciiTable.addRow(strList);
			List<AT_Cell> cells=asciiRow.getCells();
			int cellSize=cells.size();
			for(int i=0;i<cellSize;i++) {
				if(cells.get(i).getContent().toString().length()==0)
					continue;
				else {

					if(i==0) {
						cells.get(i).getContext().setPaddingRight(REPORT_PADDING_WIDTH);
					} 
					else if(i!=cellSize-1) {
						cells.get(i).getContext().setPaddingLeftRight(REPORT_PADDING_WIDTH);
					}
					else { 
						cells.get(i).getContext().setPaddingLeft(REPORT_PADDING_WIDTH); 
					}
				}
			}
			cells.forEach(c -> c.getContext().setTextAlignment(TextAlignment.JUSTIFIED));
		}
		asciiTable.addRule();
		String output = asciiTable.render();
		System.out.println(output);
	}

	private void cleanUp() {
		isCleaned=true;
		noOfPassengers=-1;
		listOfSeats=new ArrayList<Seat>();
	}
}
