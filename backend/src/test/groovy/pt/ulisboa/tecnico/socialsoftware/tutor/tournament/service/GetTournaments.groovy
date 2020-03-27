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
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.TopicRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import spock.lang.Specification
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_EXECUTION_NOT_FOUND
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.TOURNAMENT_NO_NUMBER_OF_QUESTIONS
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.TOURNAMENT_WITH_DATA_NO_VALID
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.TOURNAMENT_NO_TOPICS


@DataJpaTest
class GetTournaments extends Specification{

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
    TopicRepository topicRepository

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
        user = new User()

        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        courseRepository.save(course)

        topic = new Topic()
        //topic.setId(1)
        topicRepository.save(topic)
        courseExecution = new CourseExecution(course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        courseExecutionRepository.save(courseExecution)

        tournament.setCourseExecutionId(courseExecution.getId())
        courseExecutionId = courseExecution.getId()
        courseExecutionRepository.save(courseExecution)

        User.Role role = User.Role.STUDENT
        user.setRole(role)
        user.addCourseExecutions(courseExecution)

        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now().plusDays(1)
        conclusionDate = LocalDateTime.now().plusDays(2)

    }

    def "get opened tournaments"() {
        given: 'a tournament that will be opened'
        tournamentSettings()

        when:
        def result = tournamentService.getTournaments((Integer) courseExecutionId)

        then: "the data are correct to get opened tournament"
        tournamentRepository.count() == 1L
        result.size() == 1

    }

    def "get opened tournaments with no valid courseExecutionId"() {
        given: 'a tournament that will be opened and a not valid courseExecutionId'
        def courseExecutionIdNoValid = 1000
        tournamentSettings()

        when:
        def result = tournamentService.getTournaments((Integer) courseExecutionIdNoValid)

        then: "the data are no correct to get opened tournament"
        def exception = thrown(TutorException)
        result == null

    }

    def "get opened tournaments with no courseExecutionId"() {
        given: 'get tournaments without courseExecutionId'
        tournamentSettings()

        when:
        def result = tournamentService.getTournaments( )

        then: "the data are no correct to get opened tournament"
        def exception = thrown(MissingMethodException)
        result == null

    }

    private void tournamentSettings() {
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic.getId())
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))
        def resultTournament = tournamentService.createTournament((TournamentDto) tournament)
        tournamentRepository.findById(resultTournament.getId()).get().setStatus(Tournament.Status.OPENED)
    }


    @TestConfiguration
    static class TournamentServiceImplTestContextConfiguration {

        @Bean
        TournamentService tournamentService() {
            return new TournamentService()
        }

    }

}
