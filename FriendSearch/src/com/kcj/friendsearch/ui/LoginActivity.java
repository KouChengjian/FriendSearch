package com.kcj.friendsearch.ui;

import org.json.JSONObject;

import com.kcj.friendsearch.util.CommonUtils;
import com.kcj.friendsearch.view.CircleImageView;
import com.xiapu.whereisfriend.R;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;
import cn.bmob.v3.listener.SaveListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseActivity implements OnClickListener{

	EditText et_username, et_password;
	Button btn_login , btn_qqlogin;
	TextView btn_register;
	BmobChatUser currentUser;
	CircleImageView circleImgView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initTopBarForOnlyTitle("登入");
		initView();
		circleImgView = (CircleImageView) findViewById(R.id.img_login_icon);
		Bitmap btm1 = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.logo_yousou);
		Bitmap mBitmap = Bitmap.createScaledBitmap(btm1, 180, 180, true);
		circleImgView.setImageBitmap(mBitmap);
	}
	
	private void initView() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_qqlogin = (Button) findViewById(R.id.button1);
		btn_register = (TextView) findViewById(R.id.btn_register);
		btn_login.setOnClickListener(this);
		btn_qqlogin.setOnClickListener(this);
		btn_register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btn_register) {
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(intent);
		}else if(v == btn_qqlogin){
			//qqlogin();
		} 
		else {
			boolean isNetConnected = CommonUtils.isNetworkAvailable(this);
			if(!isNetConnected){
				ShowToast(R.string.network_tips);
				return;
			}
			login();
		}
	}
	
	private void login(){
		String name = et_username.getText().toString();
		String password = et_password.getText().toString();

		if (TextUtils.isEmpty(name)) {
			ShowToast(R.string.toast_error_username_null);
			return;
		}

		if (TextUtils.isEmpty(password)) {
			ShowToast(R.string.toast_error_password_null);
			return;
		}

		// 对话框
		final ProgressDialog progress = new ProgressDialog(
				LoginActivity.this);
		progress.setMessage("正在登陆...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		// bmob初始化
		userManager.login(name, password, new SaveListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						progress.setMessage("正在获取好友列表...");
					}
				});
				//更新用户的地理位置以及好友的资料
				updateUserInfos();
				progress.dismiss();
				Intent intent = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(int errorcode, String arg0) {
				// TODO Auto-generated method stub
				progress.dismiss();
				ShowToast(arg0);
			}
		});	
	}
	
	public void qqlogin(){
		BmobUser.qqLogin(this, "222222", new OtherLoginListener() {
			@Override
			public void onSuccess(JSONObject userAuth) {
				// TODO Auto-generated method stub
				//toast("QQ登陆成功返回:"+userAuth.toString());
				Log.e("login", "QQ登陆成功返回:"+userAuth.toString());
				//下面则是返回的json字符
//				{
//					  "qq": {
//					    "openid": "B4F5ABAD717CCC93ABF3BF28D4BCB03A", 
//					    "access_token": "05636ED97BAB7F173CB237BA143AF7C9", 
//					    "expires_in": 7776000
//					  }
//				}
				//如果你想在登陆成功之后关联当前用户
				//Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				//intent.putExtra("json", userAuth.toString());
				//intent.putExtra("from", "qq");
				//startActivity(intent);
			}
			
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
			}
		});
	}
}
