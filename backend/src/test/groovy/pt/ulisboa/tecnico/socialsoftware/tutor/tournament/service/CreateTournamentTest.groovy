package pt.ulisboa.tecnico.socialsoftware.tutor.tournament

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import spock.lang.Specification
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter

class CreateTournament extends Specification{

    static final String TOURNAMENT_TITLE = "Tournament title"
    static final Integer TOURNAMENT_NUMBER_OF_QUESTIONS = 3;
    public static final String COURSE_NAME = "Software Architecture"
    public static final String ACRONYM = "AS1"
    public static final String ACADEMIC_TERM = "1 SEM"

    def tournamentService
    def startDate
    def conclusionDate
    def currentDate
    def formatter

    def setup() {
        tournamentService = new TournamentService()
    }

    def "create a tournament"() {
    }


    def "create a tournament no start date"(){
    }

    def "create a tournament with conclusion date before start date"(){
    }

    def "create a tournament with start date before current date"(){
    }

    def "create a tournament with no topics"(){
    }

    def "create a tournament with no number of questions"() {
    }

    def "create a tournament with number of questions equal zero"(){
    }

    def "create a tournament by user no student"(){
    }
}
