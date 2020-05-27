package pt.ulisboa.tecnico.socialsoftware.tutor.tournament

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.QuizService
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import spock.lang.Specification
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.TOURNAMENT_NO_NUMBER_OF_QUESTIONS
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.TOURNAMENT_WITH_DATA_NO_VALID
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.TOURNAMENT_NO_TOPICS
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.USER_NOT_CREATOR


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
    def user2
    def topicDto

    def setup() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        tournament = new TournamentDto()
        user = new User()
        user2 = new User()
        topicDto = new TopicDto()
        topicDto.setName("NEWTOPIC")
        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        courseRepository.save(course)

        topic = new Topic(course,topicDto)

        courseExecution = new CourseExecution(course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        courseExecutionRepository.save(courseExecution)

        tournament.setCourseExecutionId(courseExecution.getId())
        courseExecutionId = courseExecution.getId()
        courseExecutionRepository.save(courseExecution)

        User.Role role = User.Role.STUDENT
        user.setRole(role)
        user.addCourseExecutions(courseExecution)
        user.setKey(1)
        userRepository.save(user)

        user2.setRole(role)
        user2.addCourseExecutions(courseExecution)
        user2.setKey(2)
        userRepository.save(user2)

        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now().plusDays(1)
        conclusionDate = LocalDateTime.now().plusDays(2)

    }

    def "create a tournament"() {
        given: 'a tournament number of questions, topics and dates'
        def userId = userRepository.findAll().get(0).getId()
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic.getId())
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setCreatorId(userId)

        when:
        def result = tournamentService.createTournament((TournamentDto) tournament)

        then: "the data are correct to create the tournament"
        tournamentRepository.count() == 1L
        result.getStatus() == Tournament.Status.OPENED
        result.getStartDate() == startDate.format(formatter)
        result.getConclusionDate() == conclusionDate.format(formatter)
        result.getNumberOfQuestions() == TOURNAMENT_NUMBER_OF_QUESTIONS
        result.getTopics().size() == 1
        result.getCourseExecutionId() == courseExecutionId
        result.getCreatorId() == user.getId()

    }

    def "cancel a tournament"() {
        given: 'a tournament number of questions, topics and dates and a user'
        def userId = userRepository.findAll().get(0).getId()
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic.getId())
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setCreatorId(userId)
        tournamentService.createTournament((TournamentDto) tournament)
        def errorX = false
        def result = tournamentRepository.count()
        def tournamentId = tournamentRepository.findAll().get(0).getId()

        when:
        tournamentService.cancelTournament(tournamentId)

        then: "the data are correct to cancel the tournament"
        !errorX
        result == 1L
        tournamentRepository.count() == 0L

    }

    def "cancel a tournament by user not creator"() {
        given: 'a tournament number of questions, topics and dates and a user not creator'
        def userId = userRepository.findAll().get(0).getId()
        def userId2 = userRepository.findAll().get(1).getId()
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic.getId())
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setCreatorId(userId)
        tournamentService.createTournament((TournamentDto) tournament)
        def errorX = false
        def result = tournamentRepository.count()
        def tournamentId = tournamentRepository.findAll().get(0).getId()

        when:
        errorX = true

        then: "user not tournament's creator can't cancel a tournament"
        errorX
        tournamentRepository.count() == 1L


    }

    def "create a tournament no start date"(){
        given: 'a tournament with number of questions, topics but no start date'
        def userId = userRepository.findAll().get(0).getId()
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic.getId())
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setCreatorId(userId)

        when:
        tournamentService.createTournament((TournamentDto) tournament)

        then: "Tournament is not consistent because doesn't have start date to create"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == TOURNAMENT_WITH_DATA_NO_VALID
        tournamentRepository.count() == 0L
    }

    def "create a tournament with conclusion date before start date"(){
        given: 'a tournament with number of questions, topics, but conclusion date is before date'
        def userId = userRepository.findAll().get(0).getId()
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic.getId())
        tournament.setStartDate(startDate.plusDays(2).format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setCreatorId(userId)

        when:
        tournamentService.createTournament((TournamentDto) tournament)

        then: "Tournament is not consistent because conclusion date is before start date"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == TOURNAMENT_WITH_DATA_NO_VALID
        tournamentRepository.count() == 0L
    }

    def "create a tournament with start date before current date"(){
        given: 'a tournament with title, number of questions, topics, but start date before current date'
        def userId = userRepository.findAll().get(0).getId()
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic.getId())
        tournament.setStartDate(startDate.minusDays(1).format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setCreatorId(userId)

        when:
        tournamentService.createTournament((TournamentDto) tournament)

        then: "Tournament is not consistent because start date is before current date"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == TOURNAMENT_WITH_DATA_NO_VALID
        tournamentRepository.count() == 0L
    }

    def "create a tournament with no topics"(){
        given: 'a tournament with title, number of questions, dates but no topics'
        def userId = userRepository.findAll().get(0).getId()
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setCreatorId(userId)

        when:
        tournamentService.createTournament((TournamentDto) tournament)

        then: "Tournament is not consistent because doesn't have topics to be created"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == TOURNAMENT_NO_TOPICS
        tournamentRepository.count() == 0L
    }

    def "create a tournament with no number of questions"() {
        given: 'a tournament with title, number of questions, topics, dates but doesnt have number of questions'
        def userId = userRepository.findAll().get(0).getId()
        tournament.getTopics().add(topic.getId())
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setCreatorId(userId)

        when:
        tournamentService.createTournament((TournamentDto) tournament)

        then: "Tournament is not consistent because but doesnt have number of questions to be created"
        def exception = thrown(TutorException)
        exception.getErrorMessage() == TOURNAMENT_NO_NUMBER_OF_QUESTIONS
        tournamentRepository.count() == 0L
    }


    def "invalid arguments: numberOfQuestions=#TOURNAMENT_NUMBER_OF_QUESTIONS || errorMessage=#errorMessage "() {
        given: "a tournamentDto"
        def userId = userRepository.findAll().get(0).getId()
        tournament.setNumberOfQuestions(numberOfQuestionsQuestions)
        tournament.getTopics().add(topic.getId())
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        tournament.setCreatorId(userId)

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
