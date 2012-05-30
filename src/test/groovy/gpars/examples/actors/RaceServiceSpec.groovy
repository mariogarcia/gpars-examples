package gpars.examples.actors

import spock.lang.Specification

class RaceServiceSpec extends Specification{

	def "3 drivers just one winner"(){
		given: "3 drivers ready for one race"
			def race = new Race(distance:10.234,name:'Loco',drivers:[
				new Driver(name:'Fernando Alonso',number:5),
				new Driver(name:'Sebastian Vettel',number:1),
				new Driver(name:'Louis Hamilton',number:3)
			])
		when:
			def raceResult = new RaceService().go(race)	
		then:
			raceResult
	}
}
