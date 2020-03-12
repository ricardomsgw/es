package pt.ulisboa.tecnico.socialsoftware.tutor.tournament

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseDto
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseService
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import spock.lang.Specification
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_EXECUTION_ACADEMIC_TERM_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_EXECUTION_ACADEMIC_TERM_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_EXECUTION_ACRONYM_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_EXECUTION_ACRONYM_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_NAME_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_NAME_IS_EMPTY
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_TYPE_NOT_DEFINED
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.TOURNAMENT_NO_NUMBER_OF_QUESTIONS
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.TOURNAMENT_WITH_DATA_NO_VALID


@DataJpaTest
class CreateTournament extends Specification{

    static final Integer TOURNAMENT_NUMBER_OF_QUESTIONS = 3;
    static final Integer COURSE_EXECUTION_ID = 10;
    public static final String COURSE_NAME = "Software Architecture"
    public static final String ACRONYM = "AS1"
    public static final String ACADEMIC_TERM = "1 SEM"
    public static final String START_DATE = "2020-06-10 03:20"
    public static final String CONCLUSION_DATE = "2020-06-11 03:20"

    @Autowired
    TournamentService tournamentService

    @Autowired
    CourseRepository courseRepository

    @Autowired
    CourseExecutionRepository courseExecutionRepository

    @Autowired
    TournamentRepository tournamentRepository

    @Autowired
    UserRepository userRepository

    def startDate
    def conclusionDate
    def currentDate
    def formatter
    def tournament
    def course
    def courseExecution
    def topic

    def setup() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        tournament = new TournamentDto()
        def user = new User()
        topic = new TopicDto()

        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        courseRepository.save(course)

        courseExecution = new CourseExecution();
        course.setId(COURSE_EXECUTION_ID);
        courseExecutionRepository.save(courseExecution)

        User.Role role = User.Role.STUDENT
        user.setRole(role)

        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now()
        conclusionDate = LocalDateTime.now().plusDays(1)

    }

    def "create a tournament"() {
        given: 'a tournament with title, number of questions, topics and dates'
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setCourseExecutionId(course.getId());

        when:
        def result = tournamentService.createTournament((TournamentDto) tournament)

        then: "the data are correct to create the tournament"
        result.getStatus() == Tournament.Status.CREATED
        result.getStartDate() == startDate.format(formatter)
        result.getConclusionDate() == conclusionDate.format(formatter)
        result.getNumberOfQuestions() == TOURNAMENT_NUMBER_OF_QUESTIONS
        result.getTopics().size() == 1
        result.getCourseExecutionId() == COURSE_EXECUTION_ID

    }


    def "create a tournament no start date"(){
        given: 'a tournament with title, number of questions, topics but no start date'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        def course = new CourseExecution();
        course.setId(COURSE_EXECUTION_ID);
        User.Role role = User.Role.STUDENT
        user.setRole(role)
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

    def "create a tournament with conclusion date before start date"(){
        given: 'a tournament with title, number of questions, topics, but conclusion date is before date'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        def course = new CourseExecution();
        course.setId(COURSE_EXECUTION_ID);
        User.Role role = User.Role.STUDENT
        user.setRole(role)
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

    def "create a tournament with start date before current date"(){
        given: 'a tournament with title, number of questions, topics, but start date before current date'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        def course = new CourseExecution();
        course.setId(COURSE_EXECUTION_ID);
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.setTopics(topic)
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

    def "create a tournament with no topics"(){
        given: 'a tournament with title, number of questions, dates but no topics'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def course = new CourseExecution();
        course.setId(COURSE_EXECUTION_ID);
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
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
        def course = new CourseExecution();
        course.setId(COURSE_EXECUTION_ID);
        User.Role role = User.Role.STUDENT
        user.setRole(role)
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

    def "create a tournament with number of questions equal zero"(){
        given: 'a tournament with title, number of questions = 0, dates and topics'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setNumberOfQuestions(0)
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

    def "create a tournament by user no student"(){
        given: 'a tournament created by a no student'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        def course = new CourseExecution();
        course.setId(COURSE_EXECUTION_ID);
        User.Role role = User.Role.TEACHER
        user.setRole(role)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
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

    def "invalid arguments: numberOfQuestions=#TOURNAMENT_NUMBER_OF_QUESTIONS | datas no valid || errorMessage=#errorMessage "() {
        given: "a tournamentDto"
        def tournament = new TournamentDto()
        tournament.setNumberOfQuestions(numberOfQuestionsQuestions)
        tournament.setStartDate(startDateDate)
        tournament.setConclusionDate(conclusionDateDate)

        when:
        tournamentService.createTournament(tournament)

        then:
        def error = thrown(TutorException)
        error.errorMessage == errorMessage

        where:
        numberOfQuestionsQuestions      | startDateDate  | conclusionDateDate  || errorMessage
        -1                              | START_DATE     | CONCLUSION_DATE     || TOURNAMENT_NO_NUMBER_OF_QUESTIONS
        0                               | START_DATE     | CONCLUSION_DATE     || TOURNAMENT_NO_NUMBER_OF_QUESTIONS
        TOURNAMENT_NUMBER_OF_QUESTIONS  | null           | CONCLUSION_DATE     || TOURNAMENT_WITH_DATA_NO_VALID
        TOURNAMENT_NUMBER_OF_QUESTIONS  | "      "       | CONCLUSION_DATE     || TOURNAMENT_WITH_DATA_NO_VALID
        TOURNAMENT_NUMBER_OF_QUESTIONS  | START_DATE     | null                || TOURNAMENT_WITH_DATA_NO_VALID
        TOURNAMENT_NUMBER_OF_QUESTIONS  | START_DATE     | "      "            || TOURNAMENT_WITH_DATA_NO_VALID

    }

    @TestConfiguration
    static class TournamentServiceImplTestContextConfiguration {

        @Bean
        TournamentService tournamentService() {
            return new TournamentService()
        }

    }

}
