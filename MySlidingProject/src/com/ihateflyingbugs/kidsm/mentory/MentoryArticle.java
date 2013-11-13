package com.ihateflyingbugs.kidsm.mentory;

import java.util.ArrayList;

import com.ihateflyingbugs.kidsm.newsfeed.Reply;

import android.view.View;

public class MentoryArticle {
	private String mentoring_srl;
	private String mentoring_category_srl;
	private String mentoring_type;
	private String mentoring_subject;
	private String mentoring_text;
	private String mentoring_created;
	private String mentoring_updated;
	private String mentoring_mentor_srl;
	private String mentoring_like;
	private String mentoring_share;
	private ArrayList<String> mentoring_likeList;
	private ArrayList<Reply> mentoring_commentList;
	private String member_scrap_srl;
	private int scrapCount;
	private View layout;
	
	public MentoryArticle(String mentoring_srl, String mentoring_category_srl,
			String mentoring_type, 
			String mentoring_subject, String mentoring_text,
			String mentoring_created, String mentoring_updated,
			String mentoring_mentor_srl, String mentoring_like,
			String mentoring_share) {
		super();
		this.mentoring_srl = mentoring_srl;
		this.mentoring_category_srl = mentoring_category_srl;
		this.mentoring_type = mentoring_type;
		this.mentoring_subject = mentoring_subject;
		this.mentoring_text = mentoring_text;
		this.mentoring_created = mentoring_created;
		this.mentoring_updated = mentoring_updated;
		this.mentoring_mentor_srl = mentoring_mentor_srl;
		this.mentoring_like = mentoring_like;
		this.mentoring_share = mentoring_share;
		
		setMentoring_likeList(new ArrayList<String>());
		if( mentoring_like.isEmpty() == false ) {
			String[] likeMemberData = mentoring_like.split(",");
			for(int i = 0; i < likeMemberData.length; i++) {
				if(likeMemberData[i] != null && likeMemberData[i].isEmpty() == false)
					getMentoring_likeList().add(likeMemberData[i]);
			}
		}
		setMentoring_commentList(new ArrayList<Reply>());
		setMember_scrap_srl("");
		setScrapCount(0);
	}
	
	public String getMentoring_srl() {
		return mentoring_srl;
	}
	public void setMentoring_srl(String mentoring_srl) {
		this.mentoring_srl = mentoring_srl;
	}
	public String getMentoring_category_srl() {
		return mentoring_category_srl;
	}
	public void setMentoring_category_srl(String mentoring_category_srl) {
		this.mentoring_category_srl = mentoring_category_srl;
	}
	public String getMentoring_type() {
		return mentoring_type;
	}
	public void setMentoring_type(String mentoring_type) {
		this.mentoring_type = mentoring_type;
	}
	public String getMentoring_subject() {
		return mentoring_subject;
	}
	public void setMentoring_subject(String mentoring_subject) {
		this.mentoring_subject = mentoring_subject;
	}
	public String getMentoring_text() {
		return mentoring_text;
	}
	public void setMentoring_text(String mentoring_text) {
		this.mentoring_text = mentoring_text;
	}
	public String getMentoring_created() {
		return mentoring_created;
	}
	public void setMentoring_created(String mentoring_created) {
		this.mentoring_created = mentoring_created;
	}
	public String getMentoring_updated() {
		return mentoring_updated;
	}
	public void setMentoring_updated(String mentoring_updated) {
		this.mentoring_updated = mentoring_updated;
	}
	public String getMentoring_mentor_srl() {
		return mentoring_mentor_srl;
	}
	public void setMentoring_mentor_srl(String mentoring_mentor_srl) {
		this.mentoring_mentor_srl = mentoring_mentor_srl;
	}
	public String getMentoring_like() {
		return mentoring_like;
	}
	public void setMentoring_like(String mentoring_like) {
		this.mentoring_like = mentoring_like;
	}
	public String getMentoring_share() {
		return mentoring_share;
	}
	public void setMentoring_share(String mentoring_share) {
		this.mentoring_share = mentoring_share;
	}

	public View getLayout() {
		return layout;
	}

	public void setLayout(View layout) {
		this.layout = layout;
	}

	public ArrayList<String> getMentoring_likeList() {
		return mentoring_likeList;
	}

	public void setMentoring_likeList(ArrayList<String> mentoring_likeList) {
		this.mentoring_likeList = mentoring_likeList;
	}

	public String getMember_scrap_srl() {
		return member_scrap_srl;
	}

	public void setMember_scrap_srl(String member_scrap_srl) {
		this.member_scrap_srl = member_scrap_srl;
	}

	public int getScrapCount() {
		return scrapCount;
	}

	public void setScrapCount(int scrapCount) {
		this.scrapCount = scrapCount;
	}

	public ArrayList<Reply> getMentoring_commentList() {
		return mentoring_commentList;
	}

	public void setMentoring_commentList(ArrayList<Reply> mentoring_commentList) {
		this.mentoring_commentList = mentoring_commentList;
	}
}
