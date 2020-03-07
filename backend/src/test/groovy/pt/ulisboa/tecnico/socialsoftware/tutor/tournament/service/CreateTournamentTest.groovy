package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.administration.AdministrationService
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseDto
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.QuizService
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.domain.Tournament
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.repository.TournamentService
import spock.lang.Specification
import spock.lang.Unroll

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_EXECUTION_ACADEMIC_TERM_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_EXECUTION_ACRONYM_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_NAME_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_TYPE_NOT_DEFINED
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_NOT_FOUND
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DUPLICATE_COURSE_EXECUTION


@DataJpaTest
class CreateTournamentTest extends Specification {

    def tournamentService

    def setup (){
        tournamentService = new TournamentService()
    }

    def "create a tournament"() {
        //a tournament is created
        expect: false
    }

    def "create a tournament no title"() {
        //create a tournament no title
        expect: false
    }

    def "create a tournament no start date"() {
        //create a tournament no start date
        expect: false
    }

    def "create a tournament with conclusion date before start date"() {
        //create a tournament with conclusion date before start date
        expect: false
    }

    def "create a tournament with start date before current date"() {
        //create a tournament with start date before current date
        expect: false
    }

    def "create a tournament with no topics"() {
        //create a tournament with no topics
        expect: false
    }

    def "create a tournament with no number of questions"() {
        //create a tournament with no number of questions
        expect: false
    }

    def "create a tournament with number of questions equal zero"() {
        //create a tournament with no number of questions
        expect: false
    }

    def "create a tournament by user no student"() {
        //create a tournament by user no student
        expect: false
    }

    def "a tournament exists with this title"() {
        //a tournament exists with this title
        expect: false
    }

    def "a student wants to participate in a closed tournament"() {
        //a student wants to participate in a closed tournament
        expect: false
    }

    def "a student wants to participate in a tournament isn't in the course"() {
        //a student wants to participate in a tournament isn't in the course
        expect: false
    }


    @TestConfiguration
    static class TournamentServiceImplTestContextConfiguration {

        @Bean
        Tournament tournamentService() {
            return new TournamentService()
        }
    }

}

