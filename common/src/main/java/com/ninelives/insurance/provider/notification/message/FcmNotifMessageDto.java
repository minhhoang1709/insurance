package com.ninelives.insurance.provider.notification.message;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FcmNotifMessageDto implements Serializable {
	private static final long serialVersionUID = -7484746784897729126L;
	private Message message;
	
	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Message implements Serializable{
		private static final long serialVersionUID = 2644158595371077085L;
		
		private String token;
		private Notification notification;
		private Android android;
		private Data data;
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}		
		public Notification getNotification() {
			return notification;
		}
		public void setNotification(Notification notification) {
			this.notification = notification;
		}		
		public Android getAndroid() {
			return android;
		}
		public void setAndroid(Android android) {
			this.android = android;
		}
		public Data getData() {
			return data;
		}
		public void setData(Data data) {
			this.data = data;
		}
		@Override
		public String toString() {
			return "Message [token=" + token + ", notification=" + notification + ", android=" + android + ", data="
					+ data + "]";
		}
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Notification implements Serializable{
		private static final long serialVersionUID = 3654449977709641143L;
		private String title;
		private String body;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		@Override
		public String toString() {
			return "Notification [title=" + title + ", body=" + body + "]";
		}
		
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Android implements Serializable{
		private static final long serialVersionUID = 4467459811291062255L;
		private String ttl;
		public String getTtl() {
			return ttl;
		}
		public void setTtl(String ttl) {
			this.ttl = ttl;
		}
		@Override
		public String toString() {
			return "Android [ttl=" + ttl + "]";
		}
		
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Data implements Serializable{
		private static final long serialVersionUID = -1871098927029223331L;
		private String action;
		private String actionData;
		public String getAction() {
			return action;
		}
		public void setAction(String action) {
			this.action = action;
		}
		public String getActionData() {
			return actionData;
		}
		public void setActionData(String actionData) {
			this.actionData = actionData;
		}
		@Override
		public String toString() {
			return "Data [action=" + action + ", actionData=" + actionData + "]";
		}
		
	}

	@Override
	public String toString() {
		return "FcmNotifMessageDto [message=" + message + "]";
	}
	
}
