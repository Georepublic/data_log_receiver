package jp.co.georepublic.datalogreceiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


import static android.provider.Settings.ACTION_SETTINGS;

/**
 * Utility class to check network connection
 */
public final class NetworkUtils {
    /* ==============================================================
     * static fields
     * ============================================================== */
    /** Network Types */
    public enum NetworkType {
        /** disconnected        */  OFF,
        /** WIFI connection     */  WIFI,
        /** MOBILE connection   */  MOBILE,
        /** Wired connection    */  LAN;
    }


    /* ==============================================================
     * instance methods
     * ============================================================== */
    /**
     * get network connection state
     * @param context application context
     * @return connected network type
     */
    public static NetworkType getNetworkState(final Context context) {
        // get connectivity manager ////////////////////////
        ConnectivityManager manager = ConnectivityManager.class.cast(context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo         info    = manager.getActiveNetworkInfo();

        NetworkType type = NetworkType.OFF;
        // case off line ///////////////////////////////////
        if( info == null || !info.isConnected() ) {
            return type;
        }

        // other case //////////////////////////////////////
        switch( info.getType() ) {
            // regarding type as LAN :::::::::::::::::::::::
            case ConnectivityManager.TYPE_ETHERNET:
                type = NetworkType.LAN; break;
            // regarding type as WiFi ::::::::::::::::::::::
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
                type = NetworkType.WIFI; break;
            // regarding type as mobile ::::::::::::::::::::
            case ConnectivityManager.TYPE_MOBILE:
            case ConnectivityManager.TYPE_MOBILE_DUN:
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
            case ConnectivityManager.TYPE_MOBILE_MMS:
            case ConnectivityManager.TYPE_MOBILE_SUPL:
                type = NetworkType.MOBILE; break;
//            // regarding type as bluetooth :::::::::::::::::
//            case ConnectivityManager.TYPE_BLUETOOTH:
//                type = NetworkType.OFF; break;
            // regarding type as unknown :::::::::::::::::::
            default :
                type = NetworkType.OFF; break;
        }

        return type;
    }

    /**
     * open data roaming setting
     * @param context application context
     */
    public static void openDataRoamingSetting(final Context context) {
        // open location setting ///////////////////////////
        context.startActivity(new Intent(ACTION_SETTINGS));
    }

    /**
     * get IP address
     * @return get IP address
     */
    public static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> netIFs = NetworkInterface.getNetworkInterfaces();
            while( netIFs.hasMoreElements() ) {
                NetworkInterface         netIF         = netIFs.nextElement();
                Enumeration<InetAddress> inetAddresses = netIF.getInetAddresses();
                while( inetAddresses.hasMoreElements() ) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if( !inetAddress.isLoopbackAddress() &&
                            netIF.isUp() &&
                            (inetAddress instanceof Inet4Address) )
                    {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }
        catch(SocketException exp) {
            Log.e("NetworkUtils","exp",exp);
        }
        return null;
    }
}
