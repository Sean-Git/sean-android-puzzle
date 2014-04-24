package com.sean.puzzle;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends Activity{

	private int level = 1;
	private int row = 0;
	private int col = 0;
	private int width = 0;//ImageView�Ŀ�
	private int height = 0;//ImageView�ĸ�
	private int step = 0;
	private int mis_count = 0;
	private int chosen_num = -1;//trick��¼��һ��ѡ�еĿ���������һ����������������-1
	private int [] num_arr = null; //������ʾλ�ö�Ӧ��new_bitmap�е�λ��
	private Bitmap [] pic_arr = null; //������ʾλ�ö�Ӧ��new_bitmap�е�ͼ��
	private Bitmap src_bitmap = null;//ԭʼλͼ
	private Bitmap new_bitmap = null;//centerCrop���λͼ
	
	private ImageView img = null;
	private Button btn_start = null;
	private TextView txt_count = null;
	private TextView txt_level = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		Intent intent = getIntent();
		//�������ڱ��ص�ͼƬȡ������С����ʾ�ڽ�����  
        Bitmap bitmap = BitmapFactory.decodeFile(intent.getStringExtra("imgPath"));  
        src_bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/5, bitmap.getHeight()/5, false);  
        //����Bitmap�ڴ�ռ�ýϴ�������Ҫ�����ڴ棬����ᱨout of memory�쳣  
        bitmap.recycle();  		

		img = (ImageView) findViewById(R.id.img_game);		
        img.setImageBitmap(src_bitmap);// ��ͼƬ��ʾ��ImageView�� 	
       
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_start.setOnClickListener(new ButtonListener());
		
		txt_count = (TextView) findViewById(R.id.txt_count);
		txt_level = (TextView) findViewById(R.id.txt_level);
        level = intent.getIntExtra("level", 1);
        switch(level){
        	case 1: txt_level.setText("����"); break;
        	case 2: txt_level.setText("�м�"); break;
        	case 3: txt_level.setText("�߼�"); break;
        }
	}
	
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			initSourceBitmap();
		}
		
	}
	
	private void initSourceBitmap(){
		switch(level){//init difficulty
			case 1: row = col = 3; break;
			case 2: row = col = 5; break;
			case 3: row = col = 7; break;
		}
		num_arr = randArray(row * col);
		mis_count = misCount(num_arr, row * col);
		pic_arr = new Bitmap[row * col];
		width = img.getWidth();
		height = img.getHeight();
		new_bitmap = centerCrop(src_bitmap, width, height);
		
		int tmp_height = new_bitmap.getHeight();
		int tmp_width = new_bitmap.getWidth();		
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				int pos = i * row + j;
				int src_row = num_arr[pos] / row;//Ӧ����λͼ�ڼ����С���ʼ�����λ��
				int src_col = num_arr[pos] % col;//Ӧ����λͼ�ڼ����С���ʼ�����λ��
				pic_arr[pos] = Bitmap.createBitmap(new_bitmap, src_col*(tmp_width/col), src_row*(tmp_height/row), tmp_width/col, tmp_height/row);
			}
		}
		
		Canvas to_draw = new Canvas(new_bitmap);
		for(int i = 0; i < row; i++)
			for(int j = 0; j < col; j++)
				to_draw.drawBitmap(pic_arr[i * row + j], j*(tmp_width/col), i*(tmp_height/row), null);
		to_draw.save(Canvas.ALL_SAVE_FLAG);
		to_draw.restore();
		img.setImageBitmap(new_bitmap);
		img.setOnTouchListener(new ImageListener());		
	}
	
	private int[] randArray(int len){
		int [] arr = new int[len];
		boolean [] status = new boolean[len];
		Random rand = new Random();
		int c = 0;
		while(c < len){
			int tmp = rand.nextInt(len);
			if(status[tmp] != true){
				arr[c] = tmp;
				status[tmp] = true;
				c++;
			}
		}			
		return arr;
	}
	
	private int misCount(int[] arr, int len){
		int tmp = 0;
		for(int i = 0; i < len; i++){
			if(arr[i] != i)
				tmp++;
		}
		return tmp;
	}
	
	private Bitmap centerCrop(Bitmap src, int W, int H){
		int w = src.getWidth();
		int h = src.getHeight();
		float ratio = (float)W/H;
		Bitmap dst = null;
		if(((float)w/h) > ratio){//crop width
			dst = Bitmap.createBitmap(src_bitmap, (int)((w-ratio*h)/2), 0, (int)(ratio*h), h);
		}else{//crop height
			dst = Bitmap.createBitmap(src_bitmap, 0, (int)((h-w/ratio)/2), w, (int)(w/ratio));
		}
		return dst;
	}
	
	class ImageListener implements OnTouchListener{
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			float x = arg1.getX();//get clicked x position
			float y = arg1.getY();//get clicked y position
			int tmp_col = (int) (x / (width/col));
			int tmp_row = (int) (y / (height/row));
			int new_chosen_num = tmp_row * col + tmp_col;
			if((chosen_num != -1) && (new_chosen_num != chosen_num)){//do swap
				swapBlock(chosen_num, new_chosen_num);
				txt_count.setText(step+"");
				chosen_num = -1;
				if(mis_count == 0){//Game Win!
					Intent intent = new Intent();
					intent.setClass(GameActivity.this, EndActivity.class);
					startActivity(intent);					
				}
			}else{//set one chosen
				chosen_num = new_chosen_num;
			}
			return false;
		}		
	}
	
	private void swapBlock(int a, int b){
		if(a == b) return;
		int x = 0;//��¼����ǰ��λ����ȷ��
		int y = 0;//��¼��������λ����ȷ��
		if(num_arr[a] == a)
			x++;
		if(num_arr[b] == b)
			x++;
		if(a == num_arr[b])
			y++;
		if(b == num_arr[a])
			y++;
		mis_count -= (y - x);
		step++;
		//swap values
		int i_tmp = num_arr[a];
		num_arr[a] = num_arr[b];
		num_arr[b] = i_tmp;
		Bitmap b_tmp = Bitmap.createBitmap(pic_arr[a]);
		pic_arr[a] = Bitmap.createBitmap(pic_arr[b]);
		pic_arr[b] = Bitmap.createBitmap(b_tmp);
		b_tmp.recycle();
		//draw effect
		Canvas to_draw = new Canvas(new_bitmap);
		to_draw.drawBitmap(pic_arr[a], (a%row)*(new_bitmap.getWidth()/col), (a/row)*(new_bitmap.getHeight()/row), null);
		to_draw.drawBitmap(pic_arr[b], (b%row)*(new_bitmap.getWidth()/col), (b/row)*(new_bitmap.getHeight()/row), null);
		to_draw.save(Canvas.ALL_SAVE_FLAG);
		to_draw.restore();
		img.setImageBitmap(new_bitmap);	
	}
}