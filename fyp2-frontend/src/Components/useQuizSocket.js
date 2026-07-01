import { useEffect, useRef } from 'react'
import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { API_CONFIG } from "../Components/Config/Base_url"

const BASE_URL = API_CONFIG.BASE_URL;

export default function useQuizSocket(roomCode, onMessage, token) {

    const clientRef = useRef(null)
    const onMessageRef = useRef(onMessage)  // ← store callback in ref


     // ← keep ref in sync whenever onMessage changes
    useEffect(() => {
        onMessageRef.current = onMessage
    }, [onMessage])

    useEffect(() => {
    if (!roomCode || !token) return

    // Disconnect previous client if exists
    if (clientRef.current) {
        clientRef.current.deactivate()
    }

    const client = new Client({
        webSocketFactory: () => new SockJS(`${BASE_URL}/ws`),
        connectHeaders: {
            Authorization: `Bearer ${token}`,
        },
        onConnect: () => {
            console.log(`WebSocket connected, subscribing to /topic/quiz/${roomCode}`)
            client.subscribe(`/topic/quiz/${roomCode}`, (message) => {
                console.log('WebSocket message received:', message.body)
                const data = JSON.parse(message.body)
                onMessageRef.current(data)
            })
        },
        onDisconnect: () => {
            console.log('WebSocket disconnected')
        }
    })

    client.activate()
    clientRef.current = client

    return () => client.deactivate()

}, [roomCode, token])

    return clientRef
}