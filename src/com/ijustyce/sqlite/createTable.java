package com.ijustyce.sqlite;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

public class createTable {
	
	@Table(name = "sms_sms")
	public static class sms_sms extends Model { 
	    
		@Column(name = "phone")
	    public String phone;
	    
	    @Column(name = "sms")
	    public String sms;
	    
	    @Column(name = "date")
	    public String date;
	    
	    @Column(name = "isMy")
	    public boolean isMy;    
	    
		public static List<sms_sms> getRandom(String phone) {
			return new Select().from(sms_sms.class).where("phone = ?", phone)
					.orderBy("Id ASC").execute();
		}
		
		public static List<sms_sms> getRandom() {
			return new Select().from(sms_sms.class).orderBy("Id ASC").execute();
		}
	}
	
	@Table(name = "sms_intercept")
	public static class sms_intercept extends Model { 
	    
		@Column(name = "value")
	    public String value;
		@Column(name = "isPhone")
	    public boolean isPhone;
		public static List<sms_intercept> getRandom(String phone) {
			return new Select().from(sms_intercept.class).where("phone = ?", phone)
					.orderBy("Id ASC").execute();
		}
		public static List<sms_intercept> getRandom() {
			return new Select().from(sms_intercept.class).orderBy("Id ASC").execute();
		}
	}
	
	@Table(name = "sms_trash")
	public static class sms_trash extends Model { 
	    
		@Column(name = "phone")
	    public String phone;
	    
	    @Column(name = "sms")
	    public String sms;
	    
	    @Column(name = "date")
	    public String date;
	    
	    @Column(name = "isMy")
	    public boolean isMy;	    
	    public static List<sms_trash> getRandom(String phone) {
			return new Select().from(sms_trash.class).where("phone = ?", phone)
					.orderBy("Id ASC").execute();
		}
	    public static List<sms_trash> getRandom() {
			return new Select().from(sms_trash.class).orderBy("Id ASC").execute();
		}
	}
	
	@Table(name = "sms_conversation")
	public static class sms_conversation extends Model { 
	    
		@Column(name = "phone")
	    public String phone;
	    
	    @Column(name = "sms")
	    public String sms;
	    
	    @Column(name = "date")
	    public String date;
	    
	    public static List<sms_conversation> getRandom(String phone) {
			return new Select().from(sms_conversation.class).where("phone = ?", phone)
					.orderBy("Id ASC").execute();
		}
	    public static List<sms_conversation> getRandom() {
			return new Select().from(sms_conversation.class).orderBy("Id ASC").execute();
		}
	}
	
	@Table(name = "sms_timing")
	public static class sms_timing extends Model { 
	    
		@Column(name = "phone")
	    public String phone;
	    
	    @Column(name = "sms")
	    public String sms;
	    
	    @Column(name = "date")
	    public String date;
	    
	    @Column(name = "isMy")
	    public boolean isMy;
	    public static List<sms_timing> getRandom(String phone) {
			return new Select().from(sms_timing.class).where("phone = ?", phone)
					.orderBy("Id ASC").execute();
		}
	    public static List<sms_timing> getRandom() {
			return new Select().from(sms_timing.class).orderBy("Id ASC").execute();
		}
	}
}
