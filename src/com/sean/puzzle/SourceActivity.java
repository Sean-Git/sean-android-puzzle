package com.sean.puzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SourceActivity extends Activity {

	private ListView lst = null;
	private int level = 1;//Ĭ���Ѷ�Ϊ1��
	private String filePath = null;//ͼƬ·��
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_source);
		lst = (ListView)findViewById(R.id.lst_choice);
		lst.setOnItemClickListener(new ListViewListener());
		Intent intent = getIntent();
		level = intent.getIntExtra("level", 1);
	}
	
	class ListViewListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// arg2 and arg3 can both be used for judgement, here we use arg3 (row id of item clicked)
			if (arg3 == 0) {//call camera
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//��ȡSD����Ӧ�Ĵ洢Ŀ¼
	            File sdCardDir = Environment.getExternalStorageDirectory();
	            //������ʱ����Ϊ��Ƭ�ļ���������
	            Date date = new Date();
	            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");//��ȡ��ǰʱ�䣬��һ��ת��Ϊ�ַ���
	            String path = null;
				try {
					path = sdCardDir.getCanonicalPath() + "/DCIM/Camera/";
				} catch (IOException e) {
					e.printStackTrace();
				}
	            String fileName = "IMG_" + format.format(date) + ".jpg";       
				Uri imageUri = Uri.fromFile(new File(path, fileName));  
				filePath = path + fileName; 				
				//ָ����Ƭ����·����SD����
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, 0);
			} else if (arg3 == 1) {//call photo library
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, 1);
			}		
		}
		
	}
	
    @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
            
        if (requestCode == 0 && resultCode == RESULT_OK) {  
			//pass
        }  
        else if(requestCode == 1 && resultCode == RESULT_OK){ 
        	//The 3 lines below is useful!
            Cursor cursor = this.getContentResolver().query(data.getData(), null, null, null, null);  
            cursor.moveToFirst();  
            filePath = cursor.getString(cursor.getColumnIndex("_data"));         
        }
        
        Intent intent = new Intent();
        intent.setClass(SourceActivity.this, GameActivity.class);
        intent.putExtra("imgPath", filePath);//����õ�ͼƬ·�����ݵ�GameActivity����
        intent.putExtra("level", level);
        startActivity(intent);	  
        finish();
    }  
}