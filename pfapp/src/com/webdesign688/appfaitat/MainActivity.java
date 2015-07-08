package com.webdesign688.appfaitat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import com.king.photo.activity.AlbumActivity;
import com.king.photo.activity.GalleryActivity;
import com.king.photo.util.Bimp;
import com.king.photo.util.FileUtils;
import com.king.photo.util.ImageItem;
import com.king.photo.util.PublicWay;
import com.king.photo.util.Res;
import com.webdesign688.appfaitat.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends BaseActivity  {


	private ImageView mIvNamain;
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private Button mBtnMainpush;
	private EditText mEtpeople;
	private EditText mEtTell;
	private EditText mEtAddress;
	private File tempFile1;
	private EditText mEtContent;
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/JOB/";

	public static Bitmap bimap ;
    private static String mStrPeople;
    private static String mStrTell;
    private static String mStrAddress;
    private static String mStrContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		bimap = BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
		PublicWay.activityList.add(this);
		parentView = getLayoutInflater().inflate(R.layout.activity_main, null);
		setContentView(parentView);

	    initView();
		
				
	}

	private void initView() {
		mEtpeople =(EditText)this.findViewById(R.id.mEtpeople);
		mEtTell =(EditText)this.findViewById(R.id.mEtTell);
		mEtAddress =(EditText)this.findViewById(R.id.mEtAddress);
		mEtContent =(EditText)this.findViewById(R.id.mEtContent);
		
		mBtnMainpush =(Button)this.findViewById(R.id.mBtnMainpush);
		mBtnMainpush.setOnClickListener(listener);
		mIvNamain =(ImageView)this.findViewById(R.id.mIvNamain);
		mIvNamain.setOnClickListener(listener);
		Init();

	}

	public void Init() {
		
		pop = new PopupWindow(MainActivity.this);
		
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			      mStrPeople =mEtpeople.getEditableText().toString();
			      mStrTell =mEtTell.getEditableText().toString();
			      mStrAddress =mEtAddress.getEditableText().toString();
			      mStrContent =mEtContent.getEditableText().toString();

				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//用于暂时保存edittext值 
			      mStrPeople =mEtpeople.getEditableText().toString();
			      mStrTell =mEtTell.getEditableText().toString();
			      mStrAddress =mEtAddress.getEditableText().toString();
			      mStrContent =mEtContent.getEditableText().toString();

				
				Intent intent = new Intent(MainActivity.this,
						AlbumActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);	
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					//用于暂时保存edittext值 
				      mStrPeople =mEtpeople.getEditableText().toString();
				      mStrTell =mEtTell.getEditableText().toString();
				      mStrAddress =mEtAddress.getEditableText().toString();
				      mStrContent =mEtContent.getEditableText().toString();

					Intent intent = new Intent(MainActivity.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if(Bimp.tempSelectBitmap.size() == 9){
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position ==Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
				      mEtpeople.setText(mStrPeople);
				      mEtTell.setText(mStrTell);
				      mEtAddress.setText(mStrAddress);
				      mEtContent.setText(mStrContent);

					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		//用于暂时保存edittext值 
	      
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;

	public void photo() {
		
		
		
		
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String fileName = String.valueOf(System.currentTimeMillis());
		if (!FileUtils.isFileExist("")) {
			try {
				File tempf = FileUtils.createSDDir("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		File f = new File(SDPATH, fileName + ".jpg"); 
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(f));
		tempFile1=f;

		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

			/*	
				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				String path =FileUtils.saveBitmap(bm, fileName);
				*/
				Bitmap bitmap = BitmapFactory.decodeFile(tempFile1.getPath());
				Bitmap newBitmap = com.king.photo.util.ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / 5, bitmap.getHeight() / 5);

				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(newBitmap);
				takePhoto.setImagePath(tempFile1.getPath());
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for(int i=0;i<PublicWay.activityList.size();i++){
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			System.exit(0);
		}
		return true;
	}


	OnClickListener listener =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mIvNamain:
			showWindow();
			break;
		case R.id.mBtnMainpush:
			if(TextUtils.isEmpty(mEtpeople.getEditableText().toString())){
				Toast.makeText(getApplicationContext(), R.string.toast1, 0).show();
			}else 			if(TextUtils.isEmpty(mEtTell.getEditableText().toString())){
				Toast.makeText(getApplicationContext(), R.string.toast2, 0).show();
			}else 			if(TextUtils.isEmpty(mEtAddress.getEditableText().toString())){
				Toast.makeText(getApplicationContext(), R.string.toast3, 0).show();
			}else 		if(TextUtils.isEmpty(mEtContent.getEditableText().toString())){
				Toast.makeText(getApplicationContext(), R.string.toast4, 0).show();
			}
			
			else{


			pushEmail();
			}
			break;
	
		default:
			break;
		}
		}

	

	};
	
	private void pushEmail() {
		
		Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE); 
		String[] tos = { "faitat_lpf@yahoo.com.hk" }; 
		String[] ccs = { "  " }; 
		intent.putExtra(Intent.EXTRA_EMAIL, tos); 
		intent.putExtra(Intent.EXTRA_CC, ccs); 
		intent.putExtra(Intent.EXTRA_TEXT, mEtpeople.getEditableText().toString()+" "+mEtTell.getEditableText().toString()+" "+mEtAddress.getEditableText().toString()+" "+mEtContent.getEditableText().toString()); 
		intent.putExtra(Intent.EXTRA_SUBJECT, " "); 
		ArrayList<Uri> imageUris = new ArrayList<Uri>(); 

        for(int i=0;i<Bimp.tempSelectBitmap.size();i++){
        	imageUris.add(Uri.parse("file://"+Bimp.tempSelectBitmap.get(i).getImagePath()));
        }
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris); 
		intent.setType("image/*"); 
		intent.setType("message/rfc882"); 
		Intent.createChooser(intent, "Choose Email Client"); 
		startActivity(intent); 

	}
	
	private PopupWindow mPop;
	private View layout;
	
	private void showWindow() {
		
		
		LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		 layout = mLayoutInflater.inflate(R.layout.popnavigation, null);
			layout.setFocusable(true); // 这个很重要  
			layout.setFocusableInTouchMode(true);  
		 mPop = new PopupWindow(layout,270, LayoutParams.WRAP_CONTENT);
		 mPop.setFocusable(true);
		 mPop.setOutsideTouchable(true);
		 mPop.update();
			//位置在R.id.button的正下方
		 mPop.showAsDropDown(findViewById(R.id.mIvNamain), 15,8);
		ListView portal_select_listView = (ListView) layout.findViewById(R.id.mLvNavi);
		ArrayList androi = new ArrayList();
		androi.clear();
		androi.add(getResources().getString(R.string.na1));
		androi.add(getResources().getString(R.string.na2));
		androi.add(getResources().getString(R.string.na3));
		androi.add(getResources().getString(R.string.na4));
		androi.add(getResources().getString(R.string.na5));
		androi.add(getResources().getString(R.string.na6));
		androi.add(getResources().getString(R.string.na8));
		portal_select_listView.setAdapter( new ArrayAdapter(this,R.layout.item_textview,androi));
		portal_select_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==0){
               startActivity(new Intent(getApplicationContext(), CompanyConfirm.class));		
				}
				if(position ==1){
		               startActivity(new Intent(getApplicationContext(), Companyrefirm.class));		
				}
				if(position ==2){
		               startActivity(new Intent(getApplicationContext(), CompanyTeam.class));		
				}
				if(position ==3){
		               startActivity(new Intent(getApplicationContext(), ServicePromise.class));		
				}
				if(position ==4){
		               startActivity(new Intent(getApplicationContext(), ClientInfo.class));		
				}
				if(position ==5){
		               startActivity(new Intent(getApplicationContext(), ProductIntroduce.class));		
				}
				if(position ==6){
		               startActivity(new Intent(getApplicationContext(), ContectUs.class));		
				}
				if(position ==7){
		               startActivity(new Intent(getApplicationContext(), ContectUs.class));		
				}
				if(position ==8){
		               startActivity(new Intent(getApplicationContext(), ContectUs.class));		
				}
				
			}
		});
		//popwindow的宽度和按钮的宽度相同都是230
		layout.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (mPop != null && mPop.isShowing()) {
						mPop.dismiss();
						mPop = null;
						return true;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
		layout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if (mPop != null && mPop.isShowing()) {
					 mPop.dismiss();
				  mPop = null; }
				 return true;
			}
		});
		
		
	}
	


}
