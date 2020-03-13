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
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.TOURNAMENT_NO_TOPICS


@DataJpaTest
class CreateTournament extends Specification{

    static final Integer TOURNAMENT_NUMBER_OF_QUESTIONS = 3;
    static final Integer COURSE_EXECUTION_ID = 10;
    static final Integer TOURNAMENT_ID = 11;
    public static final String COURSE_NAME = "Software Architecture"
    public static final String ACRONYM = "AS1"
    public static final String ACADEMIC_TERM = "1 SEM"
    public static final String CURRENT_DATE = "2020-06-09 03:20"
    public static String START_DATE = LocalDateTime.now().plusDays(1)
    public static String CONCLUSION_DATE = LocalDateTime.now().plusDays(2)

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
    def courseExecutionId
    def user

    def setup() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        tournament = new TournamentDto()
        //tournament.setId(TOURNAMENT_ID)
        user = new User()
        topic = new TopicDto()

        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        courseRepository.save(course)

        courseExecution = new CourseExecution(course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        courseExecutionRepository.save(courseExecution)

        tournament.setCourseExecutionId(courseExecution.getId())
        courseExecutionId = courseExecution.getId()
        //course.setId(COURSE_EXECUTION_ID);
        Integer ce = courseExecution.getId()
        //tournament.setCourseExecutionId(ce)
        courseExecutionRepository.save(courseExecution)

        User.Role role = User.Role.STUDENT
        user.setRole(role)
        user.addCourseExecutions(courseExecution)

        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now().plusDays(1)
        conclusionDate = LocalDateTime.now().plusDays(2)

    }

    def "create a tournament"() {
        given: 'a tournament number of questions, topics and dates'
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))


        when:
        def result = tournamentService.createTournament((TournamentDto) tournament)

        then: "the data are correct to create the tournament"
        tournamentRepository.count() == 1L
        result.getStatus() == Tournament.Status.CREATED
        result.getStartDate() == startDate.format(formatter)
        result.getConclusionDate() == conclusionDate.format(formatter)
        result.getNumberOfQuestions() == TOURNAMENT_NUMBER_OF_QUESTIONS
        result.getTopics().size() == 1
        result.getCourseExecutionId() == courseExecutionId

    }


    def "create a tournament no start date"(){
        given: 'a tournament with number of questions, topics but no start date'

        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic)
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        tournamentService.createTournament((TournamentDto) tournament)

        then: "Tournament is not consistent because doesn't have start date to create"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == TOURNAMENT_WITH_DATA_NO_VALID
        tournamentRepository.count() == 0L
    }

    def "create a tournament with conclusion date before start date"(){
        given: 'a tournament with number of questions, topics, but conclusion date is before date'

        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic)
        tournament.setStartDate(startDate.plusDays(2).format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        tournamentService.createTournament((TournamentDto) tournament)

        then: "Tournament is not consistent because conclusion date is before start date"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == TOURNAMENT_WITH_DATA_NO_VALID
        tournamentRepository.count() == 0L
    }

    def "create a tournament with start date before current date"(){
        given: 'a tournament with title, number of questions, topics, but start date before current date'
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic)
        tournament.setStartDate(startDate.minusDays(1).format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        tournamentService.createTournament((TournamentDto) tournament)

        then: "Tournament is not consistent because start date is before current date"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == TOURNAMENT_WITH_DATA_NO_VALID
        tournamentRepository.count() == 0L
    }

    def "create a tournament with no topics"(){
        given: 'a tournament with title, number of questions, dates but no topics'
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        tournamentService.createTournament((TournamentDto) tournament)

        then: "Tournament is not consistent because doesn't have topics to be created"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == TOURNAMENT_NO_TOPICS
        tournamentRepository.count() == 0L
    }

    def "create a tournament with no number of questions"() {
        given: 'a tournament with title, number of questions, topics, dates but doesnt have number of questions'
        tournament.getTopics().add(topic)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        tournamentService.createTournament((TournamentDto) tournament)

        then: "Tournament is not consistent because but doesnt have number of questions to be created"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == TOURNAMENT_NO_NUMBER_OF_QUESTIONS
        tournamentRepository.count() == 0L
    }

/*
    def "create a tournament by user no student"(){
        given: 'a tournament created by a no student'
        tournament.setNumberOfQuestions(0)
        tournament.getTopics().add(topic)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        user.setRole(user.Role.TEACHER)

        when:
        tournamentService.createTournament((TournamentDto) tournament)

        then: "Tournament can't be created by a no student"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_BY_NO_STUDENT
    }*/

    def "invalid arguments: numberOfQuestions=#TOURNAMENT_NUMBER_OF_QUESTIONS || errorMessage=#errorMessage "() {
        given: "a tournamentDto"

        tournament.setNumberOfQuestions(numberOfQuestionsQuestions)
        tournament.getTopics().add(topic)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        tournamentService.createTournament((TournamentDto) tournament)

        then:
        def error = thrown(TutorException)
        error.errorMessage == errorMessage

        where:
        numberOfQuestionsQuestions      || errorMessage
        -1                              || TOURNAMENT_NO_NUMBER_OF_QUESTIONS
        0                               || TOURNAMENT_NO_NUMBER_OF_QUESTIONS


    }

    @TestConfiguration
    static class TournamentServiceImplTestContextConfiguration {

        @Bean
        TournamentService tournamentService() {
            return new TournamentService()
        }

    }

}
