package com.ihateflyingbugs.kidsm.notice;

public class NoticeLog {
	public enum NOTICE_TYPE {
		NORMAL,
		PHOTO
	}
	private NOTICE_TYPE type;
	private String title;
	private String message;
	private String ticker;
	private String member_srl;
	private String member_picture;
	
	public NoticeLog(NOTICE_TYPE type, String title, String message, String ticker, String member_srl) {
		this.setType(type);
		this.setTitle(title);
		this.setMessage(message);
		this.setTicker(ticker);
		this.setMember_srl(member_srl);
	}

	public String getMember_srl() {
		return member_srl;
	}

	public void setMember_srl(String member_srl) {
		this.member_srl = member_srl;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public NOTICE_TYPE getType() {
		return type;
	}

	public void setType(NOTICE_TYPE type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMember_picture() {
		return member_picture;
	}

	public void setMember_picture(String member_picture) {
		this.member_picture = member_picture;
	}
}
