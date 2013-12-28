package gpars.examples.actors.race

import groovy.util.logging.Log4j
import static groovyx.gpars.actor.Actors.actor

/**
 * This class creates two type of actors, the one using only the DSL (racingSteward) and
 * the drivers using inheritance (from DefaultActor).
**/
@Log4j
class RaceService{

	def go(final race){
		def racingSteward = actor{
			loop{
				react{driverInfo->
					if(driverInfo.distance >= race.distance){
						log.debug "Race has finished, ${driverInfo.driver.name} has won, congratulations"
						reply "finish"
						stop()
					} else {
						log.debug "${driverInfo.driver.name} is at ${driverInfo.distance}"
						reply "continue"
					}
				}
			}
		}
		def transformInDriverInfo = {new DriverInfo(driver:it)}
		def transformInActor = {new DriverActor(driverInfo:it,raceSteward:racingSteward).start()}
		def drivers = race.drivers.collect(transformInDriverInfo).collect(transformInActor)

		drivers*.join()
		drivers
	}

	static void main(args){
		def race = new Race(distance:10.234,name:'Loco',drivers:[
			new Driver(name:'Fernando Alonso',number:5),
			new Driver(name:'Sebastian Vettel',number:1),
			new Driver(name:'Louis Hamilton',number:3)
		])
		new RaceService().go(race)
	}
}
