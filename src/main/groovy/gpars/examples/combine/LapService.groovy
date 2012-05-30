package gpars.examples.combine

import groovy.util.logging.Log4j
import static groovyx.gpars.GParsPool.withPool

/**
 * This class do some statistics from a given driver's laps
**/
@Log4j
class LapService{

	def loadLaps(){
		def laps = []
		def file = new File(
			LapService.class.classLoader.getResource("gpars/examples/combine/laps.csv").toURI()
		)
		file.splitEachLine(";"){
			laps << new Lap(
				lapNumber:it[1].toInteger(),
				driver:it[0],
				partial1:it[2].toDouble(),
				partial2:it[3].toDouble(),
				partial3:it[4].toDouble(),
				partial4:it[5].toDouble()
			)
			laps
		}	
	}

	def findAllDrivers(){
		withPool{
			loadLaps().parallel.
				groupBy{it.driver}.
			keySet()
		}
	}

	def findAllBestPartialsByDriver(){
		withPool{
			def keyClosure = {nxt-> "${nxt.driver}".toString()}
			def accumulator = {new Lap()}
			def aggregation = {sum,value->
				sum.driver = value.driver
				(1..4).each{p->
					def field = "partial${p}"
					if (sum."$field" > value."$field" || sum."$field" == 0){
						sum."$field"  = value."$field"
					}
				}
				sum	
			}
			loadLaps().parallel.
				map{[keyClosure.call(it),it]}.
				combine(accumulator,aggregation).
			values()
		}
	}	

	def findBestLapByDriver(driver){
		def bestLap = withPool{
			loadLaps().findAll{it.driver == driver}.parallel.
				map{it.partial1 + it.partial2 + it.partial3 + it.partial4}.
				sort{it}.collection.
			find{it}
		}
	}	

	def getTotalTimeSpentByDriver(driver){
		def totalTime = withPool{
			loadLaps().findAll{it.driver == driver}.parallel.
				map{it.partial1 + it.partial2 + it.partial3 + it.partial4}.
				sum()
		}
	}

	def getAvgTimePerLap(){
		def avgTime = withPool{
			def laps = loadLaps()
			def sum = {c-> c.collect{it.partial1+it.partial2+it.partial3+it.partial4}.sum()}.asyncFun()
			def ocurrences = {c-> c.size()}.asyncFun()
			def calculate = {s,o-> s/o}.asyncFun(true)

			calculate(sum(laps),ocurrences(laps))
		}
	}
}
