package com.ihateflyingbugs.kidsm.mentory;

public class MentoryCategory {
	private String category_srl;
	private String category_parent_srl;
	private String category_name;
	private String category_perm;
	
	public MentoryCategory(String category_srl, String category_parent_srl,
			String category_name, String category_perm) {
		super();
		this.category_srl = category_srl;
		this.category_parent_srl = category_parent_srl;
		this.category_name = category_name;
		this.category_perm = category_perm;
	}
	
	public String getCategory_srl() {
		return category_srl;
	}
	public void setCategory_srl(String category_srl) {
		this.category_srl = category_srl;
	}
	public String getCategory_parent_srl() {
		return category_parent_srl;
	}
	public void setCategory_parent_srl(String category_parent_srl) {
		this.category_parent_srl = category_parent_srl;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getCategory_perm() {
		return category_perm;
	}
	public void setCategory_perm(String category_perm) {
		this.category_perm = category_perm;
	}
}
