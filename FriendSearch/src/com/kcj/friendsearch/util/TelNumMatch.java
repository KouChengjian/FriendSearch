package com.kcj.friendsearch.util;

import android.util.Log;

/**
 * �����ж�һ�������Ƿ����ֻ���
 * 
 * @author Administrator
 * 
 */
public class TelNumMatch {

	/*
	 * �ƶ�: 2G�Ŷ�(GSM����)��139,138,137,136,135,134,159,158,152,151,150,
	 * 3G�Ŷ�(TD-SCDMA����)��157,182,183,188,187 147���ƶ�TD������ר�úŶ�. ��ͨ:
	 * 2G�Ŷ�(GSM����)��130,131,132,155,156 3G�Ŷ�(WCDMA����)��186,185 ����:
	 * 2G�Ŷ�(CDMA����)��133,153 3G�Ŷ�(CDMA����)��189,180
	 */
	static String YD = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[2378]{1})|([4]{1}[7]{1}))[0-9]{8}$";
	static String LT = "^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([8]{1}[56]{1}))[0-9]{8}$";
	static String DX = "^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([8]{1}[09]{1}))[0-9]{8}$";

	String mobPhnNum;

	public TelNumMatch(String mobPhnNum) {
		this.mobPhnNum = mobPhnNum;
		Log.d("tool", mobPhnNum);
	}

	public int matchNum() {
		/**
		 * flag = 1 YD 2 LT 3 DX 
		 */
		int flag;// �洢ƥ����
		// �ж��ֻ������Ƿ���11λ
		if (mobPhnNum.length() == 11) {
			// �ж��ֻ������Ƿ�����й��ƶ��ĺ������
			if (mobPhnNum.matches(YD)) {
				flag = 1;
			}
			// �ж��ֻ������Ƿ�����й���ͨ�ĺ������
			else if (mobPhnNum.matches(LT)) {
				flag = 2;
			}
			// �ж��ֻ������Ƿ�����й����ŵĺ������
			else if (mobPhnNum.matches(DX)) {
				flag = 3;
			}
			// �������� δ֪
			else {
				flag = 4;
			}
		}
		// ����11λ
		else {
			flag = 5;
		}
		Log.d("TelNumMatch", "flag"+flag);
		return flag;
	}
}
