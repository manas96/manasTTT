package com.example.manasttt;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	static Button g[][] = new Button[3][3];//array to store 3x3 grid buttons
	static Button rb;// RESET BUTTON
	static char values[][] = new char[][] { { 'a', 'b', 'c' },{ 'd', 'e', 'f' }, { 'g', 'h', 'i' } }; //array to store X or O.Used to check for win
	static boolean isXTurn = true;//used to alternate turns
	static boolean	win = false;// true if there is a winner
	static char winner = 'n';// winning player name
	int i, j;
	static TextView tv, tvb;
	static int turnno=0;

	static void set(int i, int j, char c) //called on any button press
	{ 
		
		g[i][j].setText("" + c);//update button text
		tv.setText("" + i + ", " + j + ", " + c + " " + isXTurn);// debug
		isXTurn = !isXTurn; //alternate between turns
		g[i][j].setEnabled(false);
		turnno++;
		
		values[i][j] = c;//store characters in array
		if (isXTurn)
			tvb.setText("X player's turn");
		else
			tvb.setText("O player's turn");
		
	

		for (int k = 0; k < 3; k++) //used to check for win
		{

			win = winVerticalLine(1, k);// check 3 vertical columns
			if (win)
			{ 
				winner = values[1][k];
				System.out.println("win condition set true by winvertical method");//debugging
				break;
			} 
			else
				win = winHorizontalLine(k, 1);//check 3 horizontal rows
			if (win)
			{ 
				winner = values[k][1];
				System.out.println("win condition set true by winhorizontal method");//debugging
				break;
			}

		}
		if ((values[1][1] == values[0][2] && values[1][1] == values[2][0])// check two diagonals
																			
				|| (values[1][1] == values[0][0] && values[1][1] == values[2][2])) {
			win = true;
			winner = values[1][1];
			System.out.println("win condition set true by winvertical method");// Debug
		}
		if (win)
		{
			
			for(int a=0;a<3;a++)
			{
				
				for(int b=0;b<3;b++)
				{
					
					if(values[a][b]=='x'||values[a][b]=='o')
						continue;
					g[a][b].setEnabled(false);
					System.out.println("In inner for loop. Value of counter b is  "+b);
				}
			} //code for removing bug*/
			
			
			tvb.setText("The winner is  " + winner + "!");
			
		} 
		else if(turnno==9&&!win)
		{
			
			tvb.setText("Draw!");
		}
	}

	static boolean winVerticalLine(int i, int j) 
	{ 
		if ((values[i][j] == values[i - 1][j])
				&& (values[i][j] == values[i + 1][j]))
			return true;
		else
			return false;
	}

	static boolean winHorizontalLine(int i, int j) 
	{
		if ((values[i][j] == values[i][j - 1])
				&& (values[i][j] == values[i][j + 1]))
			return true;
		else
			return false;
	}

	void resetButtons(){
		for (int h = 0; h < 3; h++)//clear all buttons
		{
			for (int i = 0; i < 3; i++) {
				g[h][i].setEnabled(true);
				g[h][i].setText(" ");
			}

		}
		int y = 97;
		turnno=0;
		for (int h = 0; h < 3; h++) {// reset internal array
			for (int i = 0; i < 3; i++) {
				char x = (char) y;
				values[h][i] = x;
				y++;
			}

		}
		isXTurn = true;
		winner = 'n';
		win = false;
		tv.setText("Debug text, please ignore ");
		tvb.setText("Start match!");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		g[0][0] = (Button) findViewById(R.id.R0C0);
		g[0][1] = (Button) findViewById(R.id.R0C1);
		g[0][2] = (Button) findViewById(R.id.R0C2);
		g[1][0] = (Button) findViewById(R.id.R1C0);
		g[1][1] = (Button) findViewById(R.id.R1C1);
		g[1][2] = (Button) findViewById(R.id.R1C2);
		g[2][0] = (Button) findViewById(R.id.R2C0);
		g[2][1] = (Button) findViewById(R.id.R2C1);
		g[2][2] = (Button) findViewById(R.id.R2C2);
		rb = (Button) findViewById(R.id.RB);
		tv = (TextView) findViewById(R.id.TV);
		tvb = (TextView) findViewById(R.id.TVB);

	/*	System.out.println("Printing array from outside set method");
		for (int h = 0; h < 3; h++) {
			for (int g = 0; g < 3; g++) {
				System.out.print(values[h][g]);
			}
			System.out.println();
		}
		*/
		rb.setOnClickListener(new View.OnClickListener() {// reset button

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetButtons();
			}
		});
		
		// all 3x3 buttons:
		g[0][0].setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isXTurn)
					set(0, 0, 'X');
				else
					set(0, 0, 'O');

			}
		});

		g[0][1].setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isXTurn)
					set(0, 1, 'X');
				else
					set(0, 1, 'O');

			}
		});
		g[0][2].setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isXTurn)
					set(0, 2, 'X');
				else
					set(0, 2, 'O');

			}
		});
		g[1][0].setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isXTurn)
					set(1, 0, 'X');
				else
					set(1, 0, 'O');

			}
		});
		g[1][1].setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isXTurn)
					set(1, 1, 'X');
				else
					set(1, 1, 'O');

			}
		});
		g[1][2].setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isXTurn)
					set(1, 2, 'X');
				else
					set(1, 2, 'O');

			}
		});
		g[2][0].setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isXTurn)
					set(2, 0, 'X');
				else
					set(2, 0, 'O');

			}
		});
		g[2][1].setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isXTurn)
					set(2, 1, 'X');
				else
					set(2, 1, 'O');

			}
		});
		g[2][2].setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isXTurn)
					set(2, 2, 'X');
				else
					set(2, 2, 'O');

			}
		});

	}

}


