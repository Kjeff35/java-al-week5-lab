package org.bexos;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.misc.InputMappedClassifier;
import weka.classifiers.trees.J48;
import weka.core.*;
import weka.classifiers.*;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.Random;

public class SpamClassifier {
    private final InputMappedClassifier classifier;
    private final Instances dataStructure;
    private final StringToWordVector textFilter;

    public SpamClassifier(String algorithm, EmailDataset dataset) throws Exception {
        this.textFilter = dataset.getTextFilter();
        this.dataStructure = dataset.getProcessedDataset();

        // Base filtered classifier
        FilteredClassifier base = new FilteredClassifier();
        base.setFilter(textFilter);
        base.setClassifier(createClassifier(algorithm));
        base.buildClassifier(dataset.getProcessedDataset());

        // Wrap with InputMappedClassifier
        InputMappedClassifier imc = new InputMappedClassifier();
        imc.setModelHeader(dataset.getProcessedDataset());
        imc.setClassifier(base);
        imc.setSuppressMappingReport(true); // optional to suppress logs

        // Replace classifier
        classifier = imc;
    }


    private Classifier createClassifier(String algorithm) throws Exception {
        return switch (algorithm.toLowerCase()) {
            case "naivebayes" -> new NaiveBayes();
            case "j48" -> new J48();
            case "svm" -> new SMO();
            default -> throw new Exception("Unknown algorithm: " + algorithm);
        };
    }

    public Evaluation evaluate(int folds) throws Exception {
        Evaluation eval = new Evaluation(dataStructure);
        eval.crossValidateModel(classifier, dataStructure,
                Math.min(folds, dataStructure.numInstances()), new Random(42));
        return eval;
    }

    public String classifyEmail(String emailContent) throws Exception {
        // Create instance with just the content (class value can be dummy)
        Instances rawStructure = new Instances(dataStructure, 0);
        rawStructure.setClassIndex(dataStructure.classIndex());
        Instance newInst = new DenseInstance(2);
        newInst.setValue(rawStructure.attribute(0), emailContent);
        newInst.setDataset(rawStructure);

        // Classification via InputMappedClassifier
        double pred = classifier.classifyInstance(newInst);
        return dataStructure.classAttribute().value((int) pred);
    }

}
