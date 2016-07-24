package com.example.manasttt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Main_menu extends Activity {

	Button local_game, bt_game, exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_activity);
		local_game = (Button) findViewById(R.id.LB);
		bt_game = (Button) findViewById(R.id.BB);
		exit = (Button) findViewById(R.id.EB);

		local_game.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(Main_menu.this, MainActivity.class));

			}
		});
		bt_game.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				

				startActivity(new Intent(Main_menu.this, SelectDevice.class));
			}
		});
		exit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});
	}

}
