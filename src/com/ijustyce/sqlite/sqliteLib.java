package com.ijustyce.sqlite;

import java.util.List;

import com.activeandroid.query.Delete;
import com.ijustyce.sms.unit.SmsManager;
import com.ijustyce.sqlite.createTable.sms_conversation;
import com.ijustyce.sqlite.createTable.sms_intercept;
import com.ijustyce.sqlite.createTable.sms_sms;
import com.ijustyce.sqlite.createTable.sms_timing;

public class sqliteLib {
	
	private static ViewChanged view;
	private static Intercept intercept;
	private static Timing timing;
	
	/**
	 * insert to conversation , you should not call this , it will 
	 * automatically called 
	 * @param phone phone number that send you sms
	 * @param content sms content
	 * @param date date sms send to you
	 * @return
	 */
	private static long conversationInsert(String phone , String content , 
			String date){
		
		sms_conversation conversation = new sms_conversation();
		conversation.phone = phone;
		conversation.sms = content;
		conversation.date = date;
		conversation.save();
		
		return conversation.getId();
	}
	
	/**
	 * delete a conversation if need
	 * @param phone phone number
	 */
	public static void conversationDelete(String phone) {
		
		new Delete().from(sms_conversation.class).where("phone = ?", phone).execute();
		new Delete().from(sms_sms.class).where("phone = ?", phone).execute();
	}
	
	/**
	 * get conversation list , you should call set listener first 
	 * and implements ViewChanged
	 */
	public void conversationList() {
		
		List<sms_conversation> conversation = sms_conversation.getRandom();
		for (sms_conversation tmp : conversation) {
			
			view.setView(tmp.getId(), tmp.phone, tmp.sms);
		}
	}

	/**
	 * insert a sms , you should not call this , it will called automatically ,
	 * in your a app , you call SmsManager.sendSms is enough , when you received
	 * a sms , you call SmsManager.smsReceived is enough , of course you should
	 * judge whether should intercept or not , after that just show popup window
	 * if needed
	 * @param phone
	 * @param content
	 * @param date
	 * @param isMy
	 * @return
	 */
	public static long smsInsert(String phone , String content , String date, 
			boolean isMy) {
		
		sms_sms sms = new sms_sms();
		sms.phone = phone;
		sms.sms = content;
		sms.date = date;
		sms.isMy = isMy;
		sms.save();
		
		List<sms_conversation> conversation = sms_conversation.getRandom();
		if (conversation.isEmpty()) {
			conversationInsert(phone, content, date);
		}
		
		return sms.getId();
	}
	
	/**
	 * delete a sms
	 * @param id id of this this sms
	 * @param phone phone number of this sms , for judge whether 
	 * should delete conversation or not
	 */
	public static void smsDelete(long id , String phone) {
		
		new Delete().from(sms_sms.class).where("id = ?", id).execute();
		List<sms_sms> sms = sms_sms.getRandom(phone);
		if (sms.isEmpty()) {
			conversationDelete(phone);
		}		
	}
	
	/**
	 * add a intercept value is phone or key words , and isPhone
	 * judge whether is a phone number , be care , this not judge
	 * whether value is phone number
	 * @param value
	 * @param isPhone
	 * @return
	 */
	public static long interceptAdd(String value , boolean isPhone){
		
		sms_intercept intercept = new sms_intercept();
		intercept.value = value;
		intercept.isPhone = isPhone;
		intercept.save();
		
		return intercept.getId();
	}
	
	/**
	 * delete a phone or keywords from intercept list
	 * @param id
	 */
	public static void interceptDelete(long id) {

		new Delete().from(sms_intercept.class).where("id = ?", id).execute();
	}
	
	/**
	 * intercept list , you should call set listener first
	 */
	public static void intercept(){
		
		List<sms_intercept> list = sms_intercept.getRandom();
		for (sms_intercept tmp : list) {
			
			if (tmp.isPhone) {
				intercept.phone(tmp.value);
			}else {
				intercept.text(tmp.value);
			}
		}
	}
	
	/**
	 * add a timing sms
	 * @param phone
	 * @param content
	 * @param date
	 * @return
	 */
	public static long timingAdd(String phone , String content , String date){
		
		sms_timing timing = new sms_timing();
		timing.phone = phone;
		timing.sms = content;
		timing.date = date;
		timing.save();
		
		SmsManager.timing();
		return timing.getId();
	}
	
	/**
	 * delete a timing sms , should not call this after send a timing sms
	 * it will automatically called
	 * @param id
	 */
	public static void timingDelete(long id) {

		new Delete().from(sms_timing.class).where("id = ?", id).execute();
		SmsManager.timing();
	}
	
	/**
	 * get timing list
	 */
	public static void timingList(){
		
		List<sms_timing> list = sms_timing.getRandom();
		for (sms_timing tmp : list) {
			
			timing.timingList(tmp.phone , tmp.sms , tmp.date);
		}
	}
	
	/**
	 * init timing , you should not call this , it will automatically called
	 * after you add you delete a timing sms
	 */
	public static void timing(){
		
		List<sms_timing> list = sms_timing.getRandom();
		for (sms_timing tmp : list) {
			
			timing.timing(tmp.phone , tmp.sms , tmp.date , tmp.getId());
		}
	}
	
	public interface ViewChanged{
		
		public void setView(long id , String phone , String sms ,
				String date , boolean isMy);
		public void setView(long id , String phone , String sms);
	}
	
	public interface Intercept{
		
		public void phone(String phone);
		public void text(String text);
	}
	
	public interface Timing{
		
		public void timingList(String phone , String sms , String date);
		public void timing(String phone , String sms , String date , long id);
	}
	
	/**
	 * set listener for timing module
	 * @param timing
	 */
	public static void setListenner(Timing timing){
		
		sqliteLib.timing = timing;
	}
	
	/**
	 * set listener for sms module , you should do this in conversation list
	 * as well as sms list , and refresh view in callback function
	 * @param viewChanged
	 */
	public static void setListenner(ViewChanged viewChanged) {

		sqliteLib.view = viewChanged;
	}

	/**
	 * set listener for intercept module , do this in sms receive broadcast
	 * @param intercept
	 */
	public static void setListenner(Intercept intercept) {

		sqliteLib.intercept = intercept;
	}
}
