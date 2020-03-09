package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.Tournament
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import spock.lang.Specification


class JoinTournamentSpockTest extends Specification {

    def tournamentService

    def setup(){
        tournamentService = new TournamentService()
    }

    def "student joins open tournament"(){
        //student joins tournament
        expect: false
    }

    def "student is already in tournament"(){
        //an exception is thrown
        expect: false
    }

    def "student tries to join closed tournament"(){
        //an exception is thrown
        expect: false
    }

    def "user not a student tries to join a tournament"(){
        //an exception is thrown
        expect: false
    }

    def "user tries to join a tournament belonging to a course he's not enrolled in"(){
        //an excepetion is thrown
        expect: false
    }


}
