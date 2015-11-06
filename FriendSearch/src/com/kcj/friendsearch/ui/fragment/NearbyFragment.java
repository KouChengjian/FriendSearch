package com.kcj.friendsearch.ui.fragment;

import com.kcj.friendsearch.ui.FragmentBase;
import com.kcj.friendsearch.ui.PoiResultActivity;
import com.kcj.friendsearch.ui.PoiSearchActivity;
import com.kcj.friendsearch.view.MyImageView;
import com.xiapu.whereisfriend.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class NearbyFragment extends FragmentBase {

	MyImageView imgEatView, imgDrinkView, imgPlayView, imgCheerfulView,
			imgView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		return inflater.inflate(R.layout.fragment_nearby, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initListen();
		initAnimationBackground();
	}

	public void initView() {
		initTopBarForOnlyTitle("����");
		imgEatView = (MyImageView) findViewById(R.id.img_eat);
		imgDrinkView = (MyImageView) findViewById(R.id.img_drink);
		imgPlayView = (MyImageView) findViewById(R.id.img_play);
		imgCheerfulView = (MyImageView) findViewById(R.id.img_cheerful);
		imgView = (MyImageView) findViewById(R.id.img_);
	}

	public void initListen() {

		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				if (v.equals(imgEatView)) {
					Intent intent = new Intent(getActivity(),
							PoiResultActivity.class);
					intent.putExtra("headline", "��");
					startActivity(intent);
				} else if (v.equals(imgDrinkView)) {
					Intent intent = new Intent(getActivity(),
							PoiResultActivity.class);
					intent.putExtra("headline", "ʳ");
					startActivity(intent);
				} else if (v.equals(imgPlayView)) {
					Intent intent = new Intent(getActivity(),
							PoiResultActivity.class);
					intent.putExtra("headline", "ס");
					startActivity(intent);
				} else if (v.equals(imgCheerfulView)) {
					Intent intent = new Intent(getActivity(),
							PoiResultActivity.class);
					intent.putExtra("headline", "��");
					startActivity(intent);
				} else if (v.equals(imgView)) {
					startAnimActivity(PoiSearchActivity.class);
				}
			}
		};
		imgEatView.setOnClickListener(btnClickListener);
		imgDrinkView.setOnClickListener(btnClickListener);
		imgPlayView.setOnClickListener(btnClickListener);
		imgCheerfulView.setOnClickListener(btnClickListener);
		imgView.setOnClickListener(btnClickListener);
	}

	private ImageView runImage;
	TranslateAnimation left, right;

	public void initAnimationBackground() {
		runImage = (ImageView) findViewById(R.id.run_image);
		runAnimation();
	}

	public void runAnimation() {
		right = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
				Animation.RELATIVE_TO_PARENT, -1f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f);
		left = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f, Animation.RELATIVE_TO_PARENT, 0f);
		right.setDuration(25000);
		left.setDuration(25000);
		right.setFillAfter(true);
		left.setFillAfter(true);
		right.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				runImage.startAnimation(left);
			}
		});
		left.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				runImage.startAnimation(right);
			}
		});
		runImage.startAnimation(right);
	}
}
