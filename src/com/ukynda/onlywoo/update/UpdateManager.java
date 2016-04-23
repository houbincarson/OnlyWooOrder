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
	/* 更新是否 */
	private static final int UPDATE = 10;
	/* 下载中 */
	private static final int DOWNLOAD = 11;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 12;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false; 
	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog; 
	
	private String CurVersionName=null; 
	private String SerVersionName=null;
	private static int CurVersionCode=0;
	private static int SerVersionCode=0;
	 
	//确定构造函数
	public UpdateManager(Context context)
	{
		this.mContext = context;
	}
	//提供访问接口,判断是否需要更新
	public void CheckUpdateInfo()
	{
		getCurVisionInfo();
		getSerVisionInfo(); 
		Log.v("CurVersionCode", String.valueOf(CurVersionCode)); 
		Log.v("SerVersionCode", String.valueOf(SerVersionCode)); 
	}
	//获取当前版本信息
	private void getCurVisionInfo()
	{ 
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode			
			CurVersionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
			CurVersionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
			Log.v("CurVersionCode",String.valueOf(CurVersionCode));
			Log.v("CurVersionName",String.valueOf(CurVersionName));
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		} 
	}
	//获取服务器版本号
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
		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss(); 
				showDownloadDialog();
			}
		});
		// 稍后更新
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
	 * 显示软件下载对话框
	 */
	@SuppressLint("InflateParams")
	private void showDownloadDialog()
	{
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
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
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("url"));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("versionName"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
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
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath, mHashMap.get("versionName"));
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
