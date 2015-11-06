package com.kcj.friendsearch.ui;

import java.util.List;

import com.kcj.friendsearch.CustomApplcation;
import com.kcj.friendsearch.bean.User;
import com.kcj.friendsearch.util.CollectionUtils;
import com.kcj.friendsearch.view.HeaderLayout;
import com.kcj.friendsearch.view.HeaderLayout.HeaderStyle;
import com.kcj.friendsearch.view.HeaderLayout.onLeftImageButtonClickListener;
import com.kcj.friendsearch.view.HeaderLayout.onRightImageButtonClickListener;
import com.umeng.analytics.MobclickAgent;
import com.xiapu.whereisfriend.R;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

/**
 * ����
 * 
 * @ClassName: BaseActivity
 * @Description: TODO
 * @author smile
 * @date 2014-9-12 10:30
 */
public class BaseActivity extends FragmentActivity {

	BmobUserManager userManager;
	BmobChatManager manager;

	CustomApplcation mApplication;

	Toast mToast;
	protected int mScreenWidth;
	protected int mScreenHeight;

	protected HeaderLayout mHeaderLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);  //��׽δ��׽�쳣��ͨ��������������������ռ���������쳣�˳��Ĵ�����Ϣ���粻�ÿ�ע��
		// ��ȡ�ֱ���
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		// ��ʼ��
		userManager = BmobUserManager.getInstance(this);
		manager = BmobChatManager.getInstance(this);
		mApplication = CustomApplcation.getInstance();
	}

	/**
	 * ��ʾ����String Toast
	 */
	public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (mToast == null) {
						mToast = Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_LONG);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});
		}
	}

	/**
	 * ��ʾ����int Toast
	 */
	public void ShowToast(final int resId) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mToast == null) {
					mToast = Toast.makeText(
							BaseActivity.this.getApplicationContext(), resId,
							Toast.LENGTH_LONG);
				} else {
					mToast.setText(resId);
				}
				mToast.show();
			}
		});
	}

	/**
	 * ֻ��title initTopBarLayoutByTitle
	 * 
	 * @Title: initTopBarLayoutByTitle
	 * @throws kcj
	 *             ����
	 */
	public void initTopBarForOnlyTitle(String titleName) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle(titleName);
	}

	/**
	 * ��ʼ��������-�����Ұ�ť
	 * 
	 * @return void
	 * @throws kcj
	 *             ��������Ұ�ť
	 */
	public void initTopBarForBoth(String titleName, int rightDrawableId,
			String text, onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightButton(titleName, rightDrawableId, text,
				listener);
	}

	/**
	 * ��ʼ��������-�����Ұ�ť
	 * 
	 * @return void
	 * @throws kcj
	 *             ��������Ұ�ť(û�б�������)
	 */
	public void initTopBarForBoth(String titleName, int rightDrawableId,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
	}

	/**
	 * ֻ����߰�ť��Title initTopBarLayout
	 * 
	 * @throws kcj
	 *             �������ť
	 */
	public void initTopBarForLeft(String titleName) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_back_bg_selector,
				new OnLeftButtonClickListener());
	}

	// ��߰�ť�ĵ���¼�
	public class OnLeftButtonClickListener implements
			onLeftImageButtonClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
            finish();
		}
	}

	/**
	 * ��ʾ���ߵĶԻ��� showOfflineDialog
	 * 
	 * @return void
	 * @throws public
	 *             void showOfflineDialog(final Context context) { DialogTips
	 *             dialog = new DialogTips(this,"�����˺����������豸�ϵ�¼!", "���µ�¼"); //
	 *             ���óɹ��¼� dialog.SetOnSuccessListener(new
	 *             DialogInterface.OnClickListener() { public void
	 *             onClick(DialogInterface dialogInterface, int userId) {
	 *             CustomApplcation.getInstance().logout(); startActivity(new
	 *             Intent(context, LoginActivity.class)); finish();
	 *             dialogInterface.dismiss(); } }); // ��ʾȷ�϶Ի��� dialog.show();
	 *             dialog = null; }
	 */

	/**
	 * ������ת
	 */
	public void startAnimActivity(Class<?> cla) {
		this.startActivity(new Intent(this, cla));
	}

	/**
	 * ���ڵ�½�����Զ���½����µ��û����ϼ��������ϵļ�����
	 * 
	 * @Title: updateUserInfos
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws kcj
	 *             ������Ϣ����
	 */
	public void updateUserInfos() {
		// ���µ���λ����Ϣ
		updateUserLocation();
		// ��ѯ���û��ĺ����б�(��������б���ȥ���������û���Ŷ),Ŀǰ֧�ֵĲ�ѯ���Ѹ���Ϊ100�������޸����ڵ����������ǰ����BmobConfig.LIMIT_CONTACTS���ɡ�
		// ����Ĭ�ϲ�ȡ���ǵ�½�ɹ�֮�󼴽������б�洢�����ݿ��У������µ���ǰ�ڴ���,
		userManager.queryCurrentContactList(new FindListener<BmobChatUser>() {
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				if (arg0 == BmobConfig.CODE_COMMON_NONE) {
					ShowToast(arg1);
				} else {
					ShowToast("��ѯ�����б�ʧ�ܣ�" + arg1);
				}
			}

			@Override
			public void onSuccess(List<BmobChatUser> arg0) {
				// TODO Auto-generated method stub
				// ���浽application�з���Ƚ�
				CustomApplcation.getInstance().setContactList(
						CollectionUtils.list2map(arg0));
			}
		});
	}

	/**
	 * �����û��ľ�γ����Ϣ
	 * 
	 * @Title: uploadLocation
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws kcj
	 *             �ٶȶ�λ����
	 */
	public void updateUserLocation() {
		if (CustomApplcation.lastPoint != null) {
			// ��һ�α���ľ�γ��
			String saveLatitude = mApplication.getLatitude();
			String saveLongtitude = mApplication.getLongtitude();
			// ÿ������ʱȫ���еľ�γ��
			String newLat = String.valueOf(CustomApplcation.lastPoint
					.getLatitude());
			String newLong = String.valueOf(CustomApplcation.lastPoint
					.getLongitude());
			// ShowLog("saveLatitude ="+saveLatitude+",saveLongtitude = "+saveLongtitude);
			// ShowLog("newLat ="+newLat+",newLong = "+newLong);
			if (!saveLatitude.equals(newLat) || !saveLongtitude.equals(newLong)) {
				// ֻ��λ���б仯�͸��µ�ǰλ�ã��ﵽʵʱ���µ�Ŀ��
				final User user = (User) userManager.getCurrentUser(User.class);
				user.setLocation(CustomApplcation.lastPoint);
				user.update(this, new UpdateListener() {
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						CustomApplcation.getInstance()
								.setLatitude(
										String.valueOf(user.getLocation()
												.getLatitude()));
						CustomApplcation.getInstance().setLongtitude(
								String.valueOf(user.getLocation()
										.getLongitude()));
						// ShowLog("��γ�ȸ��³ɹ�");
					}

					@Override
					public void onFailure(int code, String msg) {
						// TODO Auto-generated method stub
						// ShowLog("��γ�ȸ��� ʧ��:"+msg);
					}
				});
			} else {
				// ShowLog("�û�λ��δ�������仯");
			}
		}
	}
	
	protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
}
