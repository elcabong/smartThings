/**
 *  Button Control Child
 *
 *  Copyright 2018 Michael Barone
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
 *  Change History:
 *
 *    Date        Who            What
 *    ----        ---            ----
 *    2018-11-28  mbarone		 orig commit
 *
 * 
 */
metadata {
	definition (name: "Button Control Child", namespace: "mbarone/apps", author: "mbarone", vid:"generic-motion") {
		capability "Actuator"
		capability "Sensor"
		capability "Switch"
		capability "Health Check"
		
		command "sendData"
		
	}
	simulator {
		status "on": "switch:on"
		status "off": "switch:off"
	}
	tiles(scale: 2) {
		multiAttributeTile(name:"switch", type: "lighting", width: 3, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState:"on"
				attributeState "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00A0DC", nextState:"off"
			}
 			tileAttribute("device.lastUpdated", key: "SECONDARY_CONTROL") {
    				attributeState("default", label:'    Last updated ${currentValue}',icon: "st.Health & Wellness.health9")
            }
		}
	}	
	
}

def installed() {
    initialize()
}

def updated() {
    initialize()
}

def initialize() {
    sendEvent(name: "DeviceWatch-DeviceStatus", value: "online")
    sendEvent(name: "healthStatus", value: "online")
    sendEvent(name: "DeviceWatch-Enroll", value: [protocol: "cloud", scheme:"untracked"].encodeAsJson(), displayed: false)
}

def on() {
    sendData("on")
	sendEvent(name: "switch", value: "on")
    def nowDay = new Date().format("MMM dd", location.timeZone)
    def nowTime = new Date().format("h:mm a", location.timeZone)
    sendEvent(name: "lastUpdated", value: nowDay + " at " + nowTime, displayed: false)
}

def off() {
    sendData("off")
	sendEvent(name: "switch", value: "off")
    def nowDay = new Date().format("MMM dd", location.timeZone)
    def nowTime = new Date().format("h:mm a", location.timeZone)
    sendEvent(name: "lastUpdated", value: nowDay + " at " + nowTime, displayed: false)	
}

def sendData(String value) {
    def name = device.displayName
	//log.debug "sendData (${name}  ${value}) called"
    parent.sendEvent(name:"${name}", value: "${value}")
}