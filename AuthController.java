package jp.co.internous.eagle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import jp.co.internous.eagle.model.mapper.MstUserMapper;
import jp.co.internous.eagle.model.mapper.TblCartMapper;
import jp.co.internous.eagle.model.session.LoginSession;
import jp.co.internous.eagle.model.domain.MstUser;
import jp.co.internous.eagle.model.form.UserForm;

@RestController
@RequestMapping("/eagle/auth")
public class AuthController {
	
	
	@Autowired
	private MstUserMapper userMapper;
	
	@Autowired
	private TblCartMapper cartMapper;
	
	@Autowired
	private LoginSession loginSession;

	private Gson gson = new Gson();

	
	//ログインメソッド
	@RequestMapping("/login")
	public String login(@RequestBody UserForm f) {
		//page_header.htmlからajaxのJson形式で入力ユーザ名、パスワードを受け取りUserFormに格納
		//MstUserMapperのfindByUserNameAndPasswordメソッドを呼び出し、DBに存在するか照合し、MstUser型のインスタンスusersに代入
		MstUser user = userMapper.findByUserNameAndPassword(f.getUserName(),f.getPassword());
		
		
		//仮IDを取得
		int temporaryUserId = loginSession.getTemporaryUserId();
		
		//MstUserのusersがnullではなく、かつ仮IDが0ではなければ、loginSessionのUserIdに仮IDをセット
		//仮IDが存在する場合、カート情報を本IDと紐づける
		if(user != null && temporaryUserId != 0) {
			int count = cartMapper.findCountByUserId(temporaryUserId);
			if (count > 0) {
				cartMapper.updateUserId(user.getId(), temporaryUserId);
			}
		
		}
		
		//MstUserMapperから取得したユーザー usersが存在した場合にloginSessionに値を格納
		if(user != null) {

			//MstUser 要getter,setterメソッドの追加
			loginSession.setUserId(user.getId());
			loginSession.setUserName(user.getUserName());
			loginSession.setPassword(user.getPassword());
			loginSession.setLoginFlag(true);
			loginSession.setTemporaryUserId(0);
			
			
		}else { //ユーザーが存在しない場合はloginSessionをリセット
			loginSession.setUserId(0);
			loginSession.setUserName(null);
			loginSession.setPassword(null);
			loginSession.setLoginFlag(false);
		}
		 
		//ajaxにjson形式でMstUser型のUserを戻す
		return gson.toJson(user);
		}

	//ログアウトメソッド
	@RequestMapping("/logout")
	public String logout(){
		
		//ログインセッションの内容変更処理
		loginSession.setUserId(0);
		loginSession.setTemporaryUserId(0);
		loginSession.setUserName(null);
		loginSession.setPassword(null);
		loginSession.setLoginFlag(false);

		return "";
		
	}
	
	//パスワード再設定メソッド
	@RequestMapping("/resetPassword")
	public String resetPassword(@RequestBody UserForm f) {
		String message = "パスワードが再設定されました。";
		String newPassword = f.getNewPassword();
		String newPasswordConfirm = f.getNewPasswordConfirm();
		
		MstUser user = userMapper.findByUserNameAndPassword(f.getUserName(), f.getPassword());
		
		if (user == null) {
			return "現在のパスワードが正しくありません。";
		}
		
		if (user.getPassword().equals(newPassword)) {
			return "現在のパスワードと同一文字列が入力されました。";
		}
		
		if (!newPassword.equals(newPasswordConfirm)) {
			return "新パスワードと確認用パスワードが一致しません。";
		}
		// mst_userとloginSessionのパスワードを更新する
		userMapper.updatePassword(user.getUserName(), f.getNewPassword());
		loginSession.setPassword(f.getNewPassword());
		
		
		return message;
	}
}
	


