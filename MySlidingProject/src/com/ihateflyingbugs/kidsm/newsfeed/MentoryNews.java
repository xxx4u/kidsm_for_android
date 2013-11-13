package com.ihateflyingbugs.kidsm.newsfeed;

import java.util.ArrayList;

import android.content.Context;

import com.ihateflyingbugs.kidsm.newsfeed.News.NEWSTYPE;

public class MentoryNews extends News{
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
	private String timeline_type;
	
	public MentoryNews(String identifier, String timeline_srl, String timeline_member_srl, String timeline_like, String timeline_created) {
		super(identifier, timeline_srl, timeline_member_srl, timeline_like, timeline_created);
		type = NEWSTYPE.MENTORY;
	}

	public void setMentoryNews(String mentoring_srl,
			String mentoring_category_srl, String mentoring_type,
			String mentoring_subject, String mentoring_text,
			String mentoring_created, String mentoring_updated,
			String mentoring_mentor_srl, String mentoring_like,
			String mentoring_share) {
		// TODO Auto-generated method stub
		this.setMentoring_srl(mentoring_srl);
		this.setMentoring_category_srl(mentoring_category_srl);
		this.setMentoring_type(mentoring_type);
		this.setMentoring_subject(mentoring_subject);
		this.setMentoring_text(mentoring_text);
		this.setMentoring_created(mentoring_created);
		this.setMentoring_updated(mentoring_updated);
		this.setMentoring_mentor_srl(mentoring_mentor_srl);
		this.setMentoring_like(mentoring_like);
		this.setMentoring_share(mentoring_share);
		
		likeMemberList.clear();
		if( mentoring_like.isEmpty() == false ) {
			String[] likeMemberData = mentoring_like.split(",");
			for(int i = 0; i < likeMemberData.length; i++) {
				if(likeMemberData[i] != null && likeMemberData[i].isEmpty() == false)
					likeMemberList.add(likeMemberData[i]);
			}
		}
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

	public String getTimeline_type() {
		return timeline_type;
	}

	public void setTimeline_type(String timeline_type) {
		this.timeline_type = timeline_type;
	}
}
