package mihau.eu.pitchrecognition;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchProcessor;
import mihau.eu.pitchrecognition.utils.NotePitchMap;

/**
 * Created by kolorszczak on 2017-10-01.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById
    TextView pitch, note;

    @AfterViews
    void afterViews() {
        initPitchRecognition();
    }

    private void initPitchRecognition() {
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);
        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, (pitchDetectionResult, audioEvent) -> {
            final float pitchInHz = pitchDetectionResult.getPitch();
            runOnUiThread(() -> setupPitchLabel(pitchInHz));
        }));
        new Thread(dispatcher,"Audio Dispatcher").start();
    }

    @SuppressLint("SetTextI18n")
    public void setupPitchLabel(float pitchInHz) {
        pitch.setText(String.valueOf(pitchInHz) + " Hz");
        note.setText(NotePitchMap.getNoteFromPitch(pitchInHz));
    }
}
