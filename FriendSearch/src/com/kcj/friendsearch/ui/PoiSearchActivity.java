package com.kcj.friendsearch.ui;


import java.util.Random;

import com.kcj.friendsearch.view.KeywordsFlow;
import com.xiapu.whereisfriend.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class PoiSearchActivity extends BaseActivity implements  OnClickListener{
	EditText edt_search;
	ImageView img_break;
	ImageView img_oksearch;
	GestureDetector mGesture = null; 
    private KeywordsFlow keywordsFlow;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poisearch);
		initView();
		initListen();
	}
	
	public void initView(){
		edt_search = (EditText) findViewById(R.id.edt_search);
		img_break = (ImageView) findViewById(R.id.img_break);
		img_oksearch = (ImageView) findViewById(R.id.img_oksearch);
		mGesture = new GestureDetector(this, new GestureListener()); 
		keywordsFlow = (KeywordsFlow) findViewById(R.id.frameLayout1);
		keywordsFlow.setDuration(800l);
		keywordsFlow.setOnItemClickListener(this);
		// 添加
		feedKeywordsFlow(keywordsFlow, keywords);
		keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
		MyCount myCount = new MyCount(600000, 5000);
		myCount.start();
	}
	
	public void initListen(){
		img_break.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		img_oksearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!TextUtils.isEmpty(edt_search.getText().toString())){
					Intent intent = new Intent(PoiSearchActivity.this,
							PoiResultActivity.class);
					intent.putExtra("headline", edt_search.getText().toString());
					startActivity(intent);
				}
			}
		});
	}
	
	private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
		Random random = new Random();
		for (int i = 0; i < KeywordsFlow.MAX; i++) {
			int ran = random.nextInt(arr.length);
			String tmp = arr[ran];
			keywordsFlow.feedKeyword(tmp);
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v instanceof TextView) {
			String keyword = ((TextView) v).getText().toString();
			//Toast.makeText(this, keyword, 1).show();
			Intent intent = new Intent(PoiSearchActivity.this,
					PoiResultActivity.class);
			intent.putExtra("headline", keyword);
			startActivity(intent);
		}
	}
	
	class GestureListener extends SimpleOnGestureListener  
    {  
  
        @Override  
        public boolean onDoubleTap(MotionEvent e)  
        {  
            // TODO Auto-generated method stub  
            Log.i("TEST", "onDoubleTap");  
            return super.onDoubleTap(e);  
        }  
  
        @Override  
        public boolean onDown(MotionEvent e)  
        {  
            // TODO Auto-generated method stub  
            Log.i("TEST", "onDown");  
            return super.onDown(e);  
        }  
  
        @Override  
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
                float velocityY)  
        {  
            // TODO Auto-generated method stub  
            Log.i("TEST", "onFling:velocityX = " + velocityX + " velocityY" + velocityY);  
            return super.onFling(e1, e2, velocityX, velocityY);  
        }  
  
        @Override  
        public void onLongPress(MotionEvent e)  
        {  
            // TODO Auto-generated method stub  
            Log.i("TEST", "onLongPress");  
            super.onLongPress(e);  
        }  
  
        @Override  
        public boolean onScroll(MotionEvent e1, MotionEvent e2,  
                float distanceX, float distanceY)  
        {  
            // TODO Auto-generated method stub  
            Log.i("TEST", "onScroll:distanceX = " + distanceX + " distanceY = " + distanceY);  
            return super.onScroll(e1, e2, distanceX, distanceY);  
        }  
  
        @Override  
        public boolean onSingleTapUp(MotionEvent e)  
        {  
            // TODO Auto-generated method stub  
            Log.i("TEST", "onSingleTapUp");  
            return super.onSingleTapUp(e);  
        }  
          
    }  
	
	private class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		@Override
		public void onFinish() {
			//layoutShowTime.setBackgroundColor(R.color.verification_time_show);
		}
		@Override
		public void onTick(long millisUntilFinished) {
			keywordsFlow.rubKeywords();
			feedKeywordsFlow(keywordsFlow, keywords);
			keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
		}
	}

	public static final String[] keywords = { 
		"火锅", "川菜", "粤菜","小吃", "湘菜",
		"江浙菜", "东北菜", "海鲜", "清真菜", "北京菜",
		"湖北菜", "披萨", "牛排", "意大利菜", "法国菜",
		"德国菜", "中式快餐", "西式快餐", "肯德基", "麦当劳",
		"永和大王", "味千拉面", "真功夫", "冰淇淋", "蛋糕西点",
		"拉美烧烤", "日式烧烤", "韩国烧烤", "咖啡厅", "酒吧",
		"自助餐", "快捷酒店", "星级酒店", "青年旅社", "招待所",
		"电影院", "KTV", "网吧", "丽人", "洗浴",
		"商场", "公交站", "加油站", "停车场", "火车站",
		"超市", "药店", "ATM", "银行" , "医院",
		"厕所", "景点", "地铁出口站"};
}
