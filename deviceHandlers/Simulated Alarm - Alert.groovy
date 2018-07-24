/**
 *  Simulated Alarm - Alert
 *
 *	This panel works with a webCoRE piston to mirror the state of SHMstatus/alert and allow the resetting of SHMstatus/alert.
 *	This shows the current alert status in the favorites area, and when clicking it goes to the details page of this device to change the SHMstatus/alert.
 *
 *  Copyright 2018 mbarone
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
metadata {
	definition (name: "Simulated Alarm - Alert", namespace: "mbarone/apps", author: "mbarone") {
		capability "Alarm"
		capability "Sensor"
		capability "Actuator"
		capability "Health Check"
	}

	simulator {
		// reply messages
		["siren","off"].each {
			reply "$it": "alarm:$it"
		}
	}

	tiles {
		standardTile("alarm", "device.alarm", width: 3, height: 2) {
			state "off", label:'OFF', icon:'st.secondary.off', backgroundColor:"#00a0dc", defaultState: true
			state "siren", label:'ALERT', icon:"st.secondary.siren", backgroundColor:"#e86d13"
		}
		standardTile("siren", "device.alarm", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"alarm.siren", icon:"st.secondary.siren", backgroundColor:"#e86d13"
		}
		standardTile("empty", "null", decoration: "flat") {
			state "emptySmall", label:'', defaultState: true
		}	
		standardTile("off", "device.alarm", inactiveLabel: false, decoration: "flat") {
			state "default", label:'', action:"alarm.off", icon:"st.secondary.off", backgroundColor:"#00a0dc"
		}
		main "alarm"
		details(["alarm","off","empty","siren"])
	}
}

def installed() {
	log.trace "Executing 'installed'"
	initialize()
}

def updated() {
	log.trace "Executing 'updated'"
	initialize()
}

private initialize() {
	log.trace "Executing 'initialize'"

	sendEvent(name: "DeviceWatch-DeviceStatus", value: "online")
	sendEvent(name: "healthStatus", value: "online")
	sendEvent(name: "DeviceWatch-Enroll", value: [protocol: "cloud", scheme:"untracked"].encodeAsJson(), displayed: false)
}

def siren() {
	sendEvent(name: "alarm", value: "siren")
}

def off() {
	sendEvent(name: "alarm", value: "off")
}

// Parse incoming device messages to generate events
def parse(String description) {
	def pair = description.split(":")
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}