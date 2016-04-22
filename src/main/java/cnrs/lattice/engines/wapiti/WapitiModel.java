package cnrs.lattice.engines.wapiti;


import fr.limsi.wapiti.SWIGTYPE_p_mdl_t;
import fr.limsi.wapiti.Wapiti;

import java.io.File;

/**
 * User: zholudev
 * Date: 3/17/14
 */
public class WapitiModel {

    private SWIGTYPE_p_mdl_t model;
    private File modelFile;

    public WapitiModel(File modelFile) throws Exception {
        this.modelFile = modelFile;
        init();
    }

    private synchronized void init() throws Exception {
        if (model != null) {
            return;
        }
        if (!modelFile.exists() || modelFile.isDirectory()) {
            throw new Exception("Model file does not exists or a directory: " + modelFile.getAbsolutePath());
        }
        model = WapitiWrapper.getModel(modelFile);
    }

    public String label(String data) throws Exception {
        if (model == null) {
            init();
        }
        String label = WapitiWrapper.label(model, data).trim();//.label(model, data).trim();
        return label;
    }

    public synchronized void close() {
        if (model != null) {
            Wapiti.freeModel(model);
            model = null;
        }
    }

    public static void train(File template, File trainingData, File outputModel) {
        train(template, trainingData, outputModel, "");
    }

    public static void train(File template, File trainingData, File outputModel, String params) {
		String args = String.format("train " + params + " -p %s %s %s", template.getAbsolutePath(), trainingData.getAbsolutePath(), outputModel.getAbsolutePath());
		System.out.println("Training with equivalent command line: \n" + "wapiti " + args);
		Wapiti.runWapiti(args);
    }



}