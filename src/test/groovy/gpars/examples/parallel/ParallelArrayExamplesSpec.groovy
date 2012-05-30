package gpars.examples.parallel

import static groovyx.gpars.ParallelEnhancer.enhanceInstance

import spock.lang.Specification
import spock.lang.Shared

class ParallelArrayExamplesSpec extends Specification{
	
	@Shared def people = [
		new Person(name:'johnny',age:34),
		new Person(name:'peter',age:36),
		new Person(name:'julia',age:23)
	]

	def "Finding people names under 30 and single"(){
		when:	
			def result = new ParallelArrayExamples().findAllPeopleNameOver30AndSingle(people)
		then:
			result == ["johnny","peter"]
	}

	def "Find people instances under 30"(){
		when:
			def result = new ParallelArrayExamples().findAllPeopleUnder30(people)
		then:
			result.size() == 1
			result.find{it}.name == "julia"
	}

	def "Find people with names larger than 5 characters"(){
		when:
			enhanceInstance(people)
			def result = 
				new ParallelArrayExamples().
					findAllPeopleWithNamesGreaterThanFiveCharacters(people)
		then:
			result.size() == 1
	}

}
