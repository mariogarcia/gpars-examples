package gpars.examples.actors

import spock.lang.Specification

/**
 * This test launches a races with three different pilots. At the end of
 * the race there must be one winner, but could be anyone.
*/
class RaceServiceSpec extends Specification{

	def "3 drivers just one winner"(){
		given: "3 drivers ready for one race"
			def race = new Race(distance:10.234,name:'Loco',drivers:[
				new Driver(name:'Fernando Alonso',number:5),
				new Driver(name:'Sebastian Vettel',number:1),
				new Driver(name:'Louis Hamilton',number:3)
			])
		when: "The races starts"
			def raceResult = new RaceService().go(race)	
		then: "At the end there must be a winner"
			raceResult
	}
}
