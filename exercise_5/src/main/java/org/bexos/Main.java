package org.bexos;

// File: Main.java

import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.common.io.ClassPathResource;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        String csvPath = new ClassPathResource("emails.csv").getFile().getPath();
        List<double[]> featuresList = new ArrayList<>();
        List<Double> labelsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] parts = parseCSVLine(line);
                if (parts.length < 2) continue;
                String text = parts[0];
                int label = Integer.parseInt(parts[1]);
                double[] vector = vectorize(text);
                featuresList.add(vector);
                labelsList.add((double) label);
            }
        }

        INDArray features = Nd4j.create(featuresList.toArray(new double[0][]));
        INDArray labels = Nd4j.create(labelsList.stream().mapToDouble(Double::doubleValue).mapToObj(d -> new double[]{d}).toArray(double[][]::new));

        DataSet dataSet = new DataSet(features, labels);
        DataSetIterator trainIter = new ListDataSetIterator<>(dataSet.asList(), 2);

        int inputSize = features.columns();

        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
                .seed(123)
                .updater(new Nesterovs(0.1, 0.9))
                .list()
                .layer(new DenseLayer.Builder().nIn(inputSize).nOut(10).activation(Activation.RELU).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.XENT).activation(Activation.SIGMOID).nIn(10).nOut(1).build())
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(config);
        model.init();
        model.setListeners(new ScoreIterationListener(10));

        int numEpochs = 30;
        for (int i = 0; i < numEpochs; i++) {
            trainIter.reset();
            model.fit(trainIter);
        }

        // Evaluation
        Evaluation eval = new Evaluation();
        INDArray output = model.output(features);
        eval.eval(labels, output);
        System.out.println(eval.stats());

        // Test prediction
        String testEmail = "click now for free prize";
        INDArray testVector = Nd4j.create(new double[][]{vectorize(testEmail)});
        INDArray prediction = model.output(testVector);
        double probability = prediction.getDouble(0);
        System.out.println("Spam probability: " + probability);
        System.out.println("Prediction: " + (probability > 0.5 ? "SPAM" : "NOT SPAM"));
    }

    private static final String[] VOCAB = {"free", "click", "money", "meeting", "schedule", "lottery", "winner", "updated", "prize", "now"};

    private static double[] vectorize(String text) {
        text = text.toLowerCase();
        double[] vector = new double[VOCAB.length];
        for (int i = 0; i < VOCAB.length; i++) {
            if (text.contains(VOCAB[i])) {
                vector[i] = 1.0;
            }
        }
        return vector;
    }

    // Helper to parse lines like: "Free money, click here",1
    private static String[] parseCSVLine(String line) {
        if (line.startsWith("\"")) {
            int closingQuote = line.indexOf("\"", 1);
            if (closingQuote > 0 && closingQuote + 2 <= line.length()) {
                String text = line.substring(1, closingQuote);
                String label = line.substring(closingQuote + 2);
                return new String[]{text, label};
            }
        }
        return line.split(",");
    }
}