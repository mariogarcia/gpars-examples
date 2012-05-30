package gpars.examples.parallel

import static groovyx.gpars.GParsPool.withPool
import static groovyx.gpars.ParallelEnhancer.enhanceInstance

class ParallelArrayExamples{

	def findAllPeopleNameOver30AndSingle(people){
		enhanceInstance(people)
		people.
			findAllParallel{it.age > 30}.
			collectParallel{it.name}
	}

	def findAllPeopleUnder30(people){
		withPool{
			people.findAllParallel{it.age < 30}
		}
	}

	def findAllPeopleWithNamesGreaterThanFiveCharacters(people){
		people.findAll{it.name.length() > 5}	
	}

}
