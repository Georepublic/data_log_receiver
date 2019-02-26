import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:data_log_receiver/data_log_receiver.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    TrafficStats trafficStats;

    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      trafficStats = await DataLogReceiver.getTrafficStats;
      platformVersion = "rxMobileBytesTe: ${trafficStats.rxWiFiBytesTe}, "
          "txMobileBytesTe: ${trafficStats.txMobileBytesTe},"
          "rxWiFiBytesTe: ${trafficStats.rxWiFiBytesTe}, "
          "txWiFiBytesTe: ${trafficStats.txWiFiBytesTe}, "
          "ipAddress: ${trafficStats.ipAddress}";
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Network Traffic Data'),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: initPlatformState,
          backgroundColor: Colors.red,
          child: Icon(Icons.update),
        ),
        body: Center(
          child: Text('Traffic Info: $_platformVersion\n'),
        ),
      ),
    );
  }
}
