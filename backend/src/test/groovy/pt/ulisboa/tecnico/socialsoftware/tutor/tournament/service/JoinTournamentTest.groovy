package pt.ulisboa.tecnico.socialsoftware.tutor.tournament

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import spock.lang.Specification
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter

@DataJpaTest
class JoinTournament extends Specification {
    public static final String COURSE_NAME = "Software Architecture"
    public static final String ACRONYM = "AS1"
    public static final String ACADEMIC_TERM = "1 SEM"
    static final Integer COURSE_EXECUTION_ID = 10;
    public static final String USERNAME = "username"
    static final Integer USER_KEY = 1;
    static final Integer TOURNAMENT_NUMBER_OF_QUESTIONS = 3;
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
    UserRepository userRepository

    @Autowired
    TournamentRepository tournamentRepository

    def user
    def course
    def courseExecution
    def tournament
    def topic
    def currentDate
    def startDate
    def conclusionDate
    def formatter

    def setup(){
        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        courseExecution = new CourseExecution(course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        courseRepository.save(course)
        courseExecutionRepository.save(courseExecution)

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        tournament = CreateOpenTournament(courseExecution)
        tournamentRepository.save(tournament)

        user = new User()
        user.setKey(1)
        userRepository.save(user)

    }

    private Tournament CreateOpenTournament(CourseExecution courseExecution) {
        topic = new Topic()


        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now().plusDays(1)
        conclusionDate = LocalDateTime.now().plusDays(2)

        tournament = new Tournament()
        tournament.setStartDate(startDate)
        tournament.setCurrentDate(currentDate)
        tournament.setConclusionDate(conclusionDate)
        tournament.setCourseExecution((courseExecution))
        tournament.setStatus(Tournament.Status.OPENED)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic)
        tournament
    }

    def "student joins open tournament"(){
        given:
        user.setRole(User.Role.STUDENT)
        user.addCourseExecutions(courseExecution)

        def tournamentId = tournamentRepository.findAll().get(0).getId()
        def userId = userRepository.findAll().get(0).getId()
        when:
        def result = tournamentService.addUser(userId, tournamentId);

        then:
        result.getUsers().size() == 1;
    }
    def "student is already in tournament"(){
        given:
        user.addCourseExecutions(courseExecution)
        user.setRole(User.Role.STUDENT)

        def tournamentId = tournamentRepository.findAll().get(0).getId()
        def userId = userRepository.findAll().get(0).getId()

        tournamentService.addUser(userId, tournamentId);
        when:
        tournamentService.addUser(userId, tournamentId);

        then:
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_ALREADY_JOINED
    }

    def "student tries to join closed tournament"(){
        given:
        user.setRole(User.Role.STUDENT)
        user.addCourseExecutions(courseExecution)

        tournament.setStatus(Tournament.Status.CLOSED)

        def tournamentId = tournamentRepository.findAll().get(0).getId()
        def userId = userRepository.findAll().get(0).getId()
        when:
        tournamentService.addUser(userId, tournamentId);

        then:
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_NOT_OPEN
    }


    def "user not a student tries to join a tournament"(){
        given:
        user.addCourseExecutions(courseExecution)
        user.setRole(User.Role.TEACHER)

        def tournamentId = tournamentRepository.findAll().get(0).getId()
        def userId = userRepository.findAll().get(0).getId()
        when:
        tournamentService.addUser(userId, tournamentId);
        then:
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_NOT_ELEGIBLE
    }

    def "student tries to join a tournament belonging to a course he's not enrolled in"(){
        given:
        user.setRole(User.Role.STUDENT)
        def tournamentId = tournamentRepository.findAll().get(0).getId()
        def userId = userRepository.findAll().get(0).getId()
        when:
        tournamentService.addUser(userId, tournamentId);
        then:
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_NOT_ELEGIBLE
    }
    @TestConfiguration
    static class TournamentServiceImplTestContextConfiguration {

        @Bean
        TournamentService tournamentService() {
            return new TournamentService()
        }

    }
}
