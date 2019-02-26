package jp.co.georepublic.datalogreceiver;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import android.net.TrafficStats;
import org.json.JSONObject;

/** DataLogReceiverPlugin */
public class DataLogReceiverPlugin implements MethodCallHandler {
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "data_log_receiver");
    channel.setMethodCallHandler(new DataLogReceiverPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if(call.method.equals("getTrafficStats")) {
      result.success( getTrafficStats().toString() );
    }
    else {
      result.notImplemented();
    }
  }

  public JSONObject getTrafficStats() {
    long rxMobileBytesTe = TrafficStats.getMobileRxBytes();   // received mobile data size
    long txMobileBytesTe = TrafficStats.getMobileTxBytes();   // transmitted mobile data size

    long rxWiFiBytesTe   = TrafficStats.getTotalRxBytes() - rxMobileBytesTe;
    long txWiFiBytesTe   = TrafficStats.getTotalTxBytes() - txMobileBytesTe;

    // get IP address //////////////////////////////////
    String ipAddress = NetworkUtils.getIpAddress();

    JSONObject jsonObj = new JSONObject();

    try {
      jsonObj.put("rxMobileBytesTe", rxMobileBytesTe);
      jsonObj.put("txMobileBytesTe", txMobileBytesTe);
      jsonObj.put("rxWiFiBytesTe", rxWiFiBytesTe);
      jsonObj.put("txWiFiBytesTe", txWiFiBytesTe);
      jsonObj.put("ipAddress", ipAddress);
    }
    catch( Exception ex ) {
      ex.toString();
    }

    return jsonObj;

  }
}
