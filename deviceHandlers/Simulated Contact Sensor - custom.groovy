/**
 *  Copyright 2014 SmartThings
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
 *  Modified by mbarone to remove the click to change functionality of the tiles when used on the favorites screen.  This will
 *  now only show the open/close status like a hardware open close sensor, and not allow you to change the open/close status from the tile.
 */
metadata {
	// Automatically generated. Make future change here.
	definition (name: "Simulated Contact Sensor - custom", namespace: "mbarone/apps", author: "mbarone") {
		capability "Contact Sensor"
		capability "Sensor"
		capability "Health Check"

		command "open"
		command "close"
	}

	simulator {
		status "open": "contact:open"
		status "closed": "contact:closed"
	}

	tiles {
		multiAttributeTile(name:"contact", type: "generic"){
			tileAttribute ("contact", key: "PRIMARY_CONTROL") {
				attributeState("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
				attributeState("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")			
            }
 			tileAttribute("device.lastUpdated", key: "SECONDARY_CONTROL") {
    				attributeState("default", label:'    Last updated ${currentValue}',icon: "st.Health & Wellness.health9")
            }
        }		
		main "contact"
		details "contact"
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

def parse(String description) {
	def pair = description.split(":")
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}

def open() {
	log.trace "open()"
	sendEvent(name: "contact", value: "open")
	setLastUpdated()
}

def close() {
	log.trace "close()"
    sendEvent(name: "contact", value: "closed")
	setLastUpdated()
}

def setLastUpdated() {
	// Update lastUpdated date and time
	def nowDay = new Date().format("MMM dd", location.timeZone)
	def nowTime = new Date().format("h:mm a", location.timeZone)
	sendEvent(name: "lastUpdated", value: nowDay + " at " + nowTime, displayed: false)
}