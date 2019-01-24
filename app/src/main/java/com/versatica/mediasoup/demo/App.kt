package com.versatica.mediasoup.demo

import android.annotation.SuppressLint
import com.versatica.mediasoup.*
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.socket.client.Ack
import io.socket.client.IO
import org.webrtc.AudioTrack
import org.webrtc.MediaStreamTrack
import org.webrtc.VideoTrack

/**
 * @author wolfhan
 */

class App(val roomId: String, val peerName: String) {

    val logger = Logger("App")

    val socket = IO.socket("http://172.16.70.201:8080", IO.Options().also {
        it.query = "roomId=$roomId&peerName=$peerName"
    })

    // Transport for sending our media.
    var sendTransport: Transport? = null
    // Transport for receiving media from remote Peers.
    var recvTransport: Transport? = null

    // Create a local Room instance associated to the remote Room.
    @Suppress("UNCHECKED_CAST")
    val roomObj = Room().also { room ->
        room.join(peerName)
            .flatMap {
                // Create the Transport for sending our media.
                sendTransport = room.createTransport("send")
                // Create the Transport for receiving media from remote Peers.
                recvTransport = room.createTransport("recv")

                val peers = it as MutableList<Peer>
                peers.forEach { peer ->
                    handlePeer(peer)
                }
                Observable.create(ObservableOnSubscribe<Unit> { observableEmitter ->
                    observableEmitter.onNext(Unit)
                })
            }

        // Event fired by local room when a new remote Peer joins the Room
        room.on("newpeer") {
            val peer = it[0] as Peer
            logger.debug("A new Peer joined the Room: ${peer.name}")

            // Handle the Peer.
            handlePeer(peer)
        }

        // Event fired by local room
        room.on("request") {
            val size = it.size
            val request = it[0]
            val ack: Ack?
            if (size == 3) {
                val callback = it[1] as Function1<Any, *>
                val errCallback = it[2] as Function1<Any, *>
                ack = Ack { subArgs ->
                    val err = subArgs[0]
                    val response = subArgs[1]
                    if (err == null || err == false) {
                        // Success response, so pass the mediasoup response to the local Room.
                        callback(response)
                    } else {
                        errCallback(err)
                    }
                }
            } else {
                ack = null
            }
            logger.debug("REQUEST: $request")
            socket.emit("mediasoup-request", request, ack)
        }

        // Be ready to send mediaSoup client notifications to our remote mediaSoup Peer
        room.on("notify") { args ->
            val notification = args[0]
            logger.debug("New notification from local room: $notification")
            socket.emit("mediasoup-notification", notification)
        }

        // Handle notifications from server, as there might be important info, that affects stream
        socket.on("mediasoup-notification") {
            val notification = it[0] as MediasoupNotify
            logger.debug("New notification came from server: $notification")
            room.receiveNotification(notification)
        }
    }

    /**
     * Handles specified peer in the room
     *
     * @param peer
     */
    fun handlePeer(peer: Peer) {
        // Handle all the Consumers in the Peer.
        peer.consumers.forEach {
            handleConsumer(it)
        }

        // Event fired when the remote Room or Peer is closed.
        peer.on("close") {
            logger.debug("Remote Peer closed")
        }

        // Event fired when the remote Peer sends a new media to mediasoup server.
        peer.on("newconsumer") {
            logger.debug("Got a new remote Consumer")
            // Handle the Consumer.
            handleConsumer(it[0] as Consumer)
        }
    }

    /**
     * Handles specified consumer
     *
     * @param consumer
     */
    @SuppressLint("CheckResult")
    fun handleConsumer(consumer: Consumer) {
        // Receive the media over our receiving Transport.
        if (recvTransport != null) {
            consumer.receive(recvTransport as Transport)
                .flatMap {
                    logger.debug("Receiving a new remote MediaStreamTrack: ${consumer.kind}")

                    val track = it as MediaStreamTrack
                    // Attach the track to a MediaStream and play it.
                    if (consumer.kind === "video") {
                        val videoTrack = track as VideoTrack
                        //todo add video to ui view

                    }
                    if (consumer.kind === "audio") {
                        val audioTrack = track as AudioTrack
                        //todo add audio to ui view

                    }
                    Observable.create(ObservableOnSubscribe<Unit> { observableEmitter ->
                        observableEmitter.onNext(Unit)
                    })
                }
        }

        // Event fired when the Consumer is closed.
        consumer.on("close") {
            logger.debug("Consumer closed")
        }
    }

}