/**
 *  HTTP Ping Local Server
 *
 *  Copyright 2014 Kristopher Kubicki
 *  updated by michaelbarone 2018
 *  https://github.com/michaelbarone/smartThings/tree/master/deviceHandlers
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
	definition (name: "Ping Local Server", namespace: "mbarone", author: "mbarone", vid:"generic-switch") {
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
		capability "Switch"		
		capability "Health Check"
		
		attribute "Child","string"
		attribute "Details","string"
		
		command "changeChildValue"
		command "removeChildren"		
	}

	preferences {
		input("dest_ip", "text", title: "Server IP", description: "The server with ping.php", required: true, displayDuringSetup: true)
		input("dest_port", "number", title: "Server Port", description: "The port of the webserver (default 80)", required: true, displayDuringSetup: true)
		input("ip_addresses", "text", title: "IP Addresses", description: "IP for each machine you want status updates for.  Comma separated if multiple IPs are set.  IE:  192.168.1.50,192.168.1.20", required: true, displayDuringSetup: true)
	}
	
	simulator {
		// TODO: define status and reply messages here
	}

	tiles(scale: 2) {
			standardTile("main", "device.contact", width: 2, height: 1, canChangeIcon: false, canChangeBackground: true) {
				state "default", label:'', icon:"st.Electronics.electronics18"
			}
			standardTile("refresh", "device.poll", width: 2, height: 1, inactiveLabel: false, decoration: "flat") {
				state "default", action:"polling.poll", icon:"st.secondary.refresh"
			}
			standardTile("removeChildren", "device.removeChildren", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true){
				state "default", label:'Remove Children', action:"removeChildren"
			}
			standardTile("Details", "device.Details", width: 4, height: 1, decoration: "flat", wordWrap: true) {
				state "default", label:'${currentValue}'
			}
			standardTile("emptyS", "null", decoration: "flat", width: 1, height: 1) {
				state "emptySmall", label:'', defaultState: true
			}
			
			childDeviceTiles("All")
			main "main"
			details(["All", "emptyS", "Details", "emptyS", "refresh", "removeChildren"])
		}
	}


def parse(description) {
    log.debug "parse starting"
    def msg = parseLanMessage(description)
	//log.debug msg.json
	//def cmds = []
	//cmds << "delay 1000"
	
	msg.json.each {
		//log.debug "item: $it.ip is $it.status"
		changeChildValue(it.ip, it.status)
		//cmds
	}
}
	


def poll() {
	def nowDay = new Date().format("MMM dd", location.timeZone)
	def nowTime = new Date().format("h:mm a", location.timeZone)
	sendEvent(name:"Details", value:"Poll Started "+ nowDay + " at " + nowTime, displayed: true)
	log.debug "poll starting"
    def hosthex = convertIPtoHex(dest_ip)
    def porthex = convertPortToHex(dest_port)
    if (porthex.length() < 4) { porthex = "00" + porthex }
    device.deviceNetworkId = "$hosthex:$porthex" 
	log.debug "device.deviceNetworkId is " + device.deviceNetworkId
    
    def hubAction = new physicalgraph.device.HubAction(
   	 		'method': "GET",
    		'path': "/ping/?ip="+ip_addresses,
			'headers': [
					HOST: dest_ip+":"+dest_port
				]
		)
    log.debug "hubaction is: " + hubAction
    
    hubAction
	sendHubCommand(hubAction)
}



private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02X', it.toInteger() ) }.join()
    return hex
}

private String convertPortToHex(port) {
	String hexport = port.toString().format( '%04X', port.toInteger() )
    return hexport
}	


 def changeChildValue(title, param) {
	log.debug "changeChildValue: "+title+" is "+param
	def childDevice = null
	def name = title
	def value = param
	def deviceType = "switch"
	log.debug "${device.deviceNetworkId}-${name}"
	try {
		log.debug "about to try changeChildValue1"
		if(childDevices){
			childDevices.each {
				log.debug "childDevices Each"
				try{
					//log.debug "1-Looking for child with deviceNetworkID = ${device.deviceNetworkId}-${name} against ${it.deviceNetworkId}"
					if (it.deviceNetworkId == "${device.deviceNetworkId}-${name}") {
						childDevice = it
						log.debug "Found a match 1!!!"
					}
				}
				catch (e) {
					log.debug e
				}
			}
		}
	}
	catch (e) {
        log.error "Error in changeChildValue() routine1, error = ${e}"
	}
	try {
		if (childDevice == null) {
			log.debug "isChild = true, but no child found - Auto Add it!"
			log.debug "    Need a ${name}"
		
			createChildDevice(name)
			//find child again, since it should now exist!
			childDevices.each {
				try{
					//log.debug "2-Looking for child with deviceNetworkID = ${device.deviceNetworkId}-${name} against ${it.deviceNetworkId}"
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
            //log.debug "parse() found child device ${childDevice.deviceNetworkId}"
			//log.debug "sending parse(${deviceType} ${value})"
            childDevice.parse("${deviceType} ${value}")
			//log.debug "${childDevice.deviceNetworkId} - name: ${name} (switch), value: ${value}"
		}
	}
	catch (e) {
        log.error "Error in changeChildValue() routine2, error = ${e}"
	}
 }
 
 private void createChildDevice(String deviceName) {
	//log.trace "createChildDevice:  Creating Child Device '${device.displayName} (${deviceName})'"
	try {
		def deviceHandlerName = "Ping Local Server Child"
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
        sendEvent(name:"Details", value:"Child device created!  May take some time to display.")
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
