package com.ihateflyingbugs.kidsm.newsfeed;

public class RecommendedMentoryNews extends MentoryNews {
	private String timeline_member_name;
	public RecommendedMentoryNews(String identifier, String timeline_srl, String timeline_member_srl, String timeline_like, String timeline_created) {
		super(identifier, timeline_srl, timeline_member_srl, timeline_like, timeline_created);
		type = NEWSTYPE.RECOMMENDED_MENTORY;
	}
	public String getTimeline_member_name() {
		return timeline_member_name;
	}
	public void setTimeline_member_name(String timeline_member_name) {
		this.timeline_member_name = timeline_member_name;
	}
}
