package com.kcj.friendsearch.view;



import com.kcj.friendsearch.util.PixelUtil;
import com.xiapu.whereisfriend.R;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/** 自定义头部布局
 * @ClassName: HeaderLayout
 * @Description: TODO
 * @author smile
 * @date 2014-9-12 下午2:30:30
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HeaderLayout extends LinearLayout{
	private LayoutInflater mInflater;
	private View mHeader;
	private LinearLayout mLayoutLeftContainer;
	private LinearLayout mLayoutRightContainer;
	private TextView mHtvSubTitle;
	private LinearLayout mLayoutRightImageButtonLayout;
	private Button mRightImageButton;
	private onRightImageButtonClickListener mRightImageButtonClickListener;
	private RelativeLayout mLayoutLeftImageButtonLayout;
	private ImageButton mLeftImageButton;
	private Button mLeftButton;
	private onLeftImageButtonClickListener mLeftImageButtonClickListener;

	public enum HeaderStyle {// 头部整体样式
		DEFAULT_TITLE, TITLE_LIFT_IMAGEBUTTON, TITLE_RIGHT_IMAGEBUTTON, TITLE_DOUBLE_IMAGEBUTTON;
	}
	Context context;
	public HeaderLayout(Context context) {
		super(context);
		this.context = context;
		init(context);
	}

	public HeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 *  初始化自定义头部布局
	 */
	public void init(Context context) {
		mInflater = LayoutInflater.from(context);
		mHeader = mInflater.inflate(R.layout.common_header, null);
		addView(mHeader);
		initViews();
	}

	/**
	 *  初始化自定义头部布局中的布局
	 */
	public void initViews() {
		mLayoutLeftContainer = (LinearLayout) findViewByHeaderId(R.id.header_layout_leftview_container);
		// mLayoutMiddleContainer = (LinearLayout)
		// findViewByHeaderId(R.id.header_layout_middleview_container);中间部分添加搜索或者其他按钮时可打开
		mLayoutRightContainer = (LinearLayout) findViewByHeaderId(R.id.header_layout_rightview_container);
		mHtvSubTitle = (TextView) findViewByHeaderId(R.id.header_htv_subtitle);
	}

	public View findViewByHeaderId(int id) {
		return mHeader.findViewById(id);
	}

	/**
	 *  选择哪种的标题栏
	 */
	public void init(HeaderStyle hStyle) {
		switch (hStyle) {
		case DEFAULT_TITLE:
			defaultTitle();
			break;

		case TITLE_LIFT_IMAGEBUTTON:
			defaultTitle();
			titleLeftImageButton();
			break;

		case TITLE_RIGHT_IMAGEBUTTON:
			defaultTitle();
			titleRightImageButton();
			break;

		case TITLE_DOUBLE_IMAGEBUTTON:
			defaultTitle();
			titleLeftImageButton();
			titleRightImageButton();
			break;
		}
	}

	// 默认文字标题
	private void defaultTitle() {
		mLayoutLeftContainer.removeAllViews();
		mLayoutRightContainer.removeAllViews();
	}
  
	// 左侧自定义按钮
	private void titleLeftImageButton() {
		View mleftImageButtonView = mInflater.inflate(
				R.layout.common_header_button, null);
		mLayoutLeftContainer.addView(mleftImageButtonView);
		// 布局
		mLayoutLeftImageButtonLayout = (RelativeLayout) mleftImageButtonView
				.findViewById(R.id.header_layout_imagebuttonlayout);
		// 按钮
		mLeftImageButton = (ImageButton) mleftImageButtonView
				.findViewById(R.id.header_ib_imagebutton);
		mLeftButton = (Button) mleftImageButtonView
				.findViewById(R.id.header_ib_button);
		
		mLayoutLeftImageButtonLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mLeftImageButtonClickListener != null) {
					mLeftImageButtonClickListener.onClick( arg0);
				}
			}
		});
	}

	// 右侧自定义按钮
	private void titleRightImageButton() {
		View mRightImageButtonView = mInflater.inflate(
				R.layout.common_header_rightbutton, null);
		mLayoutRightContainer.addView(mRightImageButtonView);
		mLayoutRightImageButtonLayout = (LinearLayout) mRightImageButtonView
				.findViewById(R.id.header_layout_imagebuttonlayout);
		mRightImageButton = (Button) mRightImageButtonView
				.findViewById(R.id.header_ib_imagebutton);
		mLayoutRightImageButtonLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mRightImageButtonClickListener != null) {
					mRightImageButtonClickListener.onClick(arg0);
				}
			}
		});
	}

	/** 获取右边按钮
	  * @Title: getRightImageButton
	  * @Description: TODO
	  * @param @return 
	  * @return Button
	  * @throws
	  */
	public Button getRightImageButton(){
		if(mRightImageButton!=null){
			return mRightImageButton;
		}
		return null;
	}
	
	/**
	 *  设置标题文字
	 */
	public void setDefaultTitle(CharSequence title) {
		if (title != null) {
			mHtvSubTitle.setText(title);
		} else {
			mHtvSubTitle.setVisibility(View.GONE);
		}
	}
	
	/**
	 *  设置标题图片
	 */
	public void setDefaultTitle( ) {
		//if (title != null) {     drawableRight
		    mHtvSubTitle.setWidth(150);
		    //mHtvSubTitle.setRight(R.drawable.header_cards);
			//mHtvSubTitle.setBackgroundResource(R.drawable.header_cards);
		//} else {
		//	mHtvSubTitle.setVisibility(View.GONE);
		//}
	}

	/**
	 *  设置右边Button
	 */
	public void setTitleAndRightButton(CharSequence title, int backid,String text,
			onRightImageButtonClickListener onRightImageButtonClickListener) 
	{
		// 设置标题文字
		setDefaultTitle(title);
		mLayoutRightContainer.setVisibility(View.VISIBLE);
		if (mRightImageButton != null && backid > 0) {
			mRightImageButton.setWidth(PixelUtil.dp2px(45));
			mRightImageButton.setHeight(PixelUtil.dp2px(40));
			mRightImageButton.setBackgroundResource(backid);
			mRightImageButton.setText(text);
			setOnRightImageButtonClickListener(onRightImageButtonClickListener);
		}
	}
	
	/**
	 *  设置右边ImageButton
	 */
	public void setTitleAndRightImageButton(CharSequence title, int backid,
			onRightImageButtonClickListener onRightImageButtonClickListener) {
		setDefaultTitle(title);
		mLayoutRightContainer.setVisibility(View.VISIBLE);
		if (mRightImageButton != null && backid > 0) {
			mRightImageButton.setWidth(PixelUtil.dp2px(30));
			mRightImageButton.setHeight(PixelUtil.dp2px(30));// PixelUtil.dp2px(30)
			//Log.e("PixelUtil.dp2px(30)",String.valueOf(PixelUtil.dp2px(30)));
			mRightImageButton.setTextColor(getResources().getColor(R.color.transparent));
			mRightImageButton.setBackgroundResource(backid);
			setOnRightImageButtonClickListener(onRightImageButtonClickListener);
		}
	}

	/**
	 *  设置左边ImageButton
	 */
	public void setTitleAndLeftImageButton(CharSequence title, int id,
			onLeftImageButtonClickListener listener) {
		setDefaultTitle(title);
		if (mLeftImageButton != null && id > 0) {
			mLeftImageButton.setImageResource(id);
			setOnLeftImageButtonClickListener(listener);
		}
		mLayoutRightContainer.setVisibility(View.INVISIBLE);
	}
	
	/**
	 *  自定义Button
	 */
	public void setTitleAndLeftButton(CharSequence title, int id,
			onLeftImageButtonClickListener listener) {
		setDefaultTitle(title);
		if (mLeftButton != null && id > 0) {
			mLeftButton.setWidth(PixelUtil.dp2px(30));
			mLeftButton.setHeight(PixelUtil.dp2px(30));
			mLeftButton.setTextColor(getResources().getColor(R.color.transparent));
			mLeftButton.setBackgroundResource(id);
			setOnLeftImageButtonClickListener(listener);
		}
		mLayoutRightContainer.setVisibility(View.INVISIBLE);
	}

	public void setOnRightImageButtonClickListener(
			onRightImageButtonClickListener listener) {
		mRightImageButtonClickListener = listener;
	}

	public interface onRightImageButtonClickListener {
		void onClick(View v);
	}

	public void setOnLeftImageButtonClickListener(
			onLeftImageButtonClickListener listener) {
		mLeftImageButtonClickListener = listener;
	}

	public interface onLeftImageButtonClickListener {
		void onClick(View v);
	}
}
