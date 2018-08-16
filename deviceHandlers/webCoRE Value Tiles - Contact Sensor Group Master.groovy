/**
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
 	definition (name: "webCoRE Value Ties - Contact Sensor Group Master", namespace: "mbarone/apps", author: "mbarone") {
    capability "Contact Sensor"
	capability "Sensor"
	capability "Switch"
 	capability "Health Check"

    
    attribute "Main","string"
    attribute "Details","string"
    
    command "changeMain"
    command "changeDetails"
    command "changeChildValue"
	command "clearValues"
    command "open"
    command "close"
    command "On"
    command "Off"	
    }
	
 	tiles(scale: 2){		
    	valueTile("Main", "device.Main", width: 6, height: 4) {
    		state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
		}		
        standardTile("Refresh", "device.switch", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "off", action:"On", label: "Refresh", icon:"st.secondary.refresh"
            state "on", action:"Off", label: "Refreshing", icon:"st.motion.motion.active"
        }
        standardTile("Details", "device.Details", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'${currentValue}'
        }
 		// main(["Main"])
		// details(["Main","Refresh","Details","Title0","Value0","Title1","Value1","Title2","Value2","Title3","Value3","Title4","Value4","Title5","Value5","Title6","Value6","Title7","Value7","Title8","Value8","Title9","Value9"])
		childDeviceTiles("All")
		main(["Main"])
		details(["Main","Refresh","Details","All"])
	}
 }
 def parse(String description){
 	def pair = description.split(":")
    createEvent(name: pair[0].trim(), value: pair[1].trim(), unit:"F")
 }
 def changeMain (param){
 	sendEvent("name":"Main", "value":param)
 }
 def changeDetails (param){
 	sendEvent("name":"Details", "value":param)
 }
 def changeChildValue (num, title, param) {
 	//sendEvent("name":"Value"+num, "value":param)
    //sendEvent("name":"Title"+num, "value":title)
	
	
	def childDevice = null
	def name = title
	def value = param
	def deviceType = "contact"
	try {
		childDevices.each {
			try{
				log.debug "1-Looking for child with deviceNetworkID = ${device.deviceNetworkId}-${name} against ${it.deviceNetworkId}"
				if (it.deviceNetworkId == "${device.deviceNetworkId}-${name}") {
				childDevice = it
				log.debug "Found a match 1!!!"
				}
			}
			catch (e) {
				log.debug e
			}
		}	
	
	
		if (childDevice == null) {
			def namenum = num
			log.debug "isChild = true, but no child found - Auto Add it!"
			log.debug "    Need a ${name}"
		
			createChildDevice(name)
			//find child again, since it should now exist!
			childDevices.each {
				try{
					log.debug "2-Looking for child with deviceNetworkID = ${device.deviceNetworkId}-${name} against ${it.deviceNetworkId}"
					if (it.deviceNetworkId == "${device.deviceNetworkId}-${name}") {
						childDevice = it
						log.debug "Found a match 2!!!"
					}
				}
				catch (e) {
					log.debug e
				}
			}
		}

		if (childDevice != null) {
            log.debug "parse() found child device ${childDevice.deviceNetworkId}"
			 log.debug "sending parse(${deviceType} ${value})"
            childDevice.parse("${deviceType} ${value}")
			log.debug "${childDevice.deviceNetworkId} - name: ${name} (contact), value: ${value}"
		}
		
	}
	catch (e) {
        log.error "Error in parse() routine, error = ${e}"
	}	
	
 }
 
 
 private void createChildDevice(String deviceName) {
	log.trace "createChildDevice:  Creating Child Device '${device.displayName} (${deviceName})'"
	try {
		def deviceHandlerName = "webCoRE Value Ties - Contact Sensor Group Child"
		addChildDevice(deviceHandlerName,
						"${device.deviceNetworkId}-${deviceName}",
						null,
						[
							completedSetup: true, 
							label: "${device.displayName} (${deviceName})", 
							isComponent: true, 
							componentName: "${deviceName}", 
							componentLabel: "${deviceName}"
						]
					)	
	}
	catch (e) {
        log.error "Child device creation failed with error = ${e}"
        state.alertMessage = "Child device creation failed. Please make sure that the '${deviceHandlerName}' is installed and published."
	}
 }
 
 
 def clearValues (startingAt){
	startingAt = startingAt as Integer
	def myIntRange = startingAt..9
	for(i in myIntRange){
		// sendEvent("name":"Value"+i, "value":"")
		// sendEvent("name":"Title"+i, "value":"")		
	}
 }
 def open() {
	log.trace "open()"
	sendEvent(name: "contact", value: "open")
}

def close() {
	log.trace "close()"
    sendEvent(name: "contact", value: "closed")
}
 def On(){
 	sendEvent(name: "switch", value: "on")
 }
 def Off(){
 	sendEvent(name: "switch", value: "off")
 }