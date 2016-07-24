package com.example.manasttt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectDevice extends Activity {
	private ListView pairedDeviceList;
	ArrayAdapter<String> btArrayAdapter;
	BluetoothAdapter bluetoothAdapter;
	Set<BluetoothDevice> pairedDevices;
	static final int BLUETOOTH_ENABLE = 2;
	private final static UUID uuid = UUID.fromString("10ee7022-8f5d-4d38-93cf-fa5cce3fd479");
	ListeningThread lT;
	ConnectingThread cT;
	ConnectedThread manager;
	EditText textBox;
	Button send;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_devices);

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		pairedDeviceList = (ListView) findViewById(R.id.lv);
		send=(Button) findViewById(R.id.button1);
		textBox=(EditText)findViewById(R.id.editText1);
	//	textBox.clearFocus();
		if (!bluetoothAdapter.isEnabled()) {
			Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetoothIntent, BLUETOOTH_ENABLE);
		}
		else{
			onActivityResult(BLUETOOTH_ENABLE, Activity.RESULT_OK, null);
		}
		
	
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == BLUETOOTH_ENABLE) {
			if (resultCode == Activity.RESULT_OK) {
				
				
				btArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
				pairedDevices = bluetoothAdapter.getBondedDevices();
				if (pairedDevices.size() > 0) {
					for (BluetoothDevice device : pairedDevices) {
						String deviceBTName = device.getName();
						btArrayAdapter.add("Name: " + deviceBTName + "\n" + "Hardware address: " + device.getAddress());
					}
				}
				pairedDeviceList.setAdapter(btArrayAdapter);
				pairedDeviceList.setOnItemClickListener(mDeviceClickListener);
				
				lT=new ListeningThread();
				lT.start();

			} else {
				Toast.makeText(getApplicationContext(), "You pressed no..", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}
	public void sendClicked(View v){
		
			String message=textBox.getText().toString();
			System.out.println("YOU ARE SENDING ****************************************************************************0"+message);
			byte arr[]=message.getBytes();
			
			manager.write(arr);
	}
	
	//create server thread
	class ListeningThread extends Thread {
	    private final BluetoothServerSocket bluetoothServerSocket;

	    public ListeningThread() {
	        BluetoothServerSocket temp = null;
	        try {
	            temp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(getString(R.string.app_name), uuid);

	        } catch (IOException e) {
	                e.printStackTrace();
	        }
	            bluetoothServerSocket = temp;
	    }

	    public void run() {
	        final BluetoothSocket bluetoothSocket;
	        BluetoothSocket tmp;
	        // This will block while listening until a BluetoothSocket is returned
	        // or an exception occurs
	        while (true) {
	            try {
	                tmp = bluetoothServerSocket.accept();
	            } catch (IOException e) {
	                break;
	            }
	            // If a connection is accepted
	            if (tmp != null) {
	            	bluetoothSocket=tmp;
	                runOnUiThread(new Runnable() {
	                    public void run() {
	                        Toast.makeText(getApplicationContext(), "A connection has been accepted.",
	                                Toast.LENGTH_SHORT).show();
	                     
	                    }
	                });
	               
	                // Manage the connection in a separate thread
	                if(bluetoothSocket!=null)
	                	manager=new ConnectedThread(bluetoothSocket);
	                manager.start();
	                try {
	                    bluetoothServerSocket.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	                break;
	            }
	        }
	    }

	    // Cancel the listening socket and terminate the thread
	    public void cancel() {
	        try {
	            bluetoothServerSocket.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}//class ends
	//create  client thread
	private class ConnectingThread extends Thread {
	    private final BluetoothSocket bluetoothSocket;
	    private final BluetoothDevice bluetoothDevice;

	    public ConnectingThread(BluetoothDevice device) {

	        BluetoothSocket temp = null;
	        bluetoothDevice = device;

	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            temp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        bluetoothSocket = temp;
	    }

	    public void run() {
	        // Cancel any discovery as it will slow down the connection
	        bluetoothAdapter.cancelDiscovery();

	        try {
	            // This will block until it succeeds in connecting to the device
	            // through the bluetoothSocket or throws an exception
	            bluetoothSocket.connect();
	        } catch (IOException connectException) {
	            connectException.printStackTrace();
	            try {
	                bluetoothSocket.close();
	            } catch (IOException closeException) {
	                closeException.printStackTrace();
	            }
	        }

	        // Code to manage the connection in a separate thread
	        /*
	            manageBluetoothConnection(bluetoothSocket);
	        */
	        manager=new ConnectedThread(bluetoothSocket);
            manager.start();
	    }

	    // Cancel an open connection and terminate the thread
	    public void cancel() {
	        try {
	            bluetoothSocket.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(lT!=null)
			lT.cancel();
		if(cT!=null)
			cT.cancel();
		
	}
	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

			// Get the device MAC address, which is the last 17 chars in the
			// View
			String info = ((TextView) v).getText().toString();
			String mac = info.substring(info.length() - 17);
			BluetoothDevice bluetoothDevice=bluetoothAdapter.getRemoteDevice(mac);
			cT = new ConnectingThread(bluetoothDevice);
			cT.start();
		
			
			
			//Toast.makeText(getApplicationContext(), "You clicked"+info, Toast.LENGTH_SHORT).show();
			//finish();

		}
	};
	private class ConnectedThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;
	 
	    public ConnectedThread(BluetoothSocket socket) {
	        mmSocket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void run() {
	        byte[] buffer = new byte[1024];  // buffer store for the stream
	        int bytes; // bytes returned from read()
	 
	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	                // Read from the InputStream
	                bytes = mmInStream.read(buffer);
	                // Send the obtained bytes to the UI activity
	                final String s=new String(buffer,"US-ASCII");
	                runOnUiThread(new Runnable() {
	                    @Override
	                    public void run() {

	               //stuff that updates ui
	                    	textBox.setText(s);

	                   }
	               });
	              // mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
	               //         .sendToTarget();
	            } catch (IOException e) {
	                break;
	            }
	        }
	    }
	 
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } catch (IOException e) { }
	    }
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
}
