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

preferences {
	input("dest_ip", "text", title: "Server IP", description: "The server with ping.php", required: true, displayDuringSetup: true)
    input("dest_port", "number", title: "Server Port", description: "The port of the webserver (default 80)", required: true, displayDuringSetup: true)
    input("ip_addresses", "text", title: "IP Addresses", description: "IP for each machine you want status updates for.  Comma separated if multiple IPs are set.  IE:  192.168.1.50,192.168.1.20", required: true, displayDuringSetup: true)
}
 

metadata {
	definition (name: "Ping Local Server", namespace: "mbarone/apps", author: "mbarone") {
		capability "Polling"
		capability "Contact Sensor"
		capability "Refresh"
		capability "Health Check"
      
        attribute "ttl", "string"
        attribute "last_request", "number"
        attribute "last_live", "number"
        attribute "last_requestV", "string"
        attribute "last_liveV", "string"
	}

	simulator {
		// TODO: define status and reply messages here
	}

	tiles {
			standardTile("main", "device.contact", width: 2, height: 2, canChangeIcon: false, canChangeBackground: true) {
				state "open", label: 'Online', backgroundColor: "#79b821", icon:"st.Electronics.electronics18"
				state "closed", label: 'Offline', backgroundColor: "#ffa81e", icon:"st.Electronics.electronics18"
			}
			standardTile("refresh", "device.ttl", inactiveLabel: false, decoration: "flat") {
				state "default", action:"polling.poll", icon:"st.secondary.refresh"
			}
			standardTile("ttl", "device.ttl", inactiveLabel: false, decoration: "flat") {
				state "ttl", label:'${currentValue}'
			}
			standardTile("last_requestV", "device.last_requestV", width: 3, height: 1, inactiveLabel: false, decoration: "flat") {
				state "default", label:'Last Request: ${currentValue}'
			}        
			standardTile("last_liveV", "device.last_liveV", width: 3, height: 1, inactiveLabel: false, decoration: "flat") {
				state "default", label:'Last Online: ${currentValue}'
			} 

			main "main"
			details(["main", "refresh", "ttl","last_requestV","last_liveV"])
		}
	}


def parse(description) {
    log.debug "parse starting"
    def msg = parseLanMessage(description)
	log.debug msg.json
	msg.json.each {
		log.debug "item: $it.ip is $it.status"
	}
}
	


def poll() {
	log.debug "poll starting"

    //def hosthex = convertIPToHex(dest_ip)
    //def porthex = Long.toHexString(dest_port)	
    //def hosthex = convertIPtoHex(dest_ip)
    //def porthex = convertPortToHex(dest_port)
	
	
	
    def hosthex = convertIPToHex(dest_ip)
    def porthex = Long.toHexString(dest_port)
    if (porthex.length() < 4) { porthex = "00" + porthex }
	//device.deviceNetworkId = "$hosthex:$porthex" 	
	
	
   // device.deviceNetworkId = "$hosthex:$porthex" 
	log.debug "device.deviceNetworkId is " + device.deviceNetworkId
    
    def hubAction = new physicalgraph.device.HubAction(
   	 		'method': "GET",
    		'path': "/ping/?ip="+ip_addresses,
			'headers': [
					HOST: dest_ip+":"+dest_port
				]
		)
    log.debug "hubaction is: " + hubAction
    
    hubAction // also tried to "return hubAction"
}



private String convertIPtoHex(ipAddress) { 
    String hex = ipAddress.tokenize( '.' ).collect {  String.format( '%02X', it.toInteger() ) }.join()
    return hex
}

private String convertPortToHex(port) {
	String hexport = port.toString().format( '%04X', port.toInteger() )
    return hexport
}	
	
	
private Long convertIntToLong(ipAddress) {
	long result = 0
	def parts = ipAddress.split("\\.")
    for (int i = 3; i >= 0; i--) {
        result |= (Long.parseLong(parts[3 - i]) << (i * 8));
    }

    return result & 0xFFFFFFFF;
}

private String convertIPToHex(ipAddress) {
	log.debug ipAddress
	return Long.toHexString(convertIntToLong(ipAddress));
}
