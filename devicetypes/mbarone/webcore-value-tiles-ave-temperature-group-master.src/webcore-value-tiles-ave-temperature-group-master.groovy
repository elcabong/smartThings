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
 *
 *
 *	To be used with the following webCoRE pistons:
 *	 import code  -    piston name
 *      hi0z7     - "Average Temperatures"
 *
 *	See discussion thread:
 *		https://community.smartthings.com/t/release-custom-dth-and-webcore-pistons-for-grouping-like-sensors-and-giving-1-aggregated-status-for-the-group/134270
 *
 *
 *
 */
metadata {
 	definition (name: "webCoRE Value Tiles Ave Temperature Group Master", namespace: "mbarone", author: "mbarone", ocfDeviceType: "oic.d.thermostat", mcdSync: true, mnmn: "SmartThings") {
		capability "Temperature Measurement"
		capability "Sensor"
		capability "Switch"
		capability "Health Check"
		
		attribute "Details","string"
		
		command "changeMain"
		command "changeChildValue"
		command "On"
		command "Off"
		command "removeChildren"
    }
	
 	tiles(scale: 2){
		multiAttributeTile(name:"temperature", type: "generic", width: 6, height: 4) {
			tileAttribute ("device.temperature", key: "PRIMARY_CONTROL") {
				attributeState("temperature", label:'${currentValue}°', icon: "st.alarm.temperature.normal", unit:"F",
					backgroundColors:[
						[value: 31, color: "#153591"],
						[value: 44, color: "#1e9cbb"],
						[value: 59, color: "#90d2a7"],
						[value: 74, color: "#44b621"],
						[value: 84, color: "#f1d801"],
						[value: 95, color: "#d04e00"],
						[value: 96, color: "#bc2323"]
					]
				)
			}
		}		
        standardTile("Refresh", "device.switch", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "off", action:"On", label: "Refresh", icon:"st.secondary.refresh"
            state "on", action:"Off", label: "Refreshing", icon:"st.motion.motion.active"
        }
        standardTile("Details", "device.Details", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'${currentValue}'
        }
		standardTile("removeChildren", "device.removeChildren", inactiveLabel: false, width: 6, height: 1, decoration: "flat", wordWrap: true){
			state "default", label:'Remove Children', action:"removeChildren"
		}
		childDeviceTiles("All")
		main(["temperature"])
		details(["temperature","Refresh","Details","All","removeChildren"])
	}
 }
 def parse(String description){
 	def pair = description.split(":")
    createEvent(name: pair[0].trim(), value: pair[1].trim(), unit:"F")
 }
 def changeMain (param, details){
	sendEvent("name":"temperature", "value": param, unit:"F")
	sendEvent("name":"Details", "value":details)
 }
 def changeChildValue (title, param) {
	def childDevice = null
	def name = title
	def value = param
	def deviceType = "temperature"
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
			log.debug "${childDevice.deviceNetworkId} - name: ${name} (temperature), value: ${value}"
		}
	}
	catch (e) {
        log.error "Error in parse() routine, error = ${e}"
	}
 }
 private void createChildDevice(String deviceName) {
	log.trace "createChildDevice:  Creating Child Device '${device.displayName} (${deviceName})'"
	try {
		def deviceHandlerName = "webCoRE Value Tiles Ave Temperature Group Child"
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
 def removeChildren(){
	log.trace "starting: removing any child devices"
	childDevices.each {
		try{
			log.trace "removing ${it.deviceNetworkId}"
			deleteChildDevice(it.deviceNetworkId)
		}
		catch (e) {
			log.debug "Error deleting ${it.deviceNetworkId}: ${e}"
		}
	}
	sendEvent("name":"Details", "value":"Child devices removed!  May take some time to update below.  Refresh when ready to re-build child devices, or wait until a device changes state.")
 }
 def On(){
 	sendEvent(name: "switch", value: "on")
 }
 def Off(){
 	sendEvent(name: "switch", value: "off")
 }
 def on(){
    sendEvent(name: "switch", value: "on")
 }
 def off(){
    sendEvent(name: "switch", value: "off")
 }