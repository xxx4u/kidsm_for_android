package com.ihateflyingbugs.kidsm.notice;

public class NoticeLog {
	public enum NOTICE_TYPE {
		NORMAL,
		PHOTO
	}
	NOTICE_TYPE type;
	String title;
	String message;
	String ticker;
	String member_srl;
	
	public NoticeLog(NOTICE_TYPE type, String title, String message, String ticker, String member_srl) {
		this.type = type;
		this.title = title;
		this.message = message;
		this.ticker = ticker;
		this.member_srl = member_srl;
	}
}
