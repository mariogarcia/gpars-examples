package gpars.examples.map

import org.joda.time.DateTime
import spock.lang.Specification

import static groovyx.gpars.GParsPool.withPool

/**
 * This test calculates some silly statistics about soccer data.
**/
class GParsMapSpec extends Specification{

	def "Using functional programming style"(){
		given: "Several soccer days"
			def fmt = "dd/MM/yyyy"
			def day1s = new DateTime().toDateMidnight().toDate()
			def soccerDays = [
				new SoccerDay(day:day1s + 1,fieldViewers:300000,soccerPlayers:200),
				new SoccerDay(day:day1s + 2,fieldViewers:3000000,soccerPlayers:200),
				new SoccerDay(day:day1s + 3,fieldViewers:1100000,soccerPlayers:200),
				new SoccerDay(day:day1s + 4,fieldViewers:300000,soccerPlayers:200)
			]
		when: "Trying to get days with certain conditions" 
			def dayWhenPlayersAreMoreThan1PercentOfViewers = withPool{
				soccerDays.parallel.
				 /* More than a million people */
					filter{it.fieldViewers > 1000000}.
					map{[it,(it.soccerPlayers / it.fieldViewers) * 100]}.
				 /* There's more than a certain percentage */
					filter{it[1] > 0.018}.
					map{it[0].day}.
				collection
			}			
		then: "We know that the fourth day is when it happens"
			dayWhenPlayersAreMoreThan1PercentOfViewers.find{it} == day1s + 3
	}
}
