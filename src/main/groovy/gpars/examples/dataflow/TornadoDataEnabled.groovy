package gpars.examples.dataflow


/**
* This is a helper class which loads information about Tornados in the USA and 
* the states codification
**/
class TornadoDataEnabled{

	def static final COMA = ","

	def getTornadoRows(){
		def filePath = getClass().classLoader.getResource("gpars/examples/dataflow/").file
		def filesDir = new File(filePath)
		def regex = ~/.*\.csv/
		def rows = []

		log.debug "Loading tornado data"

		filesDir.eachFileMatch(regex){file->
			file.splitEachLine(COMA){fields->
				rows << new TornadoData(
					year: fields[1]?.toInteger(),
					month: fields[2]?.toInteger(),
					day: fields[3]?.toInteger(),
					stateCode: fields[8]?.toInteger(),
					injuries: fields[11]?.toInteger(),
					fatalities:fields[12]?.toInteger(),
					scale:fields[10]?.toInteger()
				)
			}
		}

		log.debug "Loaded ${rows.size()} tornados"

		rows
	}

 /* This method tries to simulate a blocking remote call to a service */
	def getSimulatedDataFromARemoteHost(){
		Thread.sleep(2000)
		"5"
	}

	def getStateRows(){
		def states = []
		new File(
			getClass().classLoader.getResource("gpars/examples/dataflow/states.txt").file
		).splitEachLine(COMA){fields->
			states << new StateData(
				stateName: fields[0],
				stateCode: fields[1]?.toInteger()
			)
		}
		states
	}

}
