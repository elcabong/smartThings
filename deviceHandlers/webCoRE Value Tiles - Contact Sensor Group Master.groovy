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
    command "changeValue"
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
 def changeValue (num, title, param) {
 	sendEvent("name":"Value"+num, "value":param)
    sendEvent("name":"Title"+num, "value":title)
 }
 def clearValues (startingAt){
	startingAt = startingAt as Integer
	def myIntRange = startingAt..9
	for(i in myIntRange){
		sendEvent("name":"Value"+i, "value":"")
		sendEvent("name":"Title"+i, "value":"")		
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