//SPREKEN - THE PRONUNCIATION GAME - DEVELOPED BY - ASTITVA SRIVASTAVA - asti1997.sri@gmail.com



package com.sivastava.astitva.spreken;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v4.provider.FontsContractCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

import static java.lang.System.getenv;
import static java.lang.System.in;

public class HomeScreen extends AppCompatActivity {

    private GifImageView startbtn;
    public FrameLayout framedev, frameops, framediff, framehscore, frameguide;
    public RelativeLayout devgrey;
    public GifImageView devgif;
    public TextView gotittxt, hscorelist, guide001,guide002, guide003;
    public FrameLayout framefirstrun, framefloatinghand;
    public Button devbtn;
    private ScrollView guidescroller;
    public ImageView opsbut, devbut, txtbut;
    public LinearLayout maingame;

    private InputStream in;
    private BufferedReader reader;
    public String currentdiff;
    private SharedPreferences prefs = null;
    public boolean firsttime=false;
    public Button btnhs,btndiff, btnnothanks;

    public String GUIDE1="\nSPREKEN is a game which enhances your prounciation by challenging you to pronounce the english words in the most correct way.\n\nwe everyday encounter english words which we only think that we are pronouncing them correctly. but actually we can't differentiate between pronunciation of many words like 'THEIR & THERE', 'PHASE & FACE', 'NEW & KNEW' and so on.\n\nalso, there are some difficult words which are being pronounced differently by different people, like 'VOYAGE', 'ENDEAVOUR', 'APOCALYPSE' etc.\n\nirony is that most people don't know that its 'PRONUNCIATION', not 'PRONOUNCIATION'\n";
    public String GUIDE2="\n# start the game.\n\n# you'll see an english word, tap and hold the MICROPHONE icon, pronounce that word and then release the icon.\n\n# if you'll pronounce that word correctly, your score will increase by 1.\n\n# tap on the NEXT button to see the next word.\n\n# in case, you've pronounced the word incorrectly, a message will appear on the screen : 'TRY AGAIN!'.\n\n# there is also a TIMER of 15 seconds.\n\n# you'll have to pronounce the word correctly within 15 seconds, otherwise you'll loose.\n\n# you can tap on the HEADPHONE icon to hear the correct pronunciation of that particular word.\n";
    public String GUIDE3="\nthere are 3 difficulty modes : \n\n1. KID - easy words and less restrictive pronunciation filter. \n\n2. MAN - average length words and moderately restrictive pronunciation filter. \n\n3. LEGEND - difficult words and highly restrictive pronunciation filter.\n\neach difficulty mode has its own HIGH SCORE.\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_home_screen );

        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);//FIRST RUN LOGIC


        startbtn = (GifImageView) findViewById( R.id.startgame );
        opsbut = (ImageView) findViewById( R.id.settings_btn );
        devbut = (ImageView) findViewById( R.id.developer_btn );

        startbtn.setEnabled( true );


        guide001=(TextView)findViewById( R.id.whatisspreken );
        guide002=(TextView)findViewById( R.id.gameinstructions );
        guide003=(TextView)findViewById( R.id.difficultymodes );


        framedev = (FrameLayout) findViewById( R.id.devframe );
        frameops = (FrameLayout) findViewById( R.id.optionsframe );
        framediff = (FrameLayout) findViewById( R.id.difficultyframe );
        framehscore = (FrameLayout) findViewById( R.id.highscoreframe );
        frameguide=(FrameLayout)findViewById( R.id.howtoplayframe );

        devgrey = (RelativeLayout) findViewById( R.id.frameshadow );
        devgif = (GifImageView) findViewById( R.id.developer );
        gotittxt = (TextView) findViewById( R.id.devbyastitva );
        devbtn = (Button) findViewById( R.id.devgotit );
        guidescroller=(ScrollView)findViewById( R.id.guidescroll ) ;
        maingame = (LinearLayout) findViewById( R.id.mainworkinglayout );



        hscorelist = (TextView) findViewById( R.id.highscorelist );


        startbtn.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    startbtn.setImageResource( R.drawable.starttouch );
                } else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
                    startbtn.setImageResource( R.drawable.start );

                        StartGamePlease();

                    }
                return true;
            }
        } );


    }

    public void StartGamePlease() {
        Intent i = new Intent( this, Spreken.class );



        //reading difficulty file************************************************************

        try {
            FileInputStream fileIn = openFileInput( "difficulty_file.txt" );
            InputStreamReader InputRead = new InputStreamReader( fileIn );

            char[] inputBuffer = new char[100];
            String s = "";
            int charRead;


            while ((charRead = InputRead.read( inputBuffer )) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf( inputBuffer, 0, charRead );
                s += readstring;
            }
            InputRead.close();
            currentdiff = s.substring( 0 );


        } catch (Exception e) {
            currentdiff = "1";
            e.printStackTrace();
        }


        //reading high score file***************************************************
        String high_score;
        high_score = checkHighScoreFile( currentdiff );

        //**************************************************************************

        //***********************************************************************************


        //Passing high score & difficulty***********************************************
        i.putExtra( "EXTRA_SESSION_ID", high_score );
        i.putExtra( "EXTRA_SESSION_DIFF", currentdiff );
        startActivity(i);
        //*******************************************************************************

    }

    public String checkHighScoreFile(String whatisdifficulty) {
        String highdiff;
        String filenamehd = "highscore" + whatisdifficulty.trim() + ".txt";
        try {
            FileInputStream fileIn = openFileInput( filenamehd );
            InputStreamReader InputRead = new InputStreamReader( fileIn );

            char[] inputBuffer = new char[100];
            String s = "";
            int charRead;


            while ((charRead = InputRead.read( inputBuffer )) > 0) {
                // char to string conversion
                String readstring = String.copyValueOf( inputBuffer, 0, charRead );
                s += readstring;
            }
            InputRead.close();
            highdiff = s.substring( 0 );


        } catch (Exception e) {
            highdiff = "0";
            e.printStackTrace();
        }
        return highdiff;
    }

    public void showDeveloper(View view) {
        Animation animation = AnimationUtils.loadAnimation( this, R.anim.zoom_in );


        devgrey.setVisibility( View.VISIBLE );
        framedev.setVisibility( View.VISIBLE );
        framedev.startAnimation( animation );
        startbtn.setVisibility( View.GONE );
        devbut.setEnabled( false );
        opsbut.setEnabled( false );
        maingame.invalidate();

    }

    public void hideDeveloper(View view) {

        framedev.setVisibility( View.GONE );
        devgrey.setVisibility( View.GONE );
        startbtn.setVisibility( View.VISIBLE );
        framedev.invalidate();
        devbut.setEnabled( true );
        opsbut.setEnabled( true );

    }

    public void showOptions(View view) {



        Animation animation = AnimationUtils.loadAnimation( this, R.anim.zoom_in );

        devgrey.setVisibility( View.VISIBLE );
        maingame.invalidate();
        frameops.setVisibility( View.VISIBLE );
        frameops.startAnimation( animation );
        startbtn.setVisibility( View.GONE );
        devbut.setEnabled( false );
        opsbut.setEnabled( false );


        if(firsttime) {
            framefirstrun=(FrameLayout)findViewById( R.id.firstrunframe ) ;
            framefirstrun.setVisibility( View.GONE );//FIRST RUN LOGIC

            startbtn = (GifImageView) findViewById( R.id.startgame );
            startbtn.setEnabled( true );
            firsttime=false;
            floatingHandAgain();
        }

    }

    public void hideOptions(View view) {
        frameops.setVisibility( View.GONE );
        devgrey.setVisibility( View.GONE );
        devgrey.invalidate();
        startbtn.setVisibility( View.VISIBLE );
        devbut.setEnabled( true );
        opsbut.setEnabled( true );
        frameops.invalidate();

    }


    public void showDifficultyPanel(View view) {
        frameops.setVisibility( View.GONE );
        frameops.invalidate();
        framediff.setVisibility( View.VISIBLE );
    }

    public void setDifficultyKid(View view) {

        String filename = "difficulty_file.txt";
        String fileContents = "1";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput( filename, Context.MODE_PRIVATE );
            outputStream.write( fileContents.getBytes() );
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        hideDifficultyPanel( "KID" );

    }

    public void setDifficultyMan(View view) {
        String filename = "difficulty_file.txt";
        String fileContents = "2";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput( filename, Context.MODE_PRIVATE );
            outputStream.write( fileContents.getBytes() );
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hideDifficultyPanel( "MAN" );

    }

    public void setDifficultyLegend(View view) {
        String filename = "difficulty_file.txt";
        String fileContents = "3";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput( filename, Context.MODE_PRIVATE );
            outputStream.write( fileContents.getBytes() );
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        hideDifficultyPanel( "LEGEND" );

    }

    public void hideDifficultyPanel(String diffy) {
        String msg = "Difficulty is now set to " + diffy;
        Toast.makeText( this.getApplicationContext(), msg, Toast.LENGTH_SHORT ).show();
        framediff.setVisibility( View.GONE );
        framediff.invalidate();

        //****Universal Closer Functions************
        devgrey.setVisibility( View.GONE );
        devgrey.invalidate();
        startbtn.setVisibility( View.VISIBLE );
        devbut.setEnabled( true );
        opsbut.setEnabled( true );
        //******************************************

    }


    public void showHighScorePanel(View view) {
        frameops.setVisibility( View.GONE );
        //***********Reading scores from file***********************
        String high, combinedlist = "";
        int i;
        for (i = 1; i <= 3; i++) {
            try {
                FileInputStream fileIn = openFileInput( "highscore" + String.valueOf( i ) + ".txt" );
                InputStreamReader InputRead = new InputStreamReader( fileIn );

                char[] inputBuffer = new char[100];
                String s = "";
                int charRead;


                while ((charRead = InputRead.read( inputBuffer )) > 0) {
                    // char to string conversion
                    String readstring = String.copyValueOf( inputBuffer, 0, charRead );
                    s += readstring;
                }
                InputRead.close();
                high = s.substring( 0 );


            } catch (Exception e) {
                high = "0";
                e.printStackTrace();
            }
            if (i == 1) {
                combinedlist = "KID : " + high + "\n\n";
            }
            if (i == 2) {
                combinedlist = combinedlist + "MAN : " + high + "\n\n";
            }
            if (i == 3) {
                combinedlist = combinedlist + "LEGEND : " + high + "\n";
            }
        }


        //*************************************************************************************


        hscorelist.setText( combinedlist );

        framehscore.setVisibility( View.VISIBLE );
        maingame.invalidate();
        frameops.invalidate();


    }


    public void hideHighScorePanel(View view) {
        framehscore.setVisibility( View.GONE );
        framehscore.invalidate();

        //****Universal Closer Functions************
        devgrey.setVisibility( View.GONE );
        devgrey.invalidate();
        startbtn.setVisibility( View.VISIBLE );
        devbut.setEnabled( true );
        opsbut.setEnabled( true );
        //******************************************
    }


    public void showGuide(View view)
    {

        maingame.setVisibility( View.GONE );
        frameops.setVisibility( View.GONE );
        frameops.invalidate();
        devgrey.setVisibility( View.VISIBLE );
        maingame.invalidate();


        guide001.setText( GUIDE1 );
        guide002.setText( GUIDE2 );
        guide003.setText( GUIDE3 );

        guidescroller.scrollTo( 0,0 );
        frameguide.setVisibility( View.VISIBLE );



        if(firsttime){
            framefloatinghand=(FrameLayout)findViewById( R.id.floatinghandframe );//FIRST RUN LOGIC

            btnhs=(Button)findViewById( R.id.buttonhighscore ) ;
            btndiff=(Button)findViewById( R.id.buttondifficulty ) ;
            btnnothanks=(Button)findViewById( R.id.nothanks ) ;

            btnhs.setEnabled( true );
            btndiff.setEnabled( true );
            btnnothanks.setEnabled( true );

            framefloatinghand.setVisibility( View.GONE );
            firsttime=false;
        }




    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    public void hideGuide(View view)
    {
        frameguide.setVisibility( View.GONE );
        frameguide.invalidate();
        devgrey.setVisibility( View.GONE );
        devgrey.invalidate();
        maingame.setVisibility( View.VISIBLE );
        startbtn.setVisibility( View.VISIBLE );
        devbut.setEnabled( true );
        opsbut.setEnabled( true );
        devgrey.invalidate();
    }



    public void floatingHandAgain()
    {

        framefloatinghand=(FrameLayout)findViewById( R.id.floatinghandframe );

        btnhs=(Button)findViewById( R.id.buttonhighscore ) ;
        btndiff=(Button)findViewById( R.id.buttondifficulty ) ;
        btnnothanks=(Button)findViewById( R.id.nothanks ) ;

        btnhs.setEnabled( false );
        btndiff.setEnabled( false );
        btnnothanks.setEnabled( false );

        Animation animation = AnimationUtils.loadAnimation( this, R.anim.zoom_in );

        framefloatinghand.setVisibility( View.VISIBLE );
        framefloatinghand.startAnimation( animation );
        firsttime=true;

    }





    @Override
    protected void onResume() {
        super.onResume();

        framefirstrun=(FrameLayout)findViewById( R.id.firstrunframe );
        startbtn = (GifImageView) findViewById( R.id.startgame );
        opsbut = (ImageView) findViewById( R.id.settings_btn );
        devbut = (ImageView) findViewById( R.id.developer_btn );




        if (prefs.getBoolean("firstrun", true)) {

            devgrey.setVisibility( View.VISIBLE );


            framefirstrun.setVisibility( View.VISIBLE );

            startbtn.setEnabled( false );
            devbut.setEnabled( false );
            firsttime=true;





            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }







}

