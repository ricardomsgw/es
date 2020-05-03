package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.TopicRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.Tournament
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentDto
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.*
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@DataJpaTest
class CreateTournamentServiceSpockPerformanceTest extends Specification {
    static final String COURSE = "CourseOne"
    static final String ACRONYM = "C12"
    static final String ACADEMIC_TERM = "1º Semestre"
    static final int NUMBER_OF_QUESTIONS = 3
    static final Integer TOURNAMENT_NUMBER_OF_QUESTIONS = 3;
    public static final String COURSE_NAME = "Software Architecture"


    @Autowired
    TournamentService tournamentService

    @Autowired
    CourseRepository courseRepository

    @Autowired
    TournamentRepository tournamentRepository

    @Autowired
    TopicRepository topicRepository

    @Autowired
    CourseExecutionRepository courseExecutionRepository

    @Autowired
    UserRepository userRepository

    def currentDate = LocalDateTime.now()
    def startDate = LocalDateTime.now().plusDays(1)
    def conclusionDate = LocalDateTime.now().plusDays(2)
    def formatter
    def tournament
    def user
    def user2
    def topicDto
    def course
    def topic
    def courseExecution
    def courseExecutionId

    def setup() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        tournament = new Tournament()
        user = new User()
        user2 = new User()
        topicDto = new TopicDto()
        topicDto.setName("NEWTOPIC")
        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        courseRepository.save(course)

        topic = new Topic(course,topicDto)

        courseExecution = new CourseExecution(course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        courseExecutionRepository.save(courseExecution)

        tournament.setCourseExecution(courseExecution)
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

    def "performance testing to get 1000 tournaments"() {
        given: "a course"
        def userId = userRepository.findAll().get(0).getId()
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.getTopics().add(topic)
        tournament.setStartDate(startDate)
        tournament.setCurrentDate(currentDate)
        tournament.setConclusionDate(conclusionDate)
        tournament.setCreator_tournament(userId)

        and: "a 1000 course executions"
        1.upto(1000, {
            def result = tournamentRepository.save((Tournament)tournament)
            tournamentRepository.findById(result.getId()).get().setStatus(Tournament.Status.OPENED)
        })


        when:
        1.upto(10000, { tournamentService.getTournaments((Integer)courseExecutionId)})

        then:
        true
    }

    @TestConfiguration
    static class ServiceImplTestContextConfiguration {

        @Bean
        TournamentService tournamentService() {
            return new TournamentService()
        }

    }
}
