package org.bexos;

import weka.classifiers.Evaluation;

public class Main {

    public static void main(String[] args) throws Exception {
        try {
            // Configuration
            String datasetPath = "spam_data.arff";
            String algorithm = "NaiveBayes";
            int targetFolds = 10;

            // Load and process dataset
            EmailDataset emailData = new EmailDataset(datasetPath);
            System.out.println("Loaded dataset with " +
                    emailData.getProcessedDataset().numInstances() + " instances");

            // Train classifier
            SpamClassifier classifier = new SpamClassifier(algorithm, emailData);

            // Evaluate
            Evaluation eval = classifier.evaluate(targetFolds);
            printEvaluationResults(eval);

            // Test classifications
            testSampleEmails(classifier);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printEvaluationResults(Evaluation eval) throws Exception {
        System.out.println("\n=== Evaluation Results ===");
        System.out.println(eval.toSummaryString());
        System.out.println("\n" + eval.toClassDetailsString());
        System.out.println("\nConfusion Matrix:\n" + eval.toMatrixString());
    }

    private static void testSampleEmails(SpamClassifier classifier) throws Exception {
        String[] testEmails = {
                "WIN A FREE PRIZE NOW!!! CLICK HERE!!!",
                "Hi team, our weekly meeting is scheduled for 3pm",
                "Your bank account needs immediate verification",
                "Dear John, just checking in about our lunch plans",
                "URGENT: Your PayPal account has been locked",
                "Mom, can you pick up some milk on your way home?"
        };

        System.out.println("\n=== Test Classifications ===");
        for (String email : testEmails) {
            try {
                String result = classifier.classifyEmail(email);
                System.out.printf("%-60s → %s\n",
                        email.length() > 55 ? email.substring(0, 52) + "..." : email,
                        result);
            } catch (Exception e) {
                System.out.printf("%-60s → Error: %s\n",
                        email.length() > 55 ? email.substring(0, 52) + "..." : email,
                        e.getMessage());
            }
        }
    }
}