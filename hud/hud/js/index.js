let pitchAngle = 0
let data = {}
let ws = null

function updateHUD(data){
	try{
		pitchAngle = data.pitch
		pitchAngle = (pitchAngle > 180 ? 360 - pitchAngle : pitchAngle) * (Math.PI / 180) - 0.5 * Math.PI
		hud.data.altitude = data.altitude
		hud.data.speed = data.speed
		hud.data.heading = data.yaw * (Math.PI / 180)
		hud.data.roll = -data.roll * (Math.PI / 180)
		hud.data.pitch = pitchAngle

	}catch(e){}
}



function getAircraftStatusData(){
	try{
		ws = new WebSocket(`ws://${window.location.hostname}:7000`)
		ws.onopen = () => {
			ws.onmessage = (data) => {
				data = JSON.parse(data.data)
				updateHUD(data)
			}
		}

		ws.onclose = () => {
			getAircraftStatusData()
		}
	}catch(e){
		console.error("[x] WEBSOCKET ERROR")
	}
}

getAircraftStatusData()
