const express = require("express")
const dgram = require("dgram")
const ws = require("ws")

const HTTP_SERVER_PORT = 8000
const DATA_SERVER_PORT = 9000
const WEB_SOCKET_PORT = 7000

const app = express()
const wss = new ws.WebSocketServer({ port: WEB_SOCKET_PORT })
const server = dgram.createSocket('udp4')


let aircraftStatusData = { altitude: '0', speed: '0', pitch: '0', roll: '0', yaw: '0' }
let parameters = ''
let wsc = null

app.use(express.static("hud"))

function sendStatusData(){
	try{
		wsc.send(JSON.stringify(aircraftStatusData))
	}catch(e){
		console.log(e)
	}
}

wss.on("connection" , (ws) => {
	wsc = ws
	setInterval(sendStatusData, 10)
})

server.on('listening', () => {
	let address = server.address()
	console.log(`[+] DATA-PIPE::[ONLINE] -> ${address.address}:${address.port}`)
})

server.on('message', (msg, info) => {
	try{
		parameters = msg.toString().split(',')
		aircraftStatusData.altitude = parameters[0]
		aircraftStatusData.speed = parameters[1]
		aircraftStatusData.pitch = parameters[2]
		aircraftStatusData.roll = parameters[3]
		aircraftStatusData.yaw = parameters[4]
	}catch(e){}
})

app.listen(HTTP_SERVER_PORT, () => { console.log("[+] TFX-HUD::[ONLINE]") } )
server.bind(DATA_SERVER_PORT)
