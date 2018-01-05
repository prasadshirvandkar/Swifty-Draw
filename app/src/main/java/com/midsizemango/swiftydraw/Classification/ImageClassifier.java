package com.midsizemango.swiftydraw.Classification;

import android.content.Context;
import android.content.SharedPreferences;

import com.midsizemango.swiftydraw.Helper.ScoreHelper;
import com.midsizemango.swiftydraw.Model.Recognition;

import org.tensorflow.Operation;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

/**
 * Created by prasads on 27/10/17.
 */

public class ImageClassifier  {

    public static final int INPUT_SIZE = 224;

    private static final int MAX_RESULTS = 3;
    private static final String MODEL_FILE = "retrained_graph_swiftydraw.pb";
    private static final String LABEL_FILE = "retrained_labels_swiftydraw.txt";
    private static final int CLASS_SIZE = 19;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "final_result";
    private static final String[] OUTPUT_NAMES = {OUTPUT_NAME};

    private Context context;
    private TensorFlowInferenceInterface tfInterface;
    private Vector<String> labels;

    public ImageClassifier(Context context) {
        this.context = context;
        this.tfInterface = new TensorFlowInferenceInterface(context.getAssets(), MODEL_FILE);
        initLabels();
    }

    private void initLabels(){
        labels = new Vector<>(CLASS_SIZE);
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(LABEL_FILE)));
            String line;
            while((line = br.readLine()) != null){
                labels.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Recognition> recognizeImage(final float[] imageFloats){
        this.tfInterface.feed(INPUT_NAME, imageFloats, 1, INPUT_SIZE, INPUT_SIZE, 3);
        this.tfInterface.run(OUTPUT_NAMES, false);
        float[] outputs = new float[CLASS_SIZE];
        this.tfInterface.fetch(OUTPUT_NAME, outputs);

        PriorityQueue<Recognition> pq = new PriorityQueue<>(3,
                new Comparator<Recognition>() {
                    @Override
                    public int compare(Recognition r1, Recognition r2) {
                        return Float.compare(r1.getConfidence(), r2.getConfidence());
                    }
                });

        float threshold = 0.2f;
        for(int i=0;i<outputs.length;++i){
            if(outputs[i] > threshold){
                pq.add(new Recognition(""+i, labels.get(i), outputs[i], null));
            }
        }

        final ArrayList<Recognition> recognitions = new ArrayList<>();
        int recognitionSize = Math.min(pq.size(), MAX_RESULTS);
        for(int i=0;i<recognitionSize;++i){
            recognitions.add(pq.poll());
        }

        return recognitions;
    }
}