package com.ihateflyingbugs.kidsm.schedule;

public class CheckInfo {
	public String check_srl;
	public String check_cal_srl;
	public String check_member_srl;
	public CheckInfo(String check_srl, String check_cal_srl, String check_member_srl) {
		this.check_srl = check_srl;
		this.check_cal_srl = check_cal_srl;
		this.check_member_srl = check_member_srl;
	}
	public CheckInfo(CheckInfo info) {
		this.check_srl = info.check_srl;
		this.check_cal_srl = info.check_cal_srl;
		this.check_member_srl = info.check_member_srl;
	}
}
