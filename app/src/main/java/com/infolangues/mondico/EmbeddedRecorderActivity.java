package com.infolangues.mondico;

/**
 * "help us to improve" this definition page
 * Created by bonnet on 04/06/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

// unused >> import java.io.FileNotFoundException;


public class EmbeddedRecorderActivity extends Activity {
    private Button play,stop,record;
    private MediaRecorder myAudioRecorder;
    private final String outputFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);

        // @TODO don't use hard coded strings
        final String CHOSEN_WORD = "chosen_word";
        Button send;

        Intent intent = getIntent();
        //TextView textView = (TextView) findViewById(R.id.textView);

        String word;
        if (intent != null) {
            word = intent.getStringExtra(CHOSEN_WORD);
        }
        else
        {
            // @TODO send an error
            word = "";
        }

        final String word_to_improve = dictData.getLocalForm4word(word);
        final String improve_message = MyApplication.getAppContext().getString(R.string.comment_sentence) + word_to_improve;

        final TextView message_view= findViewById(R.id.textView);
        message_view.setText(improve_message);

        //final CheckBox sound_file_box = findViewById(R.id.soundFileBox);  // CC 200916 supprimé pour Nengee
        final EditText feedback_message_editText = findViewById(R.id.feedback_message_editText);

        // added post-publication
        final Context context = getApplicationContext();
        feedback_message_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% yo %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"  + context.getString(R.string.comment_edit_text_default_string) + feedback_message_editText.getText());
                //System.err.println(feedback_message_editText.getText() + ".equals(" + context.getString(R.string.comment_edit_text_default_string) + ") = " + feedback_message_editText.getText().equals(context.getString(R.string.comment_edit_text_default_string)));
                System.err.println(feedback_message_editText.getText().length() + " " + context.getString(R.string.comment_edit_text_default_string).length());
                System.err.println(feedback_message_editText.getText() + " " + context.getString(R.string.comment_edit_text_default_string).length());
                for(int i_tmp = 0; i_tmp < feedback_message_editText.getText().length(); i_tmp++)
                    System.err.println(feedback_message_editText.getText().charAt(i_tmp) + "  " + context.getString(R.string.comment_edit_text_default_string).charAt(i_tmp));
                if (context.getString(R.string.comment_edit_text_default_string).equals(feedback_message_editText.getText().toString())) {
                    System.err.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% yo %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% 2" + context.getString(R.string.comment_edit_text_default_string) + feedback_message_editText.getText());
                    feedback_message_editText.setText("");
                }
            }
        });

        send= findViewById(R.id.send_button);
        /* Supprime l'enregistrement cf
        sound_file_box.setEnabled(false);

        play= findViewById(R.id.button3);
        stop= findViewById(R.id.button2);
        record= findViewById(R.id.button);


        stop.setEnabled(false);
        play.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

        // @todo verifier

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // @todo verifier
                    myAudioRecorder=new MediaRecorder();
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                    myAudioRecorder.setOutputFile(outputFile);

                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                }
                catch (IOException | IllegalStateException e) {
                    // TODO do something more usable
                    e.printStackTrace();
                }

                //catch (IllegalStateException e) { e.printStackTrace();}

                record.setEnabled(false);
                stop.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder  = null;

                stop.setEnabled(false);
                play.setEnabled(true);

                Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();

                sound_file_box.setEnabled(true);
                sound_file_box.setChecked(true);
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException,SecurityException,IllegalStateException {
                MediaPlayer m = new MediaPlayer();

                try {
                    m.setDataSource(outputFile);
                }   catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    m.prepare();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                m.start();
                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();

                record.setEnabled(true);
            }
        });
        */

        //final String word_to_improve = word;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                String[] to = new String[]{context.getString(R.string.feedback_mail)};
                String subject = "["+context.getString(R.string.app_name)+"] > " + word_to_improve;
                //String subject = "[message from Android Application] about " + word_to_improve;
                //String subject = "[message from " + R.string.app_name + "] about " + word_to_improve;
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                //Intent emailIntent = getPackageManager().getLaunchIntentForPackage("com.android.email"); // or check with com.google.android.gm
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, feedback_message_editText.getText());
                /*if (sound_file_box.isChecked())       // CC 200916 supprimé pour Nengee
                {
//                    try {
                    File attached_file = new File(
                            Environment.getExternalStorageDirectory().getAbsolutePath() +
                                    "/recording.3gp");
//                    }
//                    catch (FileNotFoundException file_not_found_exception){
//                    }

                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(attached_file));

                    // attached_file.delete();
                }*/
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Choose an Email client"));
            }
        });

        // home button may be to put in a frame
        ImageButton button_home = findViewById(R.id.home_button);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmbeddedRecorderActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


}
