package com.kcj.friendsearch.ui;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.task.BRequest;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;

import com.kcj.friendsearch.adapter.AddFriendAdapter;
import com.kcj.friendsearch.util.CollectionUtils;
import com.kcj.friendsearch.view.list.XListView;
import com.kcj.friendsearch.view.list.XListView.IXListViewListener;
import com.xiapu.whereisfriend.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddFriendActivity extends ActivityBase implements OnClickListener,
		IXListViewListener, OnItemClickListener {

	EditText et_find_name;
	Button btn_search;
	List<BmobChatUser> users = new ArrayList<BmobChatUser>();
	XListView mListView;
	AddFriendAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcontact);
		initView();
	}

	private void initView() {
		initTopBarForLeft("查找好友");
		et_find_name = (EditText) findViewById(R.id.et_find_name);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);
		initXListView();
	}

	private void initXListView() {
		mListView = (XListView) findViewById(R.id.list_search);
		// 首先不允许加载更多
		mListView.setPullLoadEnable(false);
		// 不允许下拉
		mListView.setPullRefreshEnable(false);
		// 设置监听器
		mListView.setXListViewListener(this);
		//
		mListView.pullRefreshing();
		adapter = new AddFriendAdapter(this, users);
		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(this);
	}

	int curPage = 0;
	ProgressDialog progress;

	private void initSearchList(final boolean isUpdate) {
		if (!isUpdate) {
			progress = new ProgressDialog(AddFriendActivity.this);
			progress.setMessage("正在搜索...");
			progress.setCanceledOnTouchOutside(true);
			progress.show();
		}
		userManager.queryUserByPage(isUpdate, 0, searchName,
				new FindListener<BmobChatUser>() {
					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						if (users != null) {
							users.clear();
						}
						ShowToast("用户不存在");
						mListView.setPullLoadEnable(false);
						refreshPull();
						// 这样能保证每次查询都是从头开始
						curPage = 0;
					}

					@Override
					public void onSuccess(List<BmobChatUser> arg0) {
						// TODO Auto-generated method stub
						if (CollectionUtils.isNotNull(arg0)) {
							if (isUpdate) {
								users.clear();
							}
							adapter.addAll(arg0);
							if (arg0.size() < BRequest.QUERY_LIMIT_COUNT) {
								mListView.setPullLoadEnable(false);
								ShowToast("用户搜索完成!");
							} else {
								mListView.setPullLoadEnable(true);
							}
						} else {
							if (users != null) {
								users.clear();
							}
							ShowToast("用户不存在");
						}
						if (!isUpdate) {
							progress.dismiss();
						} else {
							refreshPull();
						}
						// 这样能保证每次查询都是从头开始
						curPage = 0;
					}
				});
	}

	/**
	 * 查询更多
	 * 
	 * @Title: queryMoreNearList
	 * @Description: TODO
	 * @param @param page
	 * @return void
	 * @throws
	 */
	private void queryMoreSearchList(int page) {
		userManager.queryUserByPage(true, page, searchName,
				new FindListener<BmobChatUser>() {

					@Override
					public void onSuccess(List<BmobChatUser> arg0) {
						// TODO Auto-generated method stub
						if (CollectionUtils.isNotNull(arg0)) {
							adapter.addAll(arg0);
						}
						refreshLoad();
					}

					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub
						ShowToast("搜索更多用户出错:" + arg1);
						mListView.setPullLoadEnable(false);
						refreshLoad();
					}

				});
	}

	String searchName = "";

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_search:// 搜索
			users.clear();
			searchName = et_find_name.getText().toString();
			if (searchName != null && !searchName.equals("")) {
				initSearchList(false);
			} else {
				ShowToast("请输入用户名");
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		userManager.querySearchTotalCount(searchName, new CountListener() {

			@Override
			public void onSuccess(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 > users.size()) {
					curPage++;
					queryMoreSearchList(curPage);
				} else {
					ShowToast("数据加载完成");
					mListView.setPullLoadEnable(false);
					refreshLoad();
				}
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				ShowToast("查询附近的人总数失败" + arg1);
				refreshLoad();
			}
		});
	}

	private void refreshPull() {
		if (mListView.getPullRefreshing()) {
			mListView.stopRefresh();
		}
	}

	private void refreshLoad() {
		if (mListView.getPullLoading()) {
			mListView.stopLoadMore();
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		// BmobChatUser user = (BmobChatUser) adapter.getItem(position-1);
		// Intent intent =new Intent(this,SetMyInfoActivity.class);
		// intent.putExtra("from", "add");
		// intent.putExtra("username", user.getUsername());
		// startAnimActivity(intent);
	}

}
