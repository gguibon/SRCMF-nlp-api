package cnrs.lattice.engines.wapiti;

import com.google.common.base.Throwables;

import fr.limsi.wapiti.SWIGTYPE_p_mdl_t;
import fr.limsi.wapiti.Wapiti;

import java.io.File;

/**
 * User: zholudev
 * Date: 3/17/14
 */
public class WapitiWrapper {
    public static String label(SWIGTYPE_p_mdl_t model, String data) throws Exception {
        if (data.trim().isEmpty()) {
            System.err.println("Empty data is provided to Wapiti tagger: " + Throwables.getStackTraceAsString(new Throwable()));
            return "";
        }

        String result = Wapiti.labelFromModel(model, data);
        if (result == null) {
            throw new Exception("Wapiti tagging failed (null data returned)");
        }
        return result;
    }

    public static SWIGTYPE_p_mdl_t getModel(File model) {
        return getModel(model, false);
    }

    public static SWIGTYPE_p_mdl_t getModel(File model, boolean checkLabels) {
        return Wapiti.loadModel("label " + (checkLabels ? "--check" : "") + " -m " + model.getAbsolutePath());
    }

}