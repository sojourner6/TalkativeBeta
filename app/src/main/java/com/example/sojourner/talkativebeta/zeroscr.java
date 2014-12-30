package com.example.sojourner.talkativebeta;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class zeroscr extends ActionBarActivity
{
    private static int SERVICE_PORT = 11111;
    private static int LISTEN_PORT = 11112;
    private static String SELFIP = "192.168.0.29";
    private final String HOSTNAME = "<Bob>";
    private static TextView chatRoom;
    private static Socket mainSocket;
    private static String sendMessage = null;
    private static ArrayList<String> participants;
    private static EditText sendText;
    private String chatHistory = new String("");
    private final static int MSG_RCVD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zeroscr);
        chatRoom = (TextView)findViewById(R.id.tChatroom);
        sendText = (EditText)findViewById(R.id.etSend);
        participants = new ArrayList<String>();
        participants.add(SELFIP);
        final Handler mh1 = new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case MSG_RCVD:
                    {
                        System.out.println("MainActivity.onCreate> Socket Connected********************");
                        //chatHistory = chatHistory + (String)msg.obj.toString() + "\n";
                        //chatRoom.setText(chatHistory);
                        chatRoom.append((String)msg.obj.toString() + "\n");
                        sendText.setText("");
                        break;
                    }
                }
            }
        };
        setupServer(mh1);
    }



    public void buttonSendMessage(View v)
    {
        System.out.println("-------- Button Clicked --------");
    }



    public void setupServer(Handler handler)
    {
        final Handler h = handler;
        Thread t = new Thread()
        {
            public void run()
            {
                try
                {
                    ServerSocket ss = new ServerSocket(SERVICE_PORT);
                    System.out.println("MainServer>Listening....");
                    while(true)
                    {
                        Socket s = ss.accept();
                        //Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();
                        InputStreamReader r = new InputStreamReader(s.getInputStream());
                        BufferedReader buffer = new BufferedReader(r);
                        String IPv4 = s.getInetAddress().toString().substring(1);
                        String dataIn = buffer.readLine().toString();

                        //))))))))))))))))))))
                        Message msg = Message.obtain();
                        msg.what = zeroscr.MSG_RCVD;
                        msg.obj = "<Alice>"+" "+dataIn;
                        h.sendMessage(msg);
                        //))))))))))))))))))))

                        //chatHistory = chatHistory + dataIn;
                        //chatRoom.setText(dataIn);
                        System.out.println("+++++++MainActivity.setupServer: "+IPv4);
                        participants.add(IPv4);
                        System.out.println("ClientServer>"+dataIn+","+IPv4);
                        //ss.close();
                        s.close();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }



    public void sendToUser(View v)
    {
        sendMessage = sendMessage + "Connected";

        //)))))))))))))))))))))))))))))))))))))))))))))))))))))
        final Handler mh2 = new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case MSG_RCVD:
                    {
                        System.out.println("MainActivity.sendToUser> Socket Connected************");
                        //chatHistory = chatHistory + (String)msg.obj.toString() + "\n";
                        //chatRoom.setText(chatHistory);
                        chatRoom.append((String)msg.obj.toString() + "\n");
                        sendText.setText("");
                        break;
                    }
                }
            }
        };

        //))))))))))))))))))))))))))))))))))))))))))))))))))))))))

        Thread t = new Thread()
        {
            public void run()
            {
                try
                {
                    String ip = "192.168.0.28";
                    //for (String ip: participants)
                    //{
                    System.out.println("+++++++MainActivity.sendToUser>"+ip);
                    Socket s = new Socket("192.168.0.28", 11111);
                    //Socket s1 = new Socket(SERVER_IP, SERVICE_PORT);
                    PrintWriter w = new PrintWriter(s.getOutputStream());
                    String sentData = sendText.getText().toString();

                    //))))))))))))))))))))
                    Message msg = Message.obtain();
                    msg.what = zeroscr.MSG_RCVD;
                    msg.obj = HOSTNAME+" "+sentData;
                    mh2.sendMessage(msg);
                    //))))))))))))))))))))

                    //chatHistory = chatHistory + sentData;
                    //chatRoom.setText(chatHistory);
                    //sendText.setText("");
                    //chatRoom.append(sentData);
                    System.out.println("+++++++Sent");

                    //online.append(ip+" is Online");

                    w.println(sentData);
                    w.flush();
                    s.close();
                    return;
                    //}
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        };
        t.start();


//    	try
//    	{
//    		//Socket sock = new Socket("192.168.0.16", 11111);
//    		//InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
//	    	//BufferedReader buffer;
//	    	PrintWriter writer = new PrintWriter(mainSocket.getOutputStream());
//	        writer.println(/*username +*/ "Somnath :has connected:Connected"); // Displays to everyone that user connected.
//	        writer.flush();
//	        writer.flush(); // flushes the buffer
//	        //sock.close();
//	        //isConnected = true; // Used to see if the client is connected.
//    	}catch (Exception ex) {System.out.println("Error encountered"); ex.printStackTrace();}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zeroscr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
