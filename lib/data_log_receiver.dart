import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';

class DataLogReceiver {
  static const MethodChannel _channel =
      const MethodChannel('data_log_receiver');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<TrafficStats> get getTrafficStats async {
    dynamic trafficStats = await _channel.invokeMethod('getTrafficStats');
    var data = json.decode(trafficStats);

    return new TrafficStats(data['rxMobileBytesTe'], data['txMobileBytesTe'],
        data['rxWiFiBytesTe'], data['txWiFiBytesTe'], data['ipAddress']);
  }
}

class TrafficStats {
  final int rxMobileBytesTe;
  final int txMobileBytesTe;

  final int rxWiFiBytesTe;
  final int txWiFiBytesTe;
  final String ipAddress;
  TrafficStats(this.rxMobileBytesTe, this.txMobileBytesTe, this.rxWiFiBytesTe,
      this.txWiFiBytesTe, this.ipAddress);
}
