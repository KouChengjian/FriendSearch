package com.kcj.friendsearch.ui;



import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;

import com.kcj.friendsearch.CustomApplcation;
import com.kcj.friendsearch.view.HeaderLayout;
import com.kcj.friendsearch.view.HeaderLayout.HeaderStyle;
import com.kcj.friendsearch.view.HeaderLayout.onLeftImageButtonClickListener;
import com.kcj.friendsearch.view.HeaderLayout.onRightImageButtonClickListener;
import com.umeng.analytics.MobclickAgent;
import com.xiapu.whereisfriend.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Fragmenet 基类
 * 
 * @ClassName: FragmentBase
 * @Description: TODO
 * @author smile
 * @date 2014-5-22 下午2:43:50
 */
public abstract class FragmentBase extends Fragment {
	public BmobUserManager userManager;
	public BmobChatManager manager;
	/**
	 * 公用的Header布局
	 */
	public HeaderLayout mHeaderLayout;

	protected View contentView;

	public LayoutInflater mInflater;

	private Handler handler = new Handler();
	
	protected int mScreenWidth;
	protected int mScreenHeight;

	public void runOnWorkThread(Runnable action) {
		new Thread(action).start();
	}

	public void runOnUiThread(Runnable action) {
		handler.post(action);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		MobclickAgent.onError(getActivity());  //捕捉未捕捉异常，通过调用这个方法，可以收集引起程序异常退出的错误信息，如不用可注释
		// 获取手机分辨率
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		userManager = BmobUserManager.getInstance(getActivity());
		manager = BmobChatManager.getInstance(getActivity());
		mApplication = CustomApplcation.getInstance();
		mInflater = LayoutInflater.from(getActivity());
	}

	public FragmentBase() {

	}

	Toast mToast;

	public void ShowToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}

	public void ShowToast(int text) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_LONG);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}

	public View findViewById(int paramInt) {
		return getView().findViewById(paramInt);
	}

	public CustomApplcation mApplication;

	/**
	 * 只有title initTopBarLayoutByTitle
	 * 
	 * @Title: initTopBarLayoutByTitle
	 * @throws
	 */
	public void initTopBarForOnlyTitle(String titleName) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.DEFAULT_TITLE);
		mHeaderLayout.setDefaultTitle(titleName);
	} 

	/**
	 * 初始化标题栏-带左右按钮
	 * 
	 * @return void
	 * @throws
	 */
	public void initTopBarForBoth(String titleName, int rightDrawableId,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_menu_bg_selector,
				new OnLeftButtonClickListener());
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
	}

	/**
	 * kcj 自定义标题栏-带左右按钮
	 
	public void initTopBarForBoth(String titleName, int rightDrawableId,
			onRightImageButtonClickListener listener, int leftDrawableId,
			onLeftImageButtonClickListener leftlistener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_DOUBLE_IMAGEBUTTON);

		mHeaderLayout.setTitleAndLeftButton(titleName, leftDrawableId,
				leftlistener);

		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
	}*/

	/**
	 * 只有左边按钮和Title initTopBarLayout
	 * 
	 * @throws
	 */
	public void initTopBarForLeft(String titleName) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_LIFT_IMAGEBUTTON);
		mHeaderLayout.setTitleAndLeftImageButton(titleName,
				R.drawable.base_action_bar_menu_bg_selector,
				new OnLeftButtonClickListener());
	}

	/**
	 * 右边+title initTopBarForRight
	 * 
	 * @return void
	 * @throws
	 
	public void initTopBarForRight(String titleName, int rightDrawableId,
			onRightImageButtonClickListener listener) {
		mHeaderLayout = (HeaderLayout) findViewById(R.id.common_actionbar);
		mHeaderLayout.init(HeaderStyle.TITLE_RIGHT_IMAGEBUTTON);
		mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
				listener);
	}*/
  /** */
	// 左边按钮的点击事件
	public class OnLeftButtonClickListener implements
			onLeftImageButtonClickListener {
		@Override
		public void onClick(View v) {
			MainActivity.getInstance().showMenu();
		}
	} 

	/**
	 * 动画启动页面 startAnimActivity
	 * 
	 * @throws
	 */
	public void startAnimActivity(Intent intent) {
		this.startActivity(intent);
	}

	public void startAnimActivity(Class<?> cla) {
		getActivity().startActivity(new Intent(getActivity(), cla));
	}
	
	public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }
}
