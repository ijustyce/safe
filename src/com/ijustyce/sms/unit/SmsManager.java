package com.ijustyce.sms.unit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.ijustyce.safe.txApplication;
import com.ijustyce.sqlite.sqliteLib;
import com.ijustyce.sqlite.sqliteLib.Intercept;
import com.ijustyce.sqlite.sqliteLib.Timing;

public class SmsManager {
	
	private static boolean isIntercept = false;
	private static String text;
	private static Date ealier;
	private static smsReceiver sms;
	
	/**
	 *  send s sms and then insert 
	 * @param phone phoneNumber to send sms
	 * @param content sms content
	 */
	public static void sendSms(String phone , String content) {
		
		sqliteLib.smsInsert(phone, content, txApplication.getDate(), true);
	}
	
	/**
	 * send a timing sms , first should send a broadcast
	 * @param phone phone number to send sms 
	 * @param content sms content
	 * @param date the time to send the message
	 */
	private static void sendSms(String phone , String content, Date date , long id){
		
		// TODO  send a timing sms , you should send a broadcast , when time come 
		//you should send a timing sms then call sqliteLib.timingDelete(id) that all
	}
	
	/**
	 * set listener for callback if message received
	 * @param sms sms listener , should implements smsReceiver
	 */
	public static void setListener(smsReceiver sms){
		
		SmsManager.sms = sms;
	}
	
	/**
	 * received a message , insert and call callback function
	 * @param phone phone , you received sms
	 * @param content sms content
	 * @param date date sms send to you
	 */
	public static void receiveSms(String phone , String content ,String date) {
		
		sqliteLib.smsInsert(phone, content, date, false);
		sms.onReceive(phone, content, date);
	}
	
	/**
	 * judge whether a sms should intercept you should call two times
	 * @param value phone or sms , you should call two of them 
	 * @return true if should intercept or return false
	 */
	public static boolean isIntercept(String value) {
		
		text = value;
		sqliteLib.setListenner(intercept);
		sqliteLib.intercept();
		return isIntercept;
	}
	
	static Intercept intercept = new Intercept() {
		
		/**
		 * judge whether should intercept the phone
		 */
		@Override
		public void phone(String phone) {
			
			if (phone.equals(text)) {
				isIntercept = true;
			}
		}

		/**
		 * judge whether should intercept sms content
		 */
		@Override
		public void text(String value) {
			
			if (text.contains(value)) {
				isIntercept = true;
			}	
		}
	};
	
	
	/**
	 * you should not call this function , it will called automatically 
	 * after you called sqliteLib.timingAdd or sqliteLib.timingDelete
	 * for find the latest timing sms and then send broadcast
	 */
	public static void timing() {
		
		sqliteLib.setListenner(timing);
		sqliteLib.timing();
	}
	
	static Timing timing = new Timing() {
		
		@Override
		public void timingList(String phone, String sms, String date) {
			
		}
		
		@Override
		public void timing(String phone, String sms, String date , long id) {
			
			String now = txApplication.getDate();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" 
					, Locale.CHINA);
			try {
				Date nowDate = format.parse(now);
				Date tmp = format.parse(date);
				ealier = format.parse("2014-04-16 18:22:30");
				if (tmp.before(nowDate)) {
					// TODO before than today , delete or send ?
				}
				if (tmp.before(ealier)) {
					ealier = tmp;
					sendSms(phone , sms , ealier , id);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	};
	
	public interface smsReceiver{
		
		public void onReceive(String phone , String content ,String date);
	}
}
