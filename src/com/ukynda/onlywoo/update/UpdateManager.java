package com.ukynda.onlywoo.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap; 

import com.ukynda.onlywoo.R; 
 
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;


@SuppressLint("HandlerLeak")
public class UpdateManager
{
	/* �����Ƿ� */
	private static final int UPDATE = 10;
	/* ������ */
	private static final int DOWNLOAD = 11;
	/* ���ؽ��� */
	private static final int DOWNLOAD_FINISH = 12;
	/* ���������XML��Ϣ */
	HashMap<String, String> mHashMap;
	/* ���ر���·�� */
	private String mSavePath;
	/* ��¼���������� */
	private int progress;
	/* �Ƿ�ȡ������ */
	private boolean cancelUpdate = false; 
	private Context mContext;
	/* ���½����� */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog; 
	
	private String CurVersionName=null; 
	private String SerVersionName=null;
	private static int CurVersionCode=0;
	private static int SerVersionCode=0;
	 
	//ȷ�����캯��
	public UpdateManager(Context context)
	{
		this.mContext = context;
	}
	//�ṩ���ʽӿ�,�ж��Ƿ���Ҫ����
	public void CheckUpdateInfo()
	{
		getCurVisionInfo();
		getSerVisionInfo(); 
		Log.v("CurVersionCode", String.valueOf(CurVersionCode)); 
		Log.v("SerVersionCode", String.valueOf(SerVersionCode)); 
	}
	//��ȡ��ǰ�汾��Ϣ
	private void getCurVisionInfo()
	{ 
		try
		{
			// ��ȡ����汾�ţ���ӦAndroidManifest.xml��android:versionCode			
			CurVersionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
			CurVersionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
			Log.v("CurVersionCode",String.valueOf(CurVersionCode));
			Log.v("CurVersionName",String.valueOf(CurVersionName));
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		} 
	}
	//��ȡ�������汾��
	public void getSerVisionInfo()
	{
		new Thread(new Runnable() {
			public void run() { 
				 	URL url = null;
					try {
						url = new URL(mContext.getString(R.string.serverurl).toString());
					} catch (MalformedURLException e) { 
						e.printStackTrace();
					}
					Log.v("url", url.toString());	
					HttpURLConnection connection = null;
					try {
						connection = (HttpURLConnection) url.openConnection();
					} catch (IOException e) { 
						e.printStackTrace();
					}
					Log.v("connection", connection.toString()); 
					InputStream inStream = null;
					try {
						inStream = connection.getInputStream();
					} catch (IOException e) {
						 
						e.printStackTrace();
					}
					Log.v("inStream", inStream.toString()); 
					ParseXmlService service = new ParseXmlService();
					try {
						mHashMap = service.parseXml(inStream);
					} catch (Exception e) {
						 
						e.printStackTrace();
					}
					Log.v("hashmanp", mHashMap.toString());	 
					Message message = new Message();
					message.what = UPDATE;
					mHandler.sendMessage(message);
					connection.disconnect();
			}
		}).start(); 
	}
	private Handler mHandler = new Handler()
	{		
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{ 
			case UPDATE: 
				if(mHandler!=null)
				{
					SerVersionCode = Integer.valueOf(mHashMap.get("versionCode"));
					SerVersionName =  mHashMap.get("versionName").toString();
					Log.v("SerVersionCode",String.valueOf(SerVersionCode));
					Log.v("SerVersionName",String.valueOf(SerVersionName));
					if(SerVersionCode>CurVersionCode)
					{
						showNoticeDialog();
					}
					else {
						//Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_SHORT).show();
					}
				}
				break;
			case DOWNLOAD: 
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH: 
				installApk();
				break; 
			default:
				break;
			}
		};
	}; 
	public void showNoticeDialog( )
	{ 
		
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(mHashMap.get("Content"));	
		// ����
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss(); 
				showDownloadDialog();
			}
		});
		// �Ժ����
		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		 builder.show();
	}

	/**
	 * ��ʾ������ضԻ���
	 */
	@SuppressLint("InflateParams")
	private void showDownloadDialog()
	{
		// ����������ضԻ���
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// �����ضԻ������ӽ�����
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// ȡ������
		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss(); 
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// �����ļ�
		downloadApk();
	}

	/**
	 * ����apk�ļ�
	 */
	private void downloadApk()
	{
		// �������߳��������
		new downloadApkThread().start();
	}

	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// �ж�SD���Ƿ���ڣ������Ƿ���ж�дȨ��
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// ��ô洢����·��
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("url"));
					// ��������
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// ��ȡ�ļ���С
					int length = conn.getContentLength();
					// ����������
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// �ж��ļ�Ŀ¼�Ƿ����
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("versionName"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// ����
					byte buf[] = new byte[1024];
					// д�뵽�ļ���
					do
					{
						int numread = is.read(buf);
						count += numread;
						// ���������λ��
						progress = (int) (((float) count / length) * 100);
						// ���½���
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// �������
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// д���ļ�
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// ���ȡ����ֹͣ����.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// ȡ�����ضԻ�����ʾ
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * ��װAPK�ļ�
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, mHashMap.get("versionName"));
		if (!apkfile.exists())
		{
			return;
		}
		// ͨ��Intent��װAPK�ļ�
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
