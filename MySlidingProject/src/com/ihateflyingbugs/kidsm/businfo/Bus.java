package com.ihateflyingbugs.kidsm.businfo;

import java.util.ArrayList;

public class Bus {	
	
	private String suttle_srl;
	private String shuttle_org_srl;
	private String shuttle_name;
	private String shuttle_route;
	private String shuttle_location;
	
	public Bus(String suttle_srl, String shuttle_org_srl, String shuttle_name,
			String shuttle_route, String shuttle_location) {
		super();
		this.suttle_srl = suttle_srl;
		this.shuttle_org_srl = shuttle_org_srl;
		this.shuttle_name = shuttle_name;
		this.shuttle_route = shuttle_route;
		this.shuttle_location = shuttle_location;
	}
	
	public String getSuttle_srl() {
		return suttle_srl;
	}
	public void setSuttle_srl(String suttle_srl) {
		this.suttle_srl = suttle_srl;
	}
	public String getShuttle_org_srl() {
		return shuttle_org_srl;
	}
	public void setShuttle_org_srl(String shuttle_org_srl) {
		this.shuttle_org_srl = shuttle_org_srl;
	}
	public String getShuttle_name() {
		return shuttle_name;
	}
	public void setShuttle_name(String shuttle_name) {
		this.shuttle_name = shuttle_name;
	}
	public String getShuttle_route() {
		return shuttle_route;
	}
	public void setShuttle_route(String shuttle_route) {
		this.shuttle_route = shuttle_route;
	}
	public String getShuttle_location() {
		return shuttle_location;
	}
	public void setShuttle_location(String shuttle_location) {
		this.shuttle_location = shuttle_location;
	}
}
