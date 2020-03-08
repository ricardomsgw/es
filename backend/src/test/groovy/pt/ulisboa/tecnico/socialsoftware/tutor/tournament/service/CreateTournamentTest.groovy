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
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.QuizService
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.domain.Tournament
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.dto.TournamentDto
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.repository.TournamentService
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_EXECUTION_ACADEMIC_TERM_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_EXECUTION_ACRONYM_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_NAME_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_TYPE_NOT_DEFINED
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_NOT_FOUND
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.DUPLICATE_COURSE_EXECUTION


@DataJpaTest
class CreateTournamentTest extends Specification {

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

    def setup (){
        tournamentService = new TournamentService()
    }

    def "create a tournament"() {
        given: 'a tournament with title, number of questions, topics and dates'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setTitle(TOURNAMENT_TITLE)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.setTopics(topic)
        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now()
        conclusionDate = LocalDateTime.now().plusDays(1)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        def result = tournamentService.createTournament(tournament)

        then: "the data are correct to create the tournament"
        result.getTitle() == TOURNAMENT_TITLE
        result.getStartDate() == startDate.format(formatter)
        result.getConclusionDate() == conclusionDate.format(formatter)
        result.getNumberOfQuestions() == TOURNAMENT_NUMBER_OF_QUESTIONS
        result.getTopics().size() == 1


    }

    def "create a tournament no title"() {
        given: 'a tournament without title but with number of questions, topics and dates'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.setTopics(topic)
        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now()
        conclusionDate = LocalDateTime.now().plusDays(1)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        tournamentService.createTournament(tournament)

        then: "Tournament is not consistent because doesn't have title to create"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_NO_TITLE
    }

    def "create a tournament no start date"() {
        given: 'a tournament with title, number of questions, topics but no start date'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setTitle(TOURNAMENT_TITLE)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.setTopics(topic)
        currentDate = LocalDateTime.now()
        conclusionDate = LocalDateTime.now().plusDays(1)
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        tournamentService.createTournament(tournament)

        then: "Tournament is not consistent because doesn't have start date to create"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_WITH_DATA_NO_VALID
    }

    def "create a tournament with conclusion date before start date"() {
        given: 'a tournament with title, number of questions, topics, but conclusion date is before date'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setTitle(TOURNAMENT_TITLE)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.setTopics(topic)
        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now().plusDays(2)
        conclusionDate = LocalDateTime.now().plusDays(1)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        tournamentService.createTournament(tournament)

        then: "Tournament is not consistent because conclusion date is before start date"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_WITH_DATA_NO_VALID
    }

    def "create a tournament with start date before current date"() {
        given: 'a tournament with title, number of questions, topics, but start date before current date'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.setTopics(topic)
        tournament.setTitle(TOURNAMENT_TITLE)
        currentDate = LocalDateTime.now().plusDays(1)
        startDate = LocalDateTime.now()
        conclusionDate = LocalDateTime.now().plusDays(3)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        tournamentService.createTournament(tournament)

        then: "Tournament is not consistent because start date is before current date"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_WITH_DATA_NO_VALID
    }

    def "create a tournament with no topics"() {
        given: 'a tournament with title, number of questions, dates but no topics'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.setTitle(TOURNAMENT_TITLE)
        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now().plusDays(1)
        conclusionDate = LocalDateTime.now().plusDays(2)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        tournamentService.createTournament(tournament)

        then: "Tournament is not consistent because doesn't have topics to be created"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_NO_TOPICS
    }

    def "create a tournament with no number of questions"() {
        given: 'a tournament with title, number of questions, topics, dates but doesnt have number of questions'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setTitle(TOURNAMENT_TITLE)
        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now().plusDays(1)
        conclusionDate = LocalDateTime.now().plusDays(2)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setTopics(topic)

        when:
        tournamentService.createTournament(tournament)

        then: "Tournament is not consistent because but doesnt have number of questions to be created"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_NO_NUMBER_OF_QUESTIONS
    }

    def "create a tournament with number of questions equal zero"() {
        given: 'a tournament with title, number of questions = 0, dates and topics'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setNumberOfQuestions(0)
        tournament.setTitle(TOURNAMENT_TITLE)
        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now().plusDays(1)
        conclusionDate = LocalDateTime.now().plusDays(2)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setTopics(topic)

        when:
        tournamentService.createTournament(tournament)

        then: "Tournament is not consistent because number of questions can't be zero"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_NO_NUMBER_OF_QUESTIONS
    }

    def "create a tournament by user no student"() {
        given: 'a tournament created by a no student'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        User.Role role = User.Role.TEACHER
        user.setRole(role)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.setTitle(TOURNAMENT_TITLE)
        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now().plusDays(1)
        conclusionDate = LocalDateTime.now().plusDays(2)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setTopics(topic)

        when:
        tournamentService.createTournament(tournament)

        then: "Tournament can't be created by a no student"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_BY_NO_STUDENT
    }

    def "a tournament exists with this title"() {
        given: 'a tournament is created with a title that already exists'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new Tournament()
        def user = new User()
        def topic = new Topic()
        def course = new Course(COURSE_NAME, Course.Type.TECNICO)
        def courseExecution = new CourseExecution(course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.setTitle(TOURNAMENT_TITLE)
        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now().plusDays(1)
        conclusionDate = LocalDateTime.now().plusDays(2)
        tournament.setStartDate(startDate)
        tournament.setCurrentDate(currentDate)
        tournament.setConclusionDate(conclusionDate)
        tournament.setTopics(topic)
        courseExecution.getTournaments().contains(tournament)
        courseExecution.addTournament(tournament)

        def tournament1 = new Tournament()
        def user1 = new User()
        def topic1 = new Topic()
        User.Role role1 = User.Role.STUDENT
        user1.setRole(role1)
        tournament1.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament1.setTitle(TOURNAMENT_TITLE)
        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now().plusDays(1)
        conclusionDate = LocalDateTime.now().plusDays(2)
        tournament1.setStartDate(startDate)
        tournament1.setCurrentDate(currentDate)
        tournament1.setConclusionDate(conclusionDate)
        tournament1.setTopics(topic1)

        when:
        tournamentService.createTournament(tournament1)

        then: "Tournament can't be created by a no student"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_TITLE_ALREADY_EXISTS

    }



    @TestConfiguration
    static class TournamentServiceImplTestContextConfiguration {

        @Bean
        TournamentService tournamentService() {
            return new TournamentService()
        }
    }

}

