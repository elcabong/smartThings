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
 	definition (name: "The Lady", namespace: "mbarone/apps", author: "mbarone", vid: "generic-button") {
		capability "Actuator"
		capability "Switch"
		capability "Momentary"
		capability "Health Check"

		command "pushed"
    }
	
	
    preferences {
        input name: "GCNb", type: "bool", title: "Global Child Name(s)", description: "Enable Global Child Name", required: false
        input name: "GCN", type: "text", title: "Child Name(s)", description: "Child name or names, comma separated as will be read aloud.", required: false
		
		
		
		(1..4).each() { n ->
			section("Custom Lady Message ${n}") {
				input "name_${n}", type: "text", title:"Name of the message", multiple:true, required:false
				input "message_${n}", type: "text", title:"Custom message", multiple:true, required:false
			}
		}
		/*
        input name: "number", type: "number", title: "Number", description: "Enter number", required: true
        input name: "bool", type: "bool", title: "Bool", description: "Enter boolean", required: true
        input name: "password", type: "password", title: "password", description: "Enter password", required: true
        input name: "phone", type: "phone", title: "phone", description: "Enter phone", required: true
        input name: "decimal", type: "decimal", title: "decimal", description: "Enter decimal", required: true
        input name: "time", type: "time", title: "time", description: "Enter time", required: true
        input name: "options", type: "enum", title: "enum", options: ["Option 1", "Option 2"], description: "Enter enum", required: true
		*/
    }	
	
	
	
 	tiles(scale: 2){
        standardTile("Main", "device.Main", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'', icon:"st.Office.office7"
        }
		
        valueTile("GotMail-Title", "device.Title", width: 2, height: 1) {
 			state( "default", label:'Got Mail')
 		}
        standardTile("GotMail-LastUpdated", "device.GotMail-LastUpdated", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Last Completed: \n ${currentValue}'
        }		
        standardTile("GotMail-Button", "device.GotMail-Button", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
				state("off", label: 'Done', action: "GotMail", backgroundColor: "#ffffff", nextState: "on")
				state("on", label: 'Done', backgroundColor: "#00a0dc")
        }	
	
        valueTile("WaterFrontyard-Title", "device.Title", width: 2, height: 1) {
 			state( "default", label:'Water Frontyard')
 		}
        standardTile("WaterFrontyard-LastUpdated", "device.WaterFrontyard-LastUpdated", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Last Completed: \n ${currentValue}'
        }		
        standardTile("WaterFrontyard-Button", "device.WaterFrontyard-Button", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
				state("off", label: 'Done', action: "WaterFrontyard", backgroundColor: "#ffffff", nextState: "on")
				state("on", label: 'Done', backgroundColor: "#00a0dc")
        }
		
        valueTile("WaterBackyard-Title", "device.Title", width: 2, height: 1) {
 			state( "default", label:'Water Backyard')
 		}
        standardTile("WaterBackyard-LastUpdated", "device.WaterBackyard-LastUpdated", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Last Completed: \n ${currentValue}'
        }		
        standardTile("WaterBackyard-Button", "device.WaterBackyard-Button", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
				state("off", label: 'Done', action: "WaterBackyard", backgroundColor: "#ffffff", nextState: "on")
				state("on", label: 'Done', backgroundColor: "#00a0dc")
        }	

        valueTile("WaterInside-Title", "device.Title", width: 2, height: 1) {
 			state( "default", label:'Water Inside')
 		}
        standardTile("WaterInside-LastUpdated", "device.WaterInside-LastUpdated", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Last Completed: \n ${currentValue}'
        }		
        standardTile("WaterInside-Button", "device.WaterInside-Button", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
				state("off", label: 'Done', action: "WaterInside", backgroundColor: "#ffffff", nextState: "on")
				state("on", label: 'Done', backgroundColor: "#00a0dc")
        }
		
        valueTile("TrimCatNailsH-Title", "device.Title", width: 2, height: 1) {
 			state( "default", label:'Trim Cat Nails H')
 		}
        standardTile("TrimCatNailsH-LastUpdated", "device.TrimCatNailsH-LastUpdated", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Last Completed: \n ${currentValue}'
        }		
        standardTile("TrimCatNailsH-Button", "device.TrimCatNailsH-Button", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
				state("off", label: 'Done', action: "TrimCatNailsH", backgroundColor: "#ffffff", nextState: "on")
				state("on", label: 'Done', backgroundColor: "#00a0dc")
        }		
		
        valueTile("TrimCatNailsT-Title", "device.Title", width: 2, height: 1) {
 			state( "default", label:'Trim Cat Nails T')
 		}
        standardTile("TrimCatNailsT-LastUpdated", "device.TrimCatNailsT-LastUpdated", inactiveLabel: false, width: 3, height: 1, decoration: "flat", wordWrap: true) {
            state "default", label:'Last Completed: \n ${currentValue}'
        }		
        standardTile("TrimCatNailsT-Button", "device.TrimCatNailsT-Button", inactiveLabel: false, width: 1, height: 1, decoration: "flat", wordWrap: true) {
				state("off", label: 'Done', action: "TrimCatNailsT", backgroundColor: "#ffffff", nextState: "on")
				state("on", label: 'Done', backgroundColor: "#00a0dc")
        }		
		
		main(["Main"])
		
		details(["GotMail-Title","GotMail-LastUpdated","GotMail-Button"
				,"WaterFrontyard-Title","WaterFrontyard-LastUpdated","WaterFrontyard-Button"
				,"WaterBackyard-Title","WaterBackyard-LastUpdated","WaterBackyard-Button"
				,"WaterInside-Title","WaterInside-LastUpdated","WaterInside-Button"
				,"TrimCatNailsH-Title","TrimCatNailsH-LastUpdated","TrimCatNailsH-Button"
				,"TrimCatNailsT-Title","TrimCatNailsT-LastUpdated","TrimCatNailsT-Button"
				])
	}
 }
 def parse(String description){
 	def pair = description.split(":")
    createEvent(name: pair[0].trim(), value: pair[1].trim(), unit:"F")
 }
 
 def pushed(thisname) {
	log.trace "device pushed "+thisname
	sendEvent("name": thisname+"-Button", value: "on", isStateChange: true, displayed: false)
	sendEvent("name": thisname+"-Button", value: "off", isStateChange: true, displayed: false)
	def nowDay = new Date().format("MMM dd", location.timeZone)
    def nowTime = new Date().format("h:mm a", location.timeZone)
    sendEvent("name": thisname+"-LastUpdated", value: nowDay + " at " + nowTime, displayed: true)
}