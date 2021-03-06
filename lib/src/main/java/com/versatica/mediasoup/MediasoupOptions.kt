package com.versatica.mediasoup

import com.versatica.mediasoup.handlers.sdp.RTCIceServer
import com.versatica.mediasoup.handlers.sdp.RTCIceTransportPolicy

class TransportOptions {
    //Offer UDP ICE candidates.
    var udp: Boolean = true
    //Offer TCP ICE candidates.
    var tcp: Boolean = true
    //Prefer IPv4 over IPv6 ICE candidates.
    var preferIPv4: Boolean = false
    //Prefer IPv6 over IPv4 ICE candidates.
    var preferIPv6: Boolean = false
    //Prefer UDP over TCP ICE candidates.
    var preferUdp: Boolean = false
    //Prefer TCP over UDP ICE candidates.
    var preferTcp: Boolean = false
}

class RoomOptions {
    //roomSettings
    var roomSettings: QueryRoomResponse? = null
    //Timeout for mediasoup protocol sent requests (in milliseconds)
    var requestTimeout: Long = 300000000
    //Options for created transports.
    var transportOptions: TransportOptions = TransportOptions()
    //Array of TURN servers.
    var turnServers: List<RTCIceServer> = ArrayList()
    //The ICE transport policy.
    var iceTransportPolicy: String = RTCIceTransportPolicy.all.name
    var spy: Boolean = false
}