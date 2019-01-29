package com.versatica.mediasoup

import com.versatica.mediasoup.handlers.sdp.*

/**
 * mediasoup protocol
 * List of messages (requests/responses/notifications) that must be exchanged between client
 * and server side of the mediasoup based application. No signaling protocol is provided but
 * just message payloads. The application is responsible of sending and receiving these messages.
 */


/**
 * From client to server
 * Messages generated by the client-side `Room` instance that must be given to the corresponding server-side
 * `Room` or `Peer` instance (depending on the `target` field).
 */
//mediasoupRequest
open class MediasoupRequest(
    method: String,
    target: String
) {
    val method: String = method
    val target: String = target
}

//mediasoupNotify
open class MediasoupNotify(
    method: String,
    target: String,
    notification: Boolean = true
) {
    val method: String = method
    val target: String = target
    val notification: Boolean = notification
}

//queryRoom [request]
class QueryRoomRequest : MediasoupRequest(
    "queryRoom",
    "room"
)

class QueryRoomResponse {
    var rtpCapabilities: RTCRtpCapabilities? = null
    var mandatoryCodecPayloadTypes: MutableList<Int>? = null
}

//join [request]
class JoinRequest : MediasoupRequest(
    "join",
    "room"
) {
    var peerName: String = ""
    var rtpCapabilities: RTCRtpCapabilities? = null
    var spy: Boolean = false
    var appData: Any? = null
}

class JoinResponse {
    var peers: ArrayList<PeerData>? = ArrayList()
}

class PeerData {
    var name: String = ""
    var consumers: ArrayList<ConsumerData> = ArrayList()
    var appData: Any? = null
}

class ConsumerData {
    var id: Int = 0
    var kind: String = ""
    var peerName: String = ""
    var rtpParameters: RTCRtpParameters = RTCRtpParameters()
    var paused: Boolean = false
    var preferredProfile: String? = ""
    var effectiveProfile: String? = "default"
    var appData: Any? = null
}

//leave [notification]
class LeaveNotification : MediasoupNotify(
    "leave",
    "peer"
) {
    var appData: Any? = null
}

//createTransport [request]
class CreateTransportRequest : MediasoupRequest(
    "createTransport",
    "peer"
) {
    var id: Int = 0
    var direction: String = ""
    var options: TransportOptions? = null
    var appData: Any? = null
    var dtlsParameters: RTCDtlsParameters? = null
}

typealias CreateTransportResponse = TransportRemoteIceParameters

//updateTransport [notification]
class UpdateTransportNotification : MediasoupNotify(
    "updateTransport",
    "peer"
) {
    var id: Int = 0
    var dtlsParameters: RTCDtlsParameters? = null
}

//restartTransport [request]
class RestartTransportRequest : MediasoupRequest(
    "restartTransport",
    "peer"
) {
    var id: Int = 0
}

class RestartTransportResponse {
    var iceParameters: RTCIceParameters? = null
}

//closeTransport [notification]
class CloseTransportNotify : MediasoupNotify(
    "closeTransport",
    "peer"
) {
    var id: Int = 0
    var appData: Any? = null
}

//enableTransportStats [notification]
class EnableTransportStatsNotify : MediasoupNotify(
    "enableTransportStats",
    "peer"
) {
    var id: Int = 0
    var interval: Int = 0
}

//disableTransportStats [notification]
class DisableTransportStatsNotify : MediasoupNotify(
    "disableTransportStats",
    "peer"
) {
    var id: Int = 0
}

//createProducer [request]
class CreateProducerRequest : MediasoupRequest(
    "createProducer",
    "peer"
) {
    var id: Int = 0
    var kind: String = ""
    var transportId: Int = 0
    var rtpParameters: RTCRtpParameters? = null
    var paused: Boolean = false
    var appData: Any? = null
}

class CreateProducerResponse {
}

//updateProducer [notification]
class updateProducerNotify : MediasoupNotify(
    "updateProducer",
    "peer"
) {
    var id: Int = 0
    var rtpParameters: RTCRtpParameters? = null
}

//pauseProducer [notification]
class PauseProducerNotify : MediasoupNotify(
    "pauseProducer",
    "peer"
) {
    var id: Int = 0
    var appData: Any? = null
}

//resumeProducer [notification]
class ResumeProducerNotify : MediasoupNotify(
    "resumeProducer",
    "peer"
) {
    var id: Int = 0
    var appData: Any? = null
}

//closeProducer [notification]
class CloseProducerNotify : MediasoupNotify(
    "closeProducer",
    "peer"
) {
    var id: Int = 0
    var appData: Any? = null
}

//enableProducerStats [notification]
class EnableProducerStatsNotify : MediasoupNotify(
    "enableProducerStats",
    "peer"
) {
    var id: Int = 0
    var interval: Int = 0
}

//disableProducerStats [notification]
class DisableProducerStatsNotify : MediasoupNotify(
    "disableProducerStats",
    "peer"
) {
    var id: Int = 0
}

//enableConsumer [request]
class EnableConsumerRequest : MediasoupRequest(
    "enableConsumer",
    "peer"
) {
    var id: Int = 0
    var transportId: Int = 0
    var paused: Boolean = false
    var preferredProfile: String = ""
}

class EnableConsumerResponse {
    var paused: Boolean = false
    var preferredProfile: String = ""
    var effectiveProfile: String = "default"
}

//pauseConsumer [notification]
class PauseConsumerNotify : MediasoupNotify(
    "pauseConsumer",
    "peer"
) {
    var id: Int = 0
    var appData: Any? = null
}

//resumeConsumer [notification]
class ResumeConsumerNotify : MediasoupNotify(
    "resumeConsumer",
    "peer"
) {
    var id: Int = 0
    var appData: Any? = null
}

//setConsumerPreferredProfile [notification]
class SetConsumerPreferredProfileNotify : MediasoupNotify(
    "setConsumerPreferredProfile",
    "peer"
) {
    var id: Int = 0
    var profile: String = ""
}

//enableConsumerStats [notification]
class EnableConsumerStatsNotify : MediasoupNotify(
    "enableConsumerStats",
    "peer"
) {
    var id: Int = 0
    var interval: Int = 0
}

//disableConsumerStats [notification]
class DisableConsumerStatsNotify : MediasoupNotify(
    "enableConsumerStats",
    "peer"
) {
    var id: Int = 0
}

/**
 * From server to client
 * Messages generated by the server-side `Peer` instance that must be given to the client-side `Room` instance.
 */

//closed [notification]
class ClosedNotify : MediasoupNotify(
    "closed",
    "peer"
) {
    var appData: Any? = null
}

//transportClosed [notification]
class TransportClosedNotify : MediasoupNotify(
    "transportClosed",
    "peer"
) {
    var id: Int = 0
    var appData: Any? = null
}

//transportStats [notification]
class TransportStatsNotify : MediasoupNotify(
    "transportStats",
    "peer"
) {
    var id: Int = 0
    var stats: ArrayList<RTCTransportStats> = ArrayList()
}

//producerPaused [notification]
class ProducerPausedNotify : MediasoupNotify(
    "producerPaused",
    "peer"
) {
    var id: Int = 0
    var appData: Any? = null
}

//producerResumed [notification]
class ProducerResumedNotify : MediasoupNotify(
    "producerResumed",
    "peer"
) {
    var id: Int = 0
    var appData: Any? = null
}

//producerClosed [notification]
class ProducerClosedNotify : MediasoupNotify(
    "producerClosed",
    "peer"
) {
    var id: Int = 0
    var appData: Any? = null
}

//producerStats [notification]
class ProducerStatsNotify : MediasoupNotify(
    "producerStats",
    "peer"
) {
    var id: Int = 0
    var stats: ArrayList<RTCTransportStats> = ArrayList()
}

//newPeer [notification]
class NewPeerNotify : MediasoupNotify(
    "newPeer",
    "peer"
) {
    var peerData: PeerData = PeerData()
}

//peerClosed [notification]
class PeerClosedNotify : MediasoupNotify(
    "peerClosed",
    "peer"
) {
    var name: String = ""
    var appData: Any? = null
}

//newConsumer [notification]
class NewConsumerNotify : MediasoupNotify(
    "newConsumer",
    "peer"
) {
    var consumerData: ConsumerData = ConsumerData()
}

//consumerClosed [notification]
class ConsumerClosedNotify : MediasoupNotify(
    "consumerClosed",
    "peer"
) {
    var id: Int = 0
    val peerName: String = ""
    var appData: Any? = null
}

//consumerPaused [notification]
class ConsumerPausedNotify : MediasoupNotify(
    "consumerPaused",
    "peer"
) {
    var id: Int = 0
    val peerName: String = ""
    var appData: Any? = null
}

//consumerResumed [notification]
class ConsumerResumedNotify : MediasoupNotify(
    "consumerResumed",
    "peer"
) {
    var id: Int = 0
    val peerName: String = ""
    var appData: Any? = null
}

//consumerPreferredProfileSet [notification]
class ConsumerPreferredProfileSetNotify : MediasoupNotify(
    "consumerPreferredProfileSet",
    "peer"
) {
    var id: Int = 0
    val peerName: String = ""
    var profile: String = ""
}

//consumerEffectiveProfileChanged [notification]
class ConsumerEffectiveProfileChangedNotify : MediasoupNotify(
    "consumerEffectiveProfileChanged",
    "peer"
) {
    var id: Int = 0
    val peerName: String = ""
    var profile: String = ""
}

//consumerStats [notification]
class ConsumerStatsNotify : MediasoupNotify(
    "consumerStats",
    "peer"
) {
    var id: Int = 0
    val peerName: String = ""
    val stats: ArrayList<RTCTransportStats> = ArrayList()
}

class SimulcastOptions {
    var simulcast: HashMap<String, Int>? = null
    var enabled = false
}