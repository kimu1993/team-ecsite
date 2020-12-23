package jp.co.internous.eagle.model.session;
import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
	@Scope(value="session",proxyMode=ScopedProxyMode.TARGET_CLASS)
	public class LoginSession implements Serializable{
	private static final long serialVersionUID = -2197078248505364204L;
		private int userId;
		private int temporaryUserId;
		private String userName;
		private String password;
		private boolean loginFlag;
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName =userName;
		}
		public String  getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public int  getTemporaryUserId() {
			return temporaryUserId;
		}
		public void setTemporaryUserId(int temporaryUserId) {
			this.temporaryUserId = temporaryUserId;
		}
		public void setLoginFlag(boolean loginFlag) {
			this.loginFlag = loginFlag;
		}
		public boolean getLoginFlag() {
			return loginFlag;
		}
	}

