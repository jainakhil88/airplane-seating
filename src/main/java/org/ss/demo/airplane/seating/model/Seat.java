package org.ss.demo.airplane.seating.model;

public class Seat {

	long row;
	long column;
	long lane;

	long passengerNumber;
	
	SeatingPlacement seatPlacement;

	boolean isOccupied;

	public Seat() {

	}

	public long getRow() {
		return row;
	}

	public void setRow(long row) {
		this.row = row;
	}

	public long getColumn() {
		return column;
	}

	public void setColumn(long column) {
		this.column = column;
	}

	public long getLane() {
		return lane;
	}

	public void setLane(long lane) {
		this.lane = lane;
	}

	public long getPassengerNumber() {
		return passengerNumber;
	}

	public void setPassengerNumber(long passengerNumber) {
		this.passengerNumber = passengerNumber;
	}

	public SeatingPlacement getSeatPlacement() {
		return seatPlacement;
	}

	public void setSeatPlacement(SeatingPlacement seatPlacement) {
		this.seatPlacement = seatPlacement;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (column ^ (column >>> 32));
		result = prime * result + (int) (lane ^ (lane >>> 32));
		result = prime * result + (int) (row ^ (row >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seat other = (Seat) obj;
		if (column != other.column)
			return false;
		if (lane != other.lane)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Seat [row=" + row + ", column=" + column + ", lane=" + lane + ", passengerNumber=" + passengerNumber
				+ ", seatPlacement=" + seatPlacement + ", isOccupied=" + isOccupied + "]";
	}

}
