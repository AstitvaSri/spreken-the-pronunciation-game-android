//SPREKEN - THE PRONUNCIATION GAME - DEVELOPED BY - ASTITVA SRIVASTAVA - asti1997.sri@gmail.com

package com.sivastava.astitva.spreken;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

import static android.content.ContentValues.TAG;


//SpeechRecognition



public class Spreken extends AppCompatActivity implements View.OnClickListener ,TextToSpeech.OnInitListener{
    public TextView wordpron;
    private InputStream in;
    private BufferedReader reader;
    String line;
    String currentword;
    public String texttobeset;
    public GifImageView micpos;
    public GifImageView nxtwrd;
    public TextView cscore,hscore,hint;
    public SpeechRecognizer mSpeechRecognizer;
    private Intent srintent;
    String result;
    private int difficulty;
    public GifImageView restrt, headphn, middleview;
    private TextView cntdwn,shiftnext;
    private CountDownTimer clock;
    private boolean newhighscore=false;
    public String hiscorestring, difficultystring;
    private TextToSpeech tts;
    public TextView difftitle;
    private LinearLayout buttonsicons,mothercontainerlayout;
    public int min_diff_length, max_diff_length;
    public String file_containing_words;
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_spreken );



        checkPermission();


        micpos = (GifImageView) findViewById( R.id.animic );
        cntdwn = (TextView) findViewById( R.id.countdown );
        shiftnext = (TextView) findViewById( R.id.supportnext );
        restrt = (GifImageView) findViewById( R.id.restartgame );
        headphn = (GifImageView) findViewById( R.id.listenit );
        nxtwrd = (GifImageView) findViewById( R.id.nxtbtn );
        cscore = (TextView) findViewById( R.id.currentscore );
        hscore = (TextView) findViewById( R.id.highscore );
        wordpron = (TextView) findViewById( R.id.pronounce );
        hint = (TextView) findViewById( R.id.hinttxt );
        difftitle=(TextView)findViewById( R.id.difftxt );
        middleview=(GifImageView)findViewById( R.id.animic ) ;
        buttonsicons=(LinearLayout)findViewById( R.id.controllers );
        mothercontainerlayout=(LinearLayout)findViewById( R.id.mothercontainer ) ;
        //SESSION VARIABLES
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {

            hiscorestring = extras.getString( "EXTRA_SESSION_ID" );
            difficultystring=extras.getString( "EXTRA_SESSION_DIFF" );
        }

        hscore.setText( hiscorestring );
        hint.setVisibility( View.VISIBLE );
        cntdwn.setVisibility( View.GONE );
        restrt.setVisibility( View.GONE );
        hint.setText( "TOUCH AND HOLD THE MIC TO SPEAK" );


        //Assigning Difficulty Title**************************************************************


        String diffstr;
        difficulty=Integer.parseInt( difficultystring.trim() );

        switch(difficulty)
        {
            case 1:
                diffstr="KID";
                break;
            case 3:
                diffstr="LEGEND";
                break;
            default:
                diffstr="MAN";

        }

        difftitle.setText( "DIFFICULTY\n("+diffstr+")" );
        //***********************************************************************************



        //TTS*********************************
        tts = new TextToSpeech(this, this);

        //*************************************

        //ONCLICK LISTENER SEUP

        restrt.setOnClickListener( this );
        headphn.setOnClickListener( this );

        nxtwrd.setOnClickListener( this );




        //
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer( this );
        srintent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
        srintent.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM );
        srintent.putExtra( RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault() );

        //SpeechRecognizerCode


        mSpeechRecognizer.setRecognitionListener( new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

                mSpeechRecognizer.cancel();

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                mSpeechRecognizer.cancel();

            }

            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList( SpeechRecognizer.RESULTS_RECOGNITION );
                if(bundle.isEmpty())
                {

                    hint.setText( "YOU DIDN'T SPEAK! TRY AGAIN!" );
                    return;
                }

                //displaying the first match                {
                if (matches != null) {
                    result = matches.get( 0 ).toString().trim();
                    result.replaceAll( "\\s+", "" );
                    String cmp = wordpron.getText().toString().toLowerCase();
                    result=result.toLowerCase();
                    bundle.clear();
                    matches=null;



                    //Difficulty is KID************************
                    if(difficulty==1) {
                        if (result.equals( cmp ) || result.contains( cmp ) || cmp.contains( result ) || result.startsWith( cmp )||
                                result.endsWith( cmp )||cmp.startsWith( result )||cmp.endsWith( result )||
                                (cmp.subSequence( 0,cmp.length()-1 )==result) || (result.subSequence( 0, result.length()-1 )==cmp)
                                )
                        {

                            clock.cancel();
                            hint.setText( "CORRECT PRONUNCIATION!" );
                            micpos.setImageResource( R.drawable.checkmark );
                            micpos.setOnTouchListener( null );
                            cntdwn.setVisibility( View.GONE );
                            shiftnext.setVisibility( View.VISIBLE );
                            headphn.setVisibility( View.VISIBLE );
                            nxtwrd.setVisibility( View.VISIBLE );
                            updateScore();
                            System.gc();
                            mSpeechRecognizer.cancel();



                        }

                        else {
                            hint.setText( "TRY AGAIN!" );
                        }
                    }


                    //Difficulty is MAN************************
                    if(difficulty==2) {
                        if (result.equalsIgnoreCase( cmp ) || result.contains( cmp ) || cmp.contains( result )) {

                            clock.cancel();
                            hint.setText( "CORRECT PRONUNCIATION!" );
                            micpos.setImageResource( R.drawable.checkmark );
                            micpos.setOnTouchListener( null );
                            cntdwn.setVisibility( View.GONE );
                            shiftnext.setVisibility( View.VISIBLE );
                            headphn.setVisibility( View.VISIBLE );
                            nxtwrd.setVisibility( View.VISIBLE );
                            updateScore();
                            System.gc();
                            mSpeechRecognizer.cancel();

                        }

                        else {
                            hint.setText( "TRY AGAIN!" );
                        }
                    }


                    //Difficulty is LEGEND************************
                    if(difficulty==3) {
                        if (result.equalsIgnoreCase( cmp ) || result.contains( cmp ) || cmp.contains( result )) {

                            clock.cancel();
                            hint.setText( "CORRECT PRONUNCIATION!" );
                            micpos.setImageResource( R.drawable.checkmark );
                            micpos.setOnTouchListener( null );
                            cntdwn.setVisibility( View.GONE );
                            shiftnext.setVisibility( View.VISIBLE );
                            headphn.setVisibility( View.VISIBLE );
                            nxtwrd.setVisibility( View.VISIBLE );
                            updateScore();
                            System.gc();
                            mSpeechRecognizer.cancel();


                        }
                        else {
                            hint.setText( "TRY AGAIN!" );
                        }
                    }
                    //***************************************

                }

            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        } );


        //TouchListener

        try {
            nextWord();
        } catch (IOException e) {
            Toast.makeText( getApplicationContext(),"An error occurred", Toast.LENGTH_LONG ).show();
        }


    }

    //ONCLICK LISTENER*************************************************************************************
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.listenit:
                speakOut();//TTS METHOD*********************************************
                break;

            case R.id.nxtbtn:
                resetgif();
                break;

            case R.id.restartgame:
                mSpeechRecognizer.cancel();
                mSpeechRecognizer.destroy();
                mothercontainerlayout.invalidate();
                finish();
                break;

        }

    }
    /******************************************************************************************************/

    //TTS*********************************************


    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText( this,"Language Not Supported",Toast.LENGTH_SHORT ).show();
            } else {
                headphn.setEnabled(true);
            }

        } else {
            Toast.makeText( this,"Initialization Failed",Toast.LENGTH_SHORT ).show();
        }

    }


    private void speakOut() {

        String text = wordpron.getText().toString().trim();

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    //*************************************************
    public void makemictouchable()

    {

        micpos.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionevent){


                if (motionevent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    micpos.setImageResource( R.drawable.speaknow );
                    hint.setText( "LISTENING!" );
                    mSpeechRecognizer.startListening(srintent);

                } else if (motionevent.getAction() == android.view.MotionEvent.ACTION_UP) {
                    mSpeechRecognizer.stopListening();
                    micpos.setImageResource( R.drawable.micicon );
                    hint.setText( "" );
                }
                return true;
            }


        } );
    }








    public void nextWord() throws IOException {


        middleview.invalidate();
        buttonsicons.invalidate();
        makemictouchable();
        headphn.setVisibility( View.GONE );
        nxtwrd.setVisibility( View.GONE );
        shiftnext.setVisibility( View.GONE );
        hint.setVisibility( View.VISIBLE );
        hint.setText( "TOUCH AND HOLD THE MIC TO SPEAK" );


 clock = new CountDownTimer( 17000, 1000) {
            int num=16;
            int col1 = Color.parseColor("#7ff974");
            int col2 = Color.parseColor("#f4a817");
            int col3 = Color.parseColor("#f44430");


            public void onTick(long millisUntilFinished) {
                if(num==16)
                {
                    cntdwn.setText( "" );

                }
                else if(num>5)
                {
                    cntdwn.setTextColor( col1 );

                    cntdwn.setText( String.valueOf( num ) );
                }
                else if(num>0)
                {
                    cntdwn.setTextColor( col2 );

                    cntdwn.setText( String.valueOf( num ) );
                }
                else
                {
                    cntdwn.setTextColor( col3 );

                    cntdwn.setText( String.valueOf( num ) );
                }
                num=num-1;
            }


            public void onFinish() {
                mSpeechRecognizer.cancel();
                mSpeechRecognizer.destroy();
                cntdwn.setVisibility( View.GONE );
                micpos.setImageResource( R.drawable.lost );
                micpos.setOnTouchListener( null );
                hint.setText( "YOU RAN OUT OF TIME!" );
                shiftnext.setVisibility( View.VISIBLE );
                headphn.setVisibility( View.VISIBLE );
                restrt.setVisibility( View.VISIBLE );
                if(newhighscore)
                {

                    String filename = "highscore"+difficultystring.trim()+".txt";
                    String fileContents = hscore.getText().toString();
                    FileOutputStream outputStream;

                    try {
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(fileContents.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        Toast.makeText( getApplicationContext(),"AN ERROR OCCURRED", Toast.LENGTH_LONG ).show();
                    }
                    newhighscore=false;
                    congrats();
                }

            }
        }.start();
        cntdwn.setVisibility( View.VISIBLE );
        TextView t = (TextView) findViewById( R.id.pronounce );
        t.setTextSize( 30 );


        //**************DIFFICULTY FILTER*****************************************************

        if(difficulty==1)//KID
        {
            min_diff_length=3;
            max_diff_length=5;
            file_containing_words = "wordslist.txt";
        }
        else if(difficulty==3)//LEGEND
        {
            min_diff_length=8;
            max_diff_length=20;
            file_containing_words="legend_words.txt";
        }
        else //difficulty = MAN
        {
            min_diff_length=5;
            max_diff_length=14;
            file_containing_words = "wordslist.txt";
        }
        //***********************************************************************************
        //readingwordslist*******************************************************************
        try {
            in = this.getAssets().open( file_containing_words );
        } catch (IOException e) {
            e.printStackTrace();
        }
        reader = new BufferedReader( new InputStreamReader( in ) );
        int num1, num2, num3;

    try {
        do {
            Random rand = new Random();
            num1 = rand.nextInt( 24 ) + 1;
            num2 = rand.nextInt( 39 ) + 1;
            num3 = num1 * num2;
            for (int i = 1; i <= num3; i++) {
                line = reader.readLine();
            }
        }
        while ((line.trim().length() < min_diff_length) || (line.trim().length() > max_diff_length));
    }
    catch (Exception e)
    {
        Toast.makeText( this,"Insufficient memory in your device's speech engine. Restart the App!",Toast.LENGTH_LONG ).show();
    }
        currentword = line.trim().toUpperCase();
        t = (TextView) findViewById( R.id.pronounce );
        t.setText( currentword );

    }
    public void resetgif()
    {
        micpos.setImageResource( R.drawable.micicon );
        try {
            nextWord();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void checkPermission()
    {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if(!(ContextCompat.checkSelfPermission( this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED))
            {

                Intent i=new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName() ) );
                startActivity( i );
                finish();
            }
        }
    }

    public void updateScore()
    {
        int yourscore=Integer.parseInt( cscore.getText().toString().trim());
        int highscore=Integer.parseInt( hscore.getText().toString().trim());
        yourscore=yourscore+1;
        if(yourscore>highscore)
        {
            hscore.setText( String.valueOf(yourscore) );

            //SAVING HIGH SCORES IN CASE OF SYSTEM CRASH*************
            String filename = "highscore"+difficultystring.trim()+".txt";
            String fileContents = hscore.getText().toString();
            FileOutputStream outputStream;

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(fileContents.getBytes());
                outputStream.close();
            } catch (Exception e) {
                Toast.makeText( getApplicationContext(),"AN ERROR OCCURRED", Toast.LENGTH_LONG ).show();
            }
            //*******************************************************
            newhighscore=true;
        }
        cscore.setText( String.valueOf(yourscore) );
    }

    public void congrats()
    {
        String diffstr;
        switch(difficulty)
        {
            case 1:
                diffstr="KID";
                break;
            case 3:
                diffstr="LEGEND";
                break;
            default:
                diffstr="MAN";

        }
        final AlertDialog alertDialog = new AlertDialog.Builder(
                Spreken.this).create();

        // Setting Dialog Title
        alertDialog.setTitle(Html.fromHtml("<font color='#2a2c5a' size='24'>ALL HAIL THE KING!</font>"));

        // Setting Dialog Message
        alertDialog.setMessage( "YOUR NEW HIGH SCORE IS : "+hscore.getText().toString()+"\n(DIFFICULTY : "+diffstr+")");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.highscore);

        // Setting OK Button
        alertDialog.setButton("CONTINUE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
            }
        });

        // Showing Alert Message with Animation
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_SLideupdown;
        alertDialog.show();
    }





    //*************BACK BUTTON*************

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        mSpeechRecognizer.cancel();
        mSpeechRecognizer.destroy();
        mothercontainerlayout.invalidate();
        super.onBackPressed();
    }

    //*************************************




}


