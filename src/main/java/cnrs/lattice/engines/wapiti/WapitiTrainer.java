package cnrs.lattice.engines.wapiti;

import java.math.BigDecimal;

import java.io.File;

/**
 * User: zholudev
 * Date: 3/20/14
 */
public class WapitiTrainer {

    public static final String WAPITI = "wapiti";

	// default training parameters (only exploited by Wapiti)
	private double epsilon = 0.00001; // default size of the interval for stopping criterion
	private int window = 20; // default similar to CRF++

    public void train(File template, File trainingData, File outputModel, int numThreads) {
		System.out.println("epsilon: " + epsilon);
		System.out.println("window: " + window);
		System.out.println("nb threads: " + numThreads);
        WapitiModel.train(template, trainingData, outputModel, "--nthread " + numThreads +
//       		" --algo sgd-l1" +
			" -e " + BigDecimal.valueOf(epsilon).toPlainString() +
			" -w " + String.valueOf(window) +
			""
        );
    }

    public String getName() {
        return WAPITI;
    }
	
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }
	
    public void setWindow(int window) {
        this.window = window;
    }
	
    public double getEpsilon() {
        return epsilon;
    }
	
    public int getWindow() {
        return window;
    }
}