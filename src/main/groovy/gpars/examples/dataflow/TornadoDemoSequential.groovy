package gpars.examples.dataflow

import groovy.util.logging.Log4j

/**
 * This examples just filters tornado incidents by the tornado force
 * then group the results by state. The three states having the highest
 * frequency are returned.
 *
 * Sample tornado incidents are available in the src/main/resources dir.
**/
@Log4j
class TornadoDemoSequential extends TornadoDataEnabled{

	def static final COMA = ","

	def execute(){
		def start = System.currentTimeMillis()
		def rows = getTornadoRows()

	 /* Process is blocked until the sleep method returns */
		def remoteData = getSimulatedDataFromARemoteHost()	
		def top = rows.
				findAll{it.year > 2000 && it.year < 2005 && it.injuries > 10}.
				groupBy{it.stateCode}.
				collect{
					it.value[0].fatalities = it.value.sum{it.fatalities}
					it.value[0]
				}.
				sort{-it.fatalities}.
			take(3)

		def states = getStateRows()
		def result = top.collect{data->
			states.find{s-> s.stateCode == data.stateCode}.stateName
		}

		def end = System.currentTimeMillis()
	
		log.debug "The top three are: ${result}. It took ${end-start} ms. Current alert level ${remoteData}" 
	}	

	static void main(args){
		new TornadoDemoSequential().execute()
	}
}
