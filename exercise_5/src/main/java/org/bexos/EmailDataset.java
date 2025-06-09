package org.bexos;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class EmailDataset {
    private final Instances rawDataset;
    private final Instances processedDataset;
    private final StringToWordVector textFilter;

    public EmailDataset(String filePath) throws Exception {
        // Load dataset
        DataSource source = new DataSource(filePath);
        rawDataset = source.getDataSet();

        if (rawDataset.numInstances() < 1) {
            throw new Exception("Dataset is empty!");
        }

        // Set class index (last attribute by convention)
        if (rawDataset.classIndex() == -1) {
            rawDataset.setClassIndex(rawDataset.numAttributes() - 1);
        }

        // Initialize and apply text filter
        textFilter = createTextFilter();
        processedDataset = applyTextFilter();
    }

    private StringToWordVector createTextFilter() throws Exception {
        StringToWordVector filter = new StringToWordVector();
        filter.setLowerCaseTokens(true);
        filter.setWordsToKeep(1000);
        filter.setOutputWordCounts(true);
        filter.setMinTermFreq(2);
        filter.setIDFTransform(true);
        filter.setTFTransform(true);
        filter.setInputFormat(rawDataset); // Critical for batch filtering
        return filter;
    }

    private Instances applyTextFilter() throws Exception {
        return Filter.useFilter(rawDataset, textFilter);
    }

    public Instances getProcessedDataset() {
        return processedDataset;
    }

    public Instances getRawDatasetStructure() {
        return new Instances(rawDataset, 0);
    }

    public StringToWordVector getTextFilter() {
        return textFilter;
    }
}
