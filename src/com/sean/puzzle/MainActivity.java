package com.sean.puzzle;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button btn1 = null;
	private Button btn2 = null;
	private Button btn3 = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn1 = (Button)findViewById(R.id.btn_begin);
		btn2 = (Button)findViewById(R.id.btn_normal);
		btn3 = (Button)findViewById(R.id.btn_hard);
		btn1.setOnClickListener(new ButtonListener());
		btn2.setOnClickListener(new ButtonListener());
		btn3.setOnClickListener(new ButtonListener());
	}

	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			int level = 0;
			if(v.getId() == btn1.getId())
				level = 1;
			else if(v.getId() == btn2.getId())
				level = 2;
			else
				level = 3;
			Intent intent = new Intent();
			intent.putExtra("level", level);
			intent.setClass(MainActivity.this, SourceActivity.class);
			startActivity(intent);
		}
		
	}

}
