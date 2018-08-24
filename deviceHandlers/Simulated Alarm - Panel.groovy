/**
 *  Simulated Alarm - Panel
 *
 *	This panel works with a webCoRE piston to mirror the state of SHM and allow changing of the SHM status.
 *	This shows the current status in the favorites area, and when clicking it goes to the details page of this device to change the SHM status.
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
	definition (name: "Simulated Alarm - Panel", namespace: "mbarone/apps", author: "mbarone") {
		capability "Alarm"
		capability "Sensor"
		capability "Actuator"
		capability "Health Check"
		
		command "DISARMED"
		command "ARMEDStay"	
		command "ARMEDAway"	
	}

	simulator {
		// reply messages
		["ARMEDAway","ARMEDStay","DISARMED"].each {
			reply "$it": "alarm:$it"
		}
	}

	tiles(scale: 2) {
		standardTile("alarm", "device.alarm", width: 6, height: 4) {
			state "Default", label:'DISARMED1', icon:'st.security.alarm.off', backgroundColor:"#41C710"
			state "DISARMED", label:'DISARMED', icon:'st.security.alarm.off', backgroundColor:"#41C710"
			state "ARMEDStay", label:'ARMED (Stay)', icon:"st.Home.home4", backgroundColor:"#00a0dc"
			state "ARMEDAway", label:'ARMED (Away)', icon:"st.security.alarm.on", backgroundColor:"#00a0dc"
			state "siren", label:'ALERT', icon:"st.secondary.siren", backgroundColor:"#e86d13"
		}
		standardTile("DISARMED", "device.alarm", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'DISARM', action:"DISARMED", icon:"st.security.alarm.off", backgroundColor:"#41C710"
		}
		standardTile("ARMEDStay", "device.alarm", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'Set ARMED (Stay)', action:"ARMEDStay", icon:"st.Home.home4", backgroundColor:"#00a0dc"
		}
		standardTile("ARMEDAway", "device.alarm", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'Set ARMED (Away)', action:"ARMEDAway", icon:"st.security.alarm.on", backgroundColor:"#00a0dc"
		}
		standardTile("siren", "device.alarm", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'', action:"alarm.siren", icon:"st.secondary.siren", backgroundColor:"#e86d13"
		}
		standardTile("empty", "null", decoration: "flat", width: 2, height: 2) {
			state "emptySmall", label:'', defaultState: true
		}
		standardTile("emptyS", "null", decoration: "flat", width: 2, height: 1) {
			state "emptySmall", label:'', defaultState: true
		}
		standardTile("off", "device.alarm", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'', action:"alarm.off", icon:"st.secondary.off", backgroundColor:"#00a0dc"
		}
		main "alarm"
		details(["alarm","ARMEDAway","ARMEDStay","DISARMED","emptyS","emptyS","emptyS","siren","empty","off"])
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


def ARMEDStay() {
	log.trace "Executing 'ARMEDStay'"
	sendEvent(name: "alarm", value: "ARMEDStay")
}

def ARMEDAway() {
	log.trace "Executing 'ARMEDAway'"
	sendEvent(name: "alarm", value: "ARMEDAway")
}

def DISARMED() {
	log.trace "Executing 'DISARMED'"
	sendEvent(name: "alarm", value: "DISARMED")
}

def siren() {
	sendEvent(name: "alarm", value: "siren")
}

def both() {
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