package com.infolangues.mondico;

/**
 * Created by bonnet on 28/11/2017.
 * unused yet
 */

/* TODO delete if never used
import java.io.IOException;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;

 class SoundFilePlayer extends AsyncTask<Void, Void, Void>
        implements MediaPlayer.OnPreparedListener {


    private MediaPlayer mediaPlayer;

    @Override
    protected Void doInBackground(Void... params) {
        mediaPlayer = new MediaPlayer();
        try {
            Context context = MyApplication.getAppContext();

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            final Uri sound_file_uri = Uri.parse("http://lacito.vjf.cnrs.fr/pangloss/dictionaries/japhug/data/audio/wav/vt-Car.wav");
            //final Uri sound_file_uri = Uri.parse("https://cocoon.huma-num.fr/data/archi/mp3/234747_record_44k.mp3");
            mediaPlayer.setDataSource(context, sound_file_uri);
            //mediaPlayer.prepareAsync();
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(this);
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if(mediaPlayer.isPlaying()  || mediaPlayer.getCurrentPosition() > 1) {
            System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> AA");
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
        else {
            System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 00");
            mediaPlayer.start();
        }
    }

}
*/
