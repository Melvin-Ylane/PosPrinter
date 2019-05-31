package cordova.plugin.posprinter;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pax.dal.IPrinter;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.dal.exceptions.PrinterDevException;

import android.widget.Toast;
// import java.lang.Exception;

/**
 * This class echoes a string called from JavaScript.
 */
public class PosPrinter extends CordovaPlugin {

    public IDAL idal = null;
    private IPrinter printer = null;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        
        try{
            idal = NeptuneLiteUser.getInstance().getDal(this.cordova.getActivity().getApplicationContext());
            printer = idal.getPrinter();
        }catch(Exception ex){
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(this.cordova.getActivity().getApplicationContext(), "Something went wrong "+ ex, duration);
            toast.show();
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }

        if (action.equals("startPrint")) {
            this.startPrint(args, callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void startPrint(JSONArray args, CallbackContext callbackContext) {
        if (args != null) {

            try{
                String message = args.getJSONObject(0).getString("param1");
            
                try{
                    printer.init();
                    printer.printStr(message, "utf8");
                    printer.step(Integer.parseInt(message));
                    printer.setGray(5);

                    printer.start();

                }catch(PrinterDevException ex){
                    callbackContext.error("Something went wrong at print "+ ex.toString());
                }
            }catch(JSONException ex){
                callbackContext.error("Something went wrong at print "+ ex.toString());
            }
        }else{
            callbackContext.error("Please do not pass null value");
        }
    }
}
