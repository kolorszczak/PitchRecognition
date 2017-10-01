package mihau.eu.pitchrecognition;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;
import mihau.eu.pitchrecognition.utils.NotePitchMap;

/**
 * Created by kolorszczak on 2017-10-01.
 */
@EActivity()
public class MainActivity extends AppCompatActivity {

    @ViewById
    TextView pitch, note;

    @AfterViews
    void afterViews() {
        initPitchRecognition();
    }

    private void initPitchRecognition() {
        AudioDispatcher audioDispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);

        PitchDetectionHandler pitchDetectionHandler = (pitchDetectionResult, audioEvent) -> {
            final float pitchInHz = pitchDetectionResult.getPitch();
            runOnUiThread(() -> setupPitchLabel(pitchInHz));
        };

        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pitchDetectionHandler);
        audioDispatcher.addAudioProcessor(pitchProcessor);

        Thread audioThread = new Thread(audioDispatcher, "Audio Thread");
        audioThread.start();
    }

    @SuppressLint("SetTextI18n")
    public void setupPitchLabel(float pitchInHz) {
        pitch.setText(String.valueOf(pitchInHz) + " Hz");
        note.setText(NotePitchMap.getNoteFromPitch(pitchInHz));
    }
}
