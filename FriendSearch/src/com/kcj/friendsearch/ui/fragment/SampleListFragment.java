package com.kcj.friendsearch.ui.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;














import com.kcj.friendsearch.CustomApplcation;
import com.kcj.friendsearch.bean.User;
import com.kcj.friendsearch.confi.BmobConstants;
import com.kcj.friendsearch.ui.FragmentBase;
import com.kcj.friendsearch.ui.LoginActivity;
import com.kcj.friendsearch.ui.MainActivity;
import com.kcj.friendsearch.util.ImageLoadOptions;
import com.kcj.friendsearch.util.PhotoUtil;
import com.kcj.friendsearch.util.UpateVersion;
import com.kcj.friendsearch.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.fb.FeedbackAgent;
import com.xiapu.whereisfriend.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class SampleListFragment extends FragmentBase {
	TextView tv_set_name;
	TextView txt_menu_setting;
	RoundedImageView imgView;
	BmobUserManager userManager;
	RelativeLayout login_layout, feedback_layout, linemap_layout, other_layout,
			setting_layout, exit_layout;
	LinearLayout layout_all;
	ImageView img_menu_setting;

	// ����
	FeedbackAgent agent;
	boolean isallow = false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_left_fragment, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		if (BmobUserManager.getInstance(getActivity()).getCurrentUser() != null) {
			initData();
		}
		//initUmeng();
	}

	public void initUmeng() {
		// ����
		agent = new FeedbackAgent(getActivity());
		agent.sync();
	}

	public void initView() {
		userManager = BmobUserManager.getInstance(getActivity());
		tv_set_name = (TextView) findViewById(R.id.nickNameTextView);
		txt_menu_setting = (TextView) findViewById(R.id.txt_menu_setting);
		imgView = (RoundedImageView) findViewById(R.id.headImageView);
		img_menu_setting = (ImageView) findViewById(R.id.img_menu_setting);
		login_layout = (RelativeLayout) findViewById(R.id.rl_login);
		feedback_layout = (RelativeLayout) findViewById(R.id.rl_feedback);
		linemap_layout = (RelativeLayout) findViewById(R.id.rl_mapline);
		other_layout = (RelativeLayout) findViewById(R.id.rl_other);
		setting_layout = (RelativeLayout) findViewById(R.id.rl_setting);
		exit_layout = (RelativeLayout) findViewById(R.id.rl_exit);
		layout_all = (LinearLayout) findViewById(R.id.ll_all);
		setListen();
	}

	public void setListen() {
		imgView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainActivity.getInstance().showMenu();
				showAvatarPop();
			}
		});

		login_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (BmobUserManager.getInstance(getActivity()).getCurrentUser() != null) {
					ShowToast("�˺Ŵ��ڵ���״̬");
				} else {
					startAnimActivity(LoginActivity.class);
				}
			}
		});

		feedback_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				agent.startFeedbackActivity();
			}
		});

		linemap_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			}
		});

		other_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainActivity.getInstance().showMenu();
				UpateVersion uv = new UpateVersion(getActivity());
			    uv.updateVersion();
			}
		});

		setting_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainActivity.getInstance().showMenu();
				updateInfo(isallow);
			}
		});

		exit_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CustomApplcation.getInstance().logout();
				startAnimActivity(LoginActivity.class);
				getActivity().finish();
			}
		});
	}
	
	/** �޸�����
	  * updateInfo
	  * @Title: updateInfo
	  * @return void
	  * @throws
	  */
	private void updateInfo(final boolean isAllow) {
		final User user = userManager.getCurrentUser(User.class);
		user.setAllowFriendKnowLocation(isAllow);
		user.update(getActivity(), new UpdateListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(!isAllow){
					isallow = true;
					txt_menu_setting.setText(R.string.menu_forbid);
					img_menu_setting.setImageResource(R.drawable.menu_fragment_forbid_icon);
				}else{
					isallow = false;
					txt_menu_setting.setText(R.string.menu_allow);
					img_menu_setting.setImageResource(R.drawable.menu_fragment_allow_icon);
				}
				ShowToast("�޸ĳɹ�");
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("onFailure:" + arg1);
			}
		});
	}

	private void initData() {
		// .getUsername()
		tv_set_name.setText(BmobUserManager.getInstance(getActivity())
				.getCurrentUser().getNick());
		initOtherData();
	}

	User user;

	private void initOtherData() {
		String str = BmobUserManager.getInstance(getActivity())
				.getCurrentUser().getUsername();
		userManager.queryUser(str, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				// ShowLog("onError onError:"+arg1);
			}

			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				if (arg0 != null && arg0.size() > 0) {
					user = arg0.get(0);
					updateUser(user);
				} else {
					ShowToast("onSuccess ���޴���");
				}
			}
		});
	}

	private void updateUser(User user) {
		String avatar = user.getAvatar();
		if (avatar != null && !avatar.equals("")) {
			ImageLoader.getInstance().displayImage(avatar, imgView,
					ImageLoadOptions.getOptions());
		} else {
			imgView.setImageResource(R.drawable.head);
		}
	}

	RelativeLayout layout_choose;
	RelativeLayout layout_photo;
	PopupWindow avatorPop;

	public String filePath = "";

	@SuppressWarnings("deprecation")
	private void showAvatarPop() {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.pop_showavator, null);
		layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
		layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		layout_photo.setOnClickListener(new OnClickListener() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View arg0) {
				layout_choose.setBackgroundColor(getResources().getColor(
						R.color.base_color_text_white));
				layout_photo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				File dir = new File(BmobConstants.MyAvatarDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// ԭͼ
				File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date()));
				filePath = file.getAbsolutePath();// ��ȡ��Ƭ�ı���·��
				Uri imageUri = Uri.fromFile(file);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA);
			}
		});
		layout_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				layout_photo.setBackgroundColor(getResources().getColor(
						R.color.base_color_text_white));
				layout_choose.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION);
			}
		});

		avatorPop = new PopupWindow(view, mScreenWidth, 600);
		avatorPop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					avatorPop.dismiss();
					return true;
				}
				return false;
			}
		});
		avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		avatorPop.setTouchable(true);
		avatorPop.setFocusable(true);
		avatorPop.setOutsideTouchable(true);
		avatorPop.setBackgroundDrawable(new BitmapDrawable());
		// ����Ч�� �ӵײ�����
		avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);
		avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
	}

	Bitmap newBitmap;
	boolean isFromCamera = false;// ���������ת
	int degree = 0;

	@SuppressWarnings("static-access")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_CAMERA:// �����޸�ͷ��
			if (resultCode == getActivity().RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					ShowToast("SD������");
					return;
				}
				isFromCamera = true;
				File file = new File(filePath);
				degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
				Log.i("life", "���պ�ĽǶȣ�" + degree);
				startImageAction(Uri.fromFile(file), 200, 200,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
			}
			break;
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_LOCATION:// �����޸�ͷ��
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			Uri uri = null;
			if (data == null) {
				return;
			}
			if (resultCode == getActivity().RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					ShowToast("SD������");
					return;
				}
				isFromCamera = false;
				uri = data.getData();
				startImageAction(uri, 200, 200,
						BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP, true);
			} else {
				ShowToast("��Ƭ��ȡʧ��");
			}

			break;
		case BmobConstants.REQUESTCODE_UPLOADAVATAR_CROP:// �ü�ͷ�񷵻�
			// TODO sent to crop
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			if (data == null) {
				// Toast.makeText(this, "ȡ��ѡ��", Toast.LENGTH_SHORT).show();
				return;
			} else {
				saveCropAvator(data);
			}
			// ��ʼ���ļ�·��
			filePath = "";
			// �ϴ�ͷ��
			uploadAvatar();
			break;
		default:
			break;

		}
	}

	/**
	 * @Title: startImageAction
	 * @return void
	 * @throws
	 */
	private void startImageAction(Uri uri, int outputX, int outputY,
			int requestCode, boolean isCrop) {
		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	String path;

	/**
	 * ����ü���ͷ��
	 * 
	 * @param data
	 */
	private void saveCropAvator(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			Log.i("life", "avatar - bitmap = " + bitmap);
			if (bitmap != null) {
				bitmap = PhotoUtil.toRoundCorner(bitmap, 10);
				if (isFromCamera && degree != 0) {
					bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
				}
				imgView.setImageBitmap(bitmap);
				// ����ͼƬ
				String filename = new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date());
				path = BmobConstants.MyAvatarDir + filename;
				PhotoUtil.saveBitmap(BmobConstants.MyAvatarDir, filename,
						bitmap, true);
				// �ϴ�ͷ��
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
	}

	private void uploadAvatar() {
		final BmobFile bmobFile = new BmobFile(new File(path));
		bmobFile.upload(getActivity(), new UploadFileListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				String url = bmobFile.getFileUrl();
				// ����BmobUser����
				updateUserAvatar(url);
			}

			@Override
			public void onProgress(Integer arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				ShowToast("ͷ���ϴ�ʧ�ܣ�" + msg);
			}
		});
	}

	private void updateUserAvatar(final String url) {
		User user = (User) userManager.getCurrentUser(User.class);
		user.setAvatar(url);
		user.update(getActivity(), new UpdateListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ShowToast("ͷ����³ɹ���");
				// ����ͷ��
				// refreshAvatar(url);
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				ShowToast("ͷ�����ʧ�ܣ�" + msg);
			}
		});
	}

	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}
			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}
			json.put("device_id", device_id);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
