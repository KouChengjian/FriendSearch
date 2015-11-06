package com.kcj.friendsearch.ui.fragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;



import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.mapapi.model.LatLng;
import com.kcj.friendsearch.CustomApplcation;
import com.kcj.friendsearch.adapter.UserFriendAdapter;
import com.kcj.friendsearch.bean.User;
import com.kcj.friendsearch.ui.AddFriendActivity;
import com.kcj.friendsearch.ui.FragmentBase;
import com.kcj.friendsearch.ui.MainActivity;
import com.kcj.friendsearch.ui.NearPeopleActivity;
import com.kcj.friendsearch.ui.NewFriendActivity;
import com.kcj.friendsearch.util.CharacterParser;
import com.kcj.friendsearch.util.CollectionUtils;
import com.kcj.friendsearch.util.PinyinComparator;
import com.kcj.friendsearch.view.ClearEditText;
import com.kcj.friendsearch.view.MyLetterView;
import com.kcj.friendsearch.view.HeaderLayout.onRightImageButtonClickListener;
import com.kcj.friendsearch.view.MyLetterView.OnTouchingLetterChangedListener;
import com.kcj.friendsearch.view.dialog.DialogTips;
import com.xiapu.whereisfriend.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;



@SuppressLint("DefaultLocale")
public class ContactFragment extends FragmentBase implements OnItemClickListener,OnItemLongClickListener{

