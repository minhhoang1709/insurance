package com.ninelives.insurance.provider.notification.message;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FcmNotifMessageDto implements Serializable {
	private static final long serialVersionUID = -191688682587677284L;
	private NotifMessage message;
	
	public NotifMessage getMessage() {
		return message;
	}

	public void setMessage(NotifMessage message) {
		this.message = message;
	}

	public static class NotifMessage implements Serializable{
		private static final long serialVersionUID = 2644158595371077085L;
		
		private String token;
		private Map<String, String> notification;
		private Map<String, Object> data;
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public Map<String, String> getNotification() {
			return notification;
		}
		public void setNotification(Map<String, String> notification) {
			this.notification = notification;
		}
		public Map<String, Object> getData() {
			return data;
		}
		public void setData(Map<String, Object> data) {
			this.data = data;
		}
		@Override
		public String toString() {
			return "NotifMessage [token=" + token + ", notification=" + notification + ", data=" + data + "]";
		}
	}

	@Override
	public String toString() {
		return "FcmNotifMessageDto [message=" + message + "]";
	}
	
}
