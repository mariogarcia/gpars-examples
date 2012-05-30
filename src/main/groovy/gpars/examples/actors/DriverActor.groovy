package gpars.examples.actors

import groovyx.gpars.actor.DefaultActor

import static java.lang.Thread.sleep
import static java.lang.Math.random

/**
 * Once the driver starts sends a message to the race steward, telling him
 * he has already racing. Then every second the driver sends a message to the 
 * steward to tell him his position. He does that until the steward sends the 
 * message "finish" when the driver stops driving.
**/
class DriverActor extends DefaultActor{

	def raceSteward
	def driverInfo

	def void act(){
		loop{
			react{raceMessage->
				if(raceMessage == "finish"){
					stop()
				}else {
					driverInfo.distance += random()
					reply driverInfo	
					sleep(1000)
				}
			}			
		}
	}

 /* Once the actor is initialized it sends its very first position */
	def void afterStart(){
		raceSteward << driverInfo
	}
 /* If something unexpected happens the driver stops */
	def void onException(e){
		stop()
	}

}
