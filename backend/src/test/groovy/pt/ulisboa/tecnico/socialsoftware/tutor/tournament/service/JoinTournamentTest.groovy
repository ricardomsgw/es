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

@DataJpaTest
class JoinTournament extends Specification {
    public static final String COURSE_NAME = "Software Architecture"
    public static final String ACRONYM = "AS1"
    public static final String ACADEMIC_TERM = "1 SEM"
    static final Integer COURSE_EXECUTION_ID = 10;

    @Autowired
    CourseRepository courseRepository

    @Autowired
    CourseExecutionRepository courseExecutionRepository

    def user
    def course
    def courseExecution
    def tournament

    def setup(){
        def tournamentService = new TournamentService()
        user = new User();
        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        courseRepository.save(course)

        Set<CourseExecution> courseExecutions = new HashSet<>();
        courseExecutions.add(courseExecution)
        user.setCourseExecutions(courseExecutions)

        courseExecution = new CourseExecution();
        course.setId(COURSE_EXECUTION_ID);
        courseExecutionRepository.save(courseExecution)

        tournament = new Tournament()
        tournament.setStatus(Tournament.Status.OPENED)
    }

    def "student joins open tournament"(){
        given:
        user.setRole(User.Role.STUDENT)

        when:
        tournament.addUser(user);

        then:
        tournament.getUsers().size() == 1;
        user.getTournaments().size() == 1;
    }
    def "student is already in tournament"(){
        given:
        user.setRole(User.Role.STUDENT)
        tournament.addUser(user)

        when:
        tournament.addUser(user)

        then:
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_ALREADY_JOINED
        tournament.getUsers().size() == 1
        user.getTournaments().size() == 1
    }

    def "student tries to join closed tournament"(){
        given:
        user.setRole(User.Role.STUDENT)
        tournament.setStatus(Tournament.Status.CLOSED)

        when:
        tournament.addUser(user)

        then:
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_NOT_OPEN
        tournament.getUsers().size() == 0
        user.getTournaments().size() == 0
    }

    def "user not a student tries to join a tournament"(){
        given:
        user.setRole(User.Role.TEACHER)

        when:
        tournament.addUser(user)

        then:
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_NOT_ELEGIBLE
        tournament.getUsers().size() == 0
        user.getTournaments().size() == 0
    }

    def "student tries to join a tournament belonging to a course he's not enrolled in"(){
        given:
        user.setRole(User.Role.STUDENT)
        user.setCourseExecutions(null)

        when:
        tournament.addUser(user)

        then:
        def exception = thrown(TutorException)
        exception.getErrorMessage() == ErrorMessage.TOURNAMENT_NOT_ELEGIBLE
        tournament.getUsers().size() == 0
        user.getTournaments().size() == 0
    }
}
