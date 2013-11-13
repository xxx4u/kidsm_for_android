package com.ihateflyingbugs.kidsm.businfo;

public class BusStop {
	private String name;
	private boolean isPassed;
	private boolean currentLocation;
	private boolean isFinalElem;
	
	public BusStop(String name, boolean isPassed, boolean currentLocation,
			boolean isFianlElem) {
		super();
		this.name = name;
		this.isPassed = isPassed;
		this.currentLocation = currentLocation;
		this.isFinalElem = isFianlElem;
	}
	public boolean isFinalElem() {
		return isFinalElem;
	}
	public void isFinalElem(boolean isFinalElem) {
		this.isFinalElem = isFinalElem;
	}
	public boolean isCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(boolean currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isPassed() {
		return isPassed;
	}
	public void setPassed(boolean isPassed) {
		this.isPassed = isPassed;
	}
}
