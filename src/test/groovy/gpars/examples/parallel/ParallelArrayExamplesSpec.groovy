package gpars.examples.parallel

import static groovyx.gpars.ParallelEnhancer.enhanceInstance

import spock.lang.Specification
import spock.lang.Shared

/**
 * This class has tests to show the way parallel helper methods can be used with GPars
**/
class ParallelArrayExamplesSpec extends Specification{
	
	@Shared def people = [
		new Person(name:'johnny',age:34),
		new Person(name:'peter',age:36),
		new Person(name:'julia',age:23)
	]

	def "Finding people names under 30 and single"(){
		when:"Asking for people over 30 and single "
			def result = new ParallelArrayExamples().findAllPeopleNameOver30AndSingle(people)
		then: "We must find johnny and peter"
			result == ["johnny","peter"]
	}

	def "Find people instances under 30"(){
		when: "Trying to find people under 30"
			def result = new ParallelArrayExamples().findAllPeopleUnder30(people)
		then: "We just find one julia"
			result.size() == 1
			result.find{it}.name == "julia"
	}

	def "Find people with names larger than 5 characters"(){
		when: "Trying to find people with names greater than five characters"
		 /* Adding GPars methods to the metaClass */
			enhanceInstance(people)
			def result = 
				new ParallelArrayExamples().
					findAllPeopleWithNamesGreaterThanFiveCharacters(people)
		then: "You should find only one"
			result.size() == 1
	}

}
