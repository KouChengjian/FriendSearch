package com.kcj.friendsearch.util;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import android.content.Context;
import android.widget.Toast;

/**
 * 版本更新类
 * 
 * @author Msquirrel
 * 
 */
public class UpateVersion {
	
	
	private Context context = null;

	public UpateVersion(Context _context) {
		this.context = _context;
	}

	public void updateVersion() {
		UmengUpdateAgent.setDownloadListener(null);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case 0: // has update
					UmengUpdateAgent.showUpdateDialog(context, updateInfo);
					break;
				case 1: // has no update
					Toast.makeText(context, "没有更新", Toast.LENGTH_SHORT).show();
					break;
				case 2: // none wifi
					Toast.makeText(context, "没有wifi连接， 只在wifi下更新",
							Toast.LENGTH_SHORT).show();
					break;
				case 3: // time out
					Toast.makeText(context, "超时", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
		UmengUpdateAgent.update(context);
	}
}