	ClearEditText mClearEditText;
	List<User> friends = new ArrayList<User>();;
	ListView list_friends;
	private UserFriendAdapter userAdapter;// ����
	/**
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	
	/**
	 * ���ƴ��������ListView����������
	 */
	private PinyinComparator pinyinComparator;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		return inflater.inflate(R.layout.fragment_contacts, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	
    public void initView(){
    	pinyinComparator = new PinyinComparator();
    	characterParser = CharacterParser.getInstance();
    	initTopBarForBoth("����", R.drawable.base_action_bar_add_bg_selector,
				new onRightImageButtonClickListener() {
					@Override
					public void onClick(View v) {
						startAnimActivity(AddFriendActivity.class);
					}
				});
    	initListView();
    	initRightLetterView();
    	initEditText();
	}
    
    private void initEditText() {
		mClearEditText = (ClearEditText)findViewById(R.id.et_msg_search);
		// ������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// ������������ֵΪ�գ�����Ϊԭ�����б?����Ϊ��������б�
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
    
    /**
	 * ���������е�ֵ��������ݲ�����ListView
	 * 
	 * @param filterStr kcj
	 */
	private void filterData(String filterStr) {
		List<User> filterDateList = new ArrayList<User>();
		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = friends;
		} else {
			filterDateList.clear();
			for (User sortModel : friends) {
				String name = sortModel.getUsername();
				if (name != null) {
					if (name.indexOf(filterStr.toString()) != -1
							|| characterParser.getSelling(name).startsWith(
									filterStr.toString())) {
						filterDateList.add(sortModel);
					}
				}
			}
		}
		// ���a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		userAdapter.updateListView(filterDateList);
	}
    
    TextView dialog;
    MyLetterView right_letter;
    
    /**
	 *  �ұ�״̬��
	 */
	private void initRightLetterView() {
		right_letter = (MyLetterView)findViewById(R.id.right_letter);
		dialog = (TextView)findViewById(R.id.dialog);
		right_letter.setTextView(dialog);
		right_letter.setOnTouchingLetterChangedListener(new LetterListViewListener());
	}
	
	/**
	 *  ���
	 */
	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {
		@Override
		public void onTouchingLetterChanged(String s) {
			// ����ĸ�״γ��ֵ�λ��
			int position = userAdapter.getPositionForSection(s.charAt(0));
			if (position != -1) {
				list_friends.setSelection(position);
			}
		}
	}
    
	ImageView iv_msg_tips;
	TextView tv_new_name;
	LinearLayout layout_new;//������
	LinearLayout layout_near;//�������
	
    private void initListView() {
    	list_friends = (ListView) findViewById(R.id.list_friends);
    	
    	RelativeLayout headView = (RelativeLayout) mInflater.inflate(R.layout.include_new_friend, null);
		iv_msg_tips = (ImageView)headView.findViewById(R.id.iv_msg_tips);
		layout_new =(LinearLayout)headView.findViewById(R.id.layout_new);
		layout_near =(LinearLayout)headView.findViewById(R.id.layout_near);
		layout_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), NewFriendActivity.class);
				intent.putExtra("from", "contact");
				startAnimActivity(intent);
			}
		});
		layout_near.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), NearPeopleActivity.class);
				startAnimActivity(intent);
			}
		});
		
		list_friends.addHeaderView(headView);
    	
    	userAdapter = new UserFriendAdapter(getActivity(), friends);
		list_friends.setAdapter(userAdapter);
		list_friends.setOnItemClickListener(this);
		list_friends.setOnItemLongClickListener(this);
		
		list_friends.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    Log.e("TAG",String.valueOf(list_friends.getFirstVisiblePosition()));
                    Log.e("TAG",String.valueOf(list_friends.getLastVisiblePosition()));
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            
            }
        });
    }
	
	/** ��ȡ�����б�
	  * queryMyfriends
	  * @return void
	  * @throws
	  */
	private void queryMyfriends() {
		//�Ƿ����µĺ�������
		if(BmobDB.create(getActivity()).hasNewInvite()){
			iv_msg_tips.setVisibility(View.VISIBLE);
		}else{
			iv_msg_tips.setVisibility(View.GONE);
		}
		Map<String,BmobChatUser> users = CustomApplcation.getInstance().getContactList();
		//��װ�µ�User
		filledData(CollectionUtils.map2list(users));
		if(userAdapter==null){
			userAdapter = new UserFriendAdapter(getActivity(), friends);
			list_friends.setAdapter(userAdapter);
		}else{
			userAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * ΪListView������
	 * @param date
	 * @return kcj
	 */
	private void filledData(List<BmobChatUser> datas) {
		friends.clear();
		int total = datas.size();
		for (int i = 0; i < total; i++) {
			BmobChatUser user = datas.get(i);
			User sortModel = new User();
			sortModel.setAvatar(user.getAvatar());
			sortModel.setNick(user.getNick());
			sortModel.setUsername(user.getUsername());
			sortModel.setObjectId(user.getObjectId());
			sortModel.setContacts(user.getContacts());
			//sortModel.setLocation(user.get);
			// ����ת����ƴ��
			String username = sortModel.getNick();
			// ��û��username
			if (username != null) {
				String pinyin = characterParser.getSelling(sortModel.getNick());
				String sortString = pinyin.substring(0, 1).toUpperCase();
				// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase());
				} else {
					sortModel.setSortLetters("#");
				}
			} else {
				sortModel.setSortLetters("#");
			}
			friends.add(sortModel);
		}
		// ���a-z��������
		Collections.sort(friends, pinyinComparator);
	}
	
	
	private boolean hidden;
	/**
	 * �Ƿ�����  true ����  false ��ʾ
	 */
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		if(!hidden){
			refresh();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if(!hidden){
			refresh();
		}
	}
	
	public void refresh(){
		try {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					queryMyfriends();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		User user = (User) userAdapter.getItem(position-1);
		showDeleteDialog(user);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		User user = (User) userAdapter.getItem(position-1);
		initOtherData(user.getUsername());
		//Toast.makeText(getActivity(), String.valueOf(location.getLatitude()), 1).show();
	}
	
	User user;
	private void initOtherData(String name) {
		userManager.queryUser(name, new FindListener<User>() {
			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("onError onError:" + arg1);
			}
			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				if (arg0 != null && arg0.size() > 0) {
					user = arg0.get(0);
                    if(!user.getAllowFriendKnowLocation()){
                    	ShowToast("�Բ�����ĺ��Ѿܾ��ñ���֪��λ��");
                    	return;
                    }
                    if(user.getLatitude() == 0 || user.getLongitude() == 0){
                        ShowToast("�Բ���δ���ֺ���λ��,�鿴�Ƿ��ֻ������ȡλ����Ϣ��");
                        return;
                    }
					MainActivity.getInstance().setLatLng(new LatLng(user.getLatitude(),user.getLongitude()));
					MainActivity.getInstance().setIndex(0);
					MainActivity.getInstance().setFriendSearch(true);
					MainActivity.getInstance().fragmentCurrentShow();
					Intent intent = new Intent();
					intent.setAction("action.nearMapLocation");
					getActivity().sendBroadcast(intent);
				} else {
					ShowToast("���޴��ˣ��Է��Ƿ��ѽ���ɾ��");
				}
			}
		});
	}
	
	public void showDeleteDialog(final User user) {
		DialogTips dialog = new DialogTips(getActivity(),user.getUsername(),"ɾ����ϵ��", "ȷ��",true,true);
		// ���óɹ��¼�
		dialog.SetOnSuccessListener(new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int userId) {
				deleteContact(user);
			}
		});
		// ��ʾȷ�϶Ի���
		dialog.show();
		dialog = null;
	}
	
	/** ɾ����ϵ��
	  * deleteContact
	  * @return void
	  * @throws
	  */
	private void deleteContact(final User user){
		final ProgressDialog progress = new ProgressDialog(getActivity());
		progress.setMessage("����ɾ��...");
		progress.setCanceledOnTouchOutside(false);
		progress.show();
		userManager.deleteContact(user.getObjectId(), new UpdateListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ShowToast("ɾ��ɹ�");
				//ɾ���ڴ�
				CustomApplcation.getInstance().getContactList().remove(user.getUsername());
				//���½���
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						progress.dismiss();
						userAdapter.remove(user);
					}
				});
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("ɾ��ʧ�ܣ�"+arg1);
				progress.dismiss();
			}
		});
	}
	
	/**
	 * �������侭γ����꣨doubleֵ���������������룬
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return ���룺��λΪ��
	 */
	public static double DistanceOfTwoPoints(double lat1, double lng1,
			double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	private static final double EARTH_RADIUS = 6378137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}
}
