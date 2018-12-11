/**
 *  Button Control Master
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
	definition (name: "Button Control Master", namespace: "mbarone/apps", author: "mbarone", vid:"generic-motion") {
		capability "Sensor"
		capability "Switch"
		capability "Health Check"
	}
 	tiles(scale: 2){
		standardTile("Main", "device.Main", inactiveLabel: false, width: 6, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'', icon:"st.Office.office7"
        }
		childDeviceTiles("All")
		main(["Main"])
		details(["Main","All"])
	}
}


def installed() {
	initialize()
}

def updated() {
    initialize()
}

def initialize() {

	state.counter = state.counter ? state.counter + 1 : 1
    if (state.counter == 1) {
		def thisname = "voiceEnabled"
		def thislabel = "Settings - Voice Enabled"
        addChildDevice(
                "Button Control Child",
                "${device.deviceNetworkId}.${thisname}",
                null,
                [
					completedSetup: true,
					label: "${thislabel}", 
					isComponent: false, 
					componentName: "${thisname}", 
					componentLabel: "${thislabel}"
				]
			)
		/*
		
		Button Control Child for quick exit delays
		
		
        addChildDevice(
                "Simulated Refrigerator Door",
                "${device.deviceNetworkId}.2",
                null,
                [completedSetup: true, label: "${device.label} (Main Door)", componentName: "mainDoor", componentLabel: "Main Door"])

        addChildDevice(
                "Simulated Refrigerator Temperature Control",
                "${device.deviceNetworkId}.3",
                null,
                [completedSetup: true, label: "${device.label} (Freezer)", componentName: "freezer", componentLabel: "Freezer"])

        addChildDevice(
                "Simulated Refrigerator Temperature Control",
                "${device.deviceNetworkId}.3",
                null,
                [completedSetup: true, label: "${device.label} (Fridge)", componentName: "refrigerator", componentLabel: "Fridge"])
		*/
	}


    sendEvent(name: "DeviceWatch-DeviceStatus", value: "online")
    sendEvent(name: "healthStatus", value: "online")
    sendEvent(name: "DeviceWatch-Enroll", value: [protocol: "cloud", scheme:"untracked"].encodeAsJson(), displayed: false)
}

/*
def parse(String description) {
    log.debug "parse(${description}) called"
	def parts = description.split(" ")
    def name  = parts.length>0?parts[0].trim():null
    def value = parts.length>1?parts[1].trim():null
    if (name && value) {
        // Update device
        sendEvent(name: name, value: value)
        // Update lastUpdated date and time
        def nowDay = new Date().format("MMM dd", location.timeZone)
        def nowTime = new Date().format("h:mm a", location.timeZone)
        //sendEvent(name: "lastUpdated", value: nowDay + " at " + nowTime, displayed: false)
    }
    else {
    	log.debug "Missing either name or value.  Cannot parse!"
    }
}
*/