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
 *
 *
 *	To be used with the following webCoRE piston:
 *	 import code  -    piston name
 *       us47     -   "Setting Button Control"
 *
 *	See discussion thread:
 *		https://community.smartthings.com/t/release-custom-dth-and-webcore-pistons-for-grouping-like-sensors-and-giving-1-aggregated-status-for-the-group/134270
 *
 *
 *
 */
metadata {
 	definition (name: "webCoRE Value Tiles - Button Group Master", namespace: "mbarone/apps", author: "mbarone", vid:"generic-switch") {
		capability "Sensor"
		capability "Switch"
		capability "Health Check"
		
		attribute "Child","string"
		attribute "Details","string"
		
		command "changeChildValue"
		command "On"
		command "Off"
		command "removeChildren"
    }
	
 	tiles(scale: 2){
		standardTile("Main", "device.Main", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'', icon:"st.Office.office7"
        }

        standardTile("Details", "device.Details", width: 5, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'${currentValue}'
        }
		
        standardTile("Refresh", "device.switch", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "off", action:"On", label: "Refresh", icon:"st.secondary.refresh"
            state "on", action:"Off", label: "Refreshing", icon:"st.motion.motion.active"
        }
		standardTile("removeChildren", "device.removeChildren", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true){
			state "default", label:'Remove Children', action:"removeChildren"
		}		
		standardTile("emptyS", "null", decoration: "flat", width: 2, height: 1) {
			state "emptySmall", label:'', defaultState: true
		}	
		
		
		childDeviceTiles("All")
		main(["Main"])
		details(["All","Details","Refresh","removeChildren"])
	}
 }
 def parse(String description){
 	def pair = description.split(":")
    createEvent(name: pair[0].trim(), value: pair[1].trim())
 }
 def changeChildValue (title, param) {
    sendEvent(name:"Details", value:"", displayed: false)
	def childDevice = null
	def name = title
	def value = param
	def deviceType = "switch"
	try {
		childDevices.each {
			try{
				//log.debug "1-Looking for child with deviceNetworkID = ${device.deviceNetworkId}-${name} against ${it.deviceNetworkId}"
				if (it.deviceNetworkId == "${device.deviceNetworkId}-${name}") {
					childDevice = it
					//log.debug "Found a match 1!!!"
				}
			}
			catch (e) {
				log.debug e
			}
		}	
		if (childDevice == null) {
			//log.debug "isChild = true, but no child found - Auto Add it!"
			//log.debug "    Need a ${name}"
		
			createChildDevice(name)
			//find child again, since it should now exist!
			childDevices.each {
				try{
					//log.debug "2-Looking for child with deviceNetworkID = ${device.deviceNetworkId}-${name} against ${it.deviceNetworkId}"
					if (it.deviceNetworkId == "${device.deviceNetworkId}-${name}") {
						childDevice = it
						//log.debug "Found a match 2!!!"
					}
				}
				catch (e) {
					log.debug e
				}
			}
		}
		if (childDevice != null) {
            //log.debug "parse() found child device ${childDevice.deviceNetworkId}"
			//log.debug "sending parse(${deviceType} ${value})"
            childDevice.parse("${deviceType} ${value}")
			//log.debug "${childDevice.deviceNetworkId} - name: ${name} (switch), value: ${value}"
		}
	}
	catch (e) {
        log.error "Error in parse() routine, error = ${e}"
	}
 }
 private void createChildDevice(String deviceName) {
	//log.trace "createChildDevice:  Creating Child Device '${device.displayName} (${deviceName})'"
	try {
		def deviceHandlerName = "webCoRE Value Tiles - Button Group Child"
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
        sendEvent(name:"Details", value:"Child device created!  May take some time to display above.")
	}
	catch (e) {
        log.error "Child device creation failed with error = ${e}"
        state.alertMessage = "Child device creation failed. Please make sure that the '${deviceHandlerName}' is installed and published."
	}
 }
 def removeChildren(){
	log.trace "removing any child devices"
	childDevices.each {
		try{
			log.trace "removing ${it.deviceNetworkId}"
			deleteChildDevice(it.deviceNetworkId)
		}
		catch (e) {
			log.debug "Error deleting ${it.deviceNetworkId}: ${e}"
		}
	}
	sendEvent(name:"Details", value:"Child devices removed!  May take some time to remove device.  Refresh when ready to re-build child devices, or wait until a device changes state.")
 }

 def On(){
 	sendEvent(name: "switch", value: "on")
 }
 def Off(){
 	sendEvent(name: "switch", value: "off")
 }