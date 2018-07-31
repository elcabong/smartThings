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
 	definition (name: "webCoRE Value Ties - Contact Sensor Group", namespace: "mbarone/apps", author: "mbarone") {
    capability "Contact Sensor"
	capability "Sensor"  	
 	capability "Health Check"

    
    attribute "Main","string"
    attribute "Details","string"
    attribute "Value0","string"
    attribute "Value1","string"
    attribute "Value2","string"
    attribute "Value3","string"
    attribute "Value4","string"
    attribute "Value5","string"
    attribute "Value6","string"
    attribute "Value7","string"
    attribute "Value8","string"
    attribute "Value9","string"
    attribute "Title0","string"
    attribute "Title1","string"
    attribute "Title2","string"
    attribute "Title3","string"
    attribute "Title4","string"
    attribute "Title5","string"
    attribute "Title6","string"
    attribute "Title7","string"
    attribute "Title8","string"
    attribute "Title9","string"   
    
    command "changeMain"
    command "changeDetails"
    command "changeValue"
    command "open"
    command "close"
    }
 	tiles(scale: 2){		
    	valueTile("Main", "device.Main", width: 6, height: 4) {
    		state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
		}		
        standardTile("Refresh", "device.switch", inactiveLabel: false, width: 2, height: 1, decoration: "flat", wordWrap: true) {
            state "off", action:"On", label: "Refresh", icon:"st.secondary.refresh"
            state "on", label: "Refreshing", icon:"st.motion.motion.active"
        }
        standardTile("Details", "device.Details", inactiveLabel: false, width: 4, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'${currentValue}'
        }		
        valueTile("image", "image", width: 1, height: 1) {
 			state("default", label:'', icon: "st.alarm.temperature.normal")
 		}
        valueTile("Value0", "device.Value0", width: 1, height: 1) {
 			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
 		}		
 		valueTile("Value1", "device.Value1", width: 1, height: 1) {
 			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
 		}
 		valueTile("Value2", "device.Value2", width: 1, height: 1) {
 			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
 		}
 		valueTile("Value3", "device.Value3", width: 1, height: 1) {
 			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
 		}
 		valueTile("Value4", "device.Value4", width: 1, height: 1) {
 			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
 		}
 		valueTile("Value5", "device.Value5", width: 1, height: 1) {
 			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
 		}
        valueTile("Value6", "device.Value6", width: 1, height: 1) {
 			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
 		}
        valueTile("Value7", "device.Value7", width: 1, height: 1) {
 			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
 		}
        valueTile("Value8", "device.Value8", width: 1, height: 1) {
 			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
 		}
        valueTile("Value9", "device.Value9", width: 1, height: 1) {
 			state("closed", label:'${name}', icon:"st.contact.contact.closed", backgroundColor:"#00A0DC")
			state("open", label:'${name}', icon:"st.contact.contact.open", backgroundColor:"#e86d13")
 		}
        valueTile("Title0", "device.Title0", width: 5, height: 1) {
 			state( "default", label:'${currentValue}')
 		}
        valueTile("Title1", "device.Title1", width: 5, height: 1) {
 			state( "default", label:'${currentValue}')
 		}
        valueTile("Title2", "device.Title2", width: 5, height: 1) {
 			state( "default", label:'${currentValue}')
 		}
        valueTile("Title3", "device.Title3", width: 5, height: 1) {
 			state( "default", label:'${currentValue}')
 		}
        valueTile("Title4", "device.Title4", width: 5, height: 1) {
 			state( "default", label:'${currentValue}')
 		}
        valueTile("Title5", "device.Title5", width: 5, height: 1) {
 			state( "default", label:'${currentValue}')
 		}
        valueTile("Title6", "device.Title6", width: 5, height: 1) {
 			state( "default", label:'${currentValue}')
 		}
        valueTile("Title7", "device.Title7", width: 5, height: 1) {
 			state( "default", label:'${currentValue}')
 		}
        valueTile("Title8", "device.Title8", width: 5, height: 1) {
 			state( "default", label:'${currentValue}')
 		}
        valueTile("Title9", "device.Title9", width: 5, height: 1) {
 			state( "default", label:'${currentValue}')
 		}
 		main(["Main"])
 		details(["Main","Refresh","Details","Title0","Value0","Title1","Value1","Title2","Value2","Title3","Value3","Title4","Value4","Title5","Value5","Title6","Value6","Title7","Value7","Title8","Value8","Title9","Value9"])
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
 def open() {
	log.trace "open()"
	sendEvent(name: "contact", value: "open")
}

def close() {
	log.trace "close()"
    sendEvent(name: "contact", value: "closed")
}