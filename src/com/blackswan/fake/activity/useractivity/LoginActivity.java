package com.blackswan.fake.activity.useractivity;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.service.CustomerService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
	//用户手机号
	private EditText et_userphone;
	//用户密码
	private EditText et_userpwd;
	//注册按钮
	private Button registerButton;
	//忘记密码
	private Button forgetPwdButton;
	//理发店加盟
	private Button joinButton;
	//记住我
	CheckBox cb = null;
	ProgressDialog pd;
	Handler myHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			if (msg.what == 1)
			{
				Toast.makeText(LoginActivity.this, "用户名或密码错误！",Toast.LENGTH_LONG).show();
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		checkIfRemember();
		et_userphone = (EditText) findViewById(R.id.et_userphone);
		et_userpwd = (EditText) findViewById(R.id.et_userpwd);
		Button btnLogin = (Button)findViewById(R.id.btn_login);
		cb = (CheckBox) findViewById(R.id.rememberCheck);
	    btnLogin.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
				String uphone = et_userphone.getText().toString();	//获得输入的帐号
				String pwd = et_userpwd.getText().toString();	//获得输入的密码
				if (uphone.trim().equals(""))
				{
					Toast.makeText(LoginActivity.this, "请你输入手机号或账户名！", Toast.LENGTH_SHORT).show();
					return ;
				}
				if (pwd.trim().equals(""))
				{
					Toast.makeText(LoginActivity.this, "请您输入密码！", Toast.LENGTH_SHORT).show();
					return;
				}
				pd = ProgressDialog.show(LoginActivity.this, "请稍候", "正在连接服务器...", true, true);
				boolean result=CustomerService.check(uphone,pwd);  
			        if(result)  
			        {  
			            Toast.makeText(getApplicationContext(),R.string.success,Toast.LENGTH_SHORT).show();  
			        }else  
			        {  
			            Toast.makeText(getApplicationContext(),R.string.fail,Toast.LENGTH_SHORT).show();  
			        }     
				
				if (cb.isChecked())
				{
					rememberMe(et_userphone.getText().toString().trim(), et_userpwd.getText().toString().trim());
				}
				}
		});

		this.initCotrol();
		this.setListentener();
		this.changeBack();
	}
	private void rememberMe(String userphone,String userpwd)
	{
		//将用户的手机号和密码存入Prefernces
		SharedPreferences sp = getPreferences(MODE_PRIVATE);     //获得preferences
		SharedPreferences.Editor editor = sp.edit();			 //获得Editor
    	editor.putString("uphone", userphone);			    	 //将手机号存入Preferences
    	editor.putString("pwd", userpwd);						 //将密码存入Preferences
    	editor.commit();
	}
    private void checkIfRemember()                               //从preferences取得手机号和密码
	{
    	SharedPreferences sp = getPreferences(MODE_PRIVATE);     //获得preferences
    	String uphone = sp.getString("uphone", null);            //读取手机号
    	String pwd = sp.getString("pwd", null);                  //读取密码
    	if(uphone != null && pwd!= null){
    		EditText etUphone = (EditText)findViewById(R.id.et_userphone);
    		EditText etPwd = (EditText)findViewById(R.id.et_userpwd);
    		CheckBox cbRemember = (CheckBox)findViewById(R.id.rememberCheck);
    		etUphone.setText(uphone);
    		etPwd.setText(pwd);
    		cbRemember.setChecked(true);
    	}
		
	}
	/**
     * 初始化控件
     */
	public void initCotrol()
	{
		registerButton = (Button) findViewById(R.id.btn_register);
		forgetPwdButton = (Button) findViewById(R.id.forgetpwd);
		joinButton = (Button) findViewById(R.id.join);
	}
	
	/**
	 * 设置监听
	 */
	public void setListentener()
	{
		registerButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// 跳转到注册页面
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("action", "register");
				intent.putExtras(bundle);
				startActivity(intent);
				LoginActivity.this.finish();
				
			}
		});
		forgetPwdButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// 跳转到手机验证码修改密码页
				
			}
		});
		
		joinButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				//跳转到联系更么好科技公司页面
				
			}
		});
	}
	/**
	 * 在获得焦点时改变编辑框背景
	 * 
	 * @param btn
	 */
	public void changeBack() {
		et_userphone.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					et_userphone
							.setBackgroundResource(R.drawable.bg_edit_selected);
				} else {
					et_userphone
							.setBackgroundResource(R.drawable.bg_edit_unselected);
				}

			}
		});
		et_userpwd.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					et_userpwd
							.setBackgroundResource(R.drawable.bg_edit_selected);
				} else {
					et_userpwd
							.setBackgroundResource(R.drawable.bg_edit_unselected);
				}
			}
		});
	}
	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		
	}
}
