package gpars.examples.dataflow

import groovy.util.logging.Log4j
import groovyx.gpars.dataflow.Dataflows

import static groovyx.gpars.dataflow.Dataflow.task

@Log4j
class TornadoDemoDataFlow extends TornadoDataEnabled{

	def execute(){
		def start = System.currentTimeMillis()
		def flow = new Dataflows()

	/* This is the first task to be executed */
		task {
			flow.rows = getTornadoRows()

		 /* All processes depending on the flow.rows variable are blocked 
			rest of tasks continue smoothly */
			flow.remoteData = getSimulatedDataFromARemoteHost()
		}
	 /* The point of this example is to notice that while the first tasks is still
		waiting for the data this second tasks can load the states list in parallel */
		task {flow.states = getStateRows()}
		task {
			flow.top = 
 				flow.rows.
					findAll{it.year > 2000 && it.year < 2005 && it.injuries > 10}.
					groupBy{it.stateCode}.
					collect{
						it.value[0].fatalities = it.value.sum{it.fatalities}
						it.value[0]
					}.
				sort{-it.fatalities}.
			take(3)

		}
		task {
			flow.result = flow.top.collect{data->
				flow.states.find{s-> s.stateCode == data.stateCode}.stateName
			}
			flow.end = System.currentTimeMillis()
		}
	
		log.debug "The top three are: ${flow.result}. It took ${flow.end-start} ms. Current alert level ${flow.remoteData}" 
	}

	static void main(args){
		new TornadoDemoDataFlow().execute()
	}

}
