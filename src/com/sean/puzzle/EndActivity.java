package com.sean.puzzle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EndActivity extends Activity{

	private Button btn_end = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end);
		btn_end = (Button)findViewById(R.id.btn_end);
		btn_end.setOnClickListener(new ButtonListener());
	}
	
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			finish();
		}
		
	}
	
}