package gpars.examples.combine

import spock.lang.Specification
import java.math.MathContext

class LapSpec extends Specification{

	def lapService

	def setup(){
		lapService = new LapService()
	}

	def "Getting best lap of every driver"(){
		given: "All qualifying laps"
			def bestLaps = lapService.findAllBestPartialsByDriver()
		expect: "Retrieving best laps"
			def bestLapOf = bestLaps.find{it.driver == driver}
			bestLapOf.lapNumber == lap
			bestLapOf.partial1 == p1
			bestLapOf.partial2 == p2
			bestLapOf.partial3 == p3
			bestLapOf.partial4 == p4
		where: "There should be only one per driver"
			 driver	|	lap	|	p1	|	p2	|	p3	|	p4
			'001VET'|	0	|14.123	|21.234	|20.130	|18.226
			'002ALO'|	0	|14.123	|21.232	|20.231	|18.246
			'003WEB'|	0	|14.123	|22.234	|20.232	|18.256
			'004HAM'|	0	|14.123	|22.204	|20.233	|18.266
			'005BUT'|	0	|14.123	|22.234	|20.230	|18.276
			'006RAI'|	0	|14.123	|22.234	|20.332	|18.286
			'007MSC'|	0	|14.123	|22.234	|20.332	|18.296
			'008GRO'|	0	|14.123	|22.234	|20.430	|18.299
	}


	def "Get distinct drivers from laps"(){
		setup: "Invoking service method"
			def drivers = lapService.findAllDrivers()
		expect:
			driver in drivers
		where:
			driver << [
				'001VET','002ALO','003WEB','004HAM','005BUT',
				'006RAI','007MSC','008GRO'
			]
	}

	def "Get best lap from a given driver"(){
		given: "Best laps"
			def driver = '001VET'
			def bestLap = lapService.findBestLapByDriver(driver)
		expect: "The best total time"
			bestLap == 73.713 
	}

	def "Get total time from a given driver"(){
		given: "A driver"
			def driver = '001VET'
			def totalTime = lapService.getTotalTimeSpentByDriver(driver)
		expect: "The total time all laps"
			totalTime == 149.926 
	}

	def "Get average time"(){
		setup:
			def avgTime = lapService.avgTimePerLap
		expect:
			avgTime.toBigDecimal().round(new MathContext(7)) == 75.51075 
	}

}
