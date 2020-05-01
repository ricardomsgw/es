package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.TopicRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.Tournament
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.*
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest
class CreateTournamentServiceSpockPerformanceTest extends Specification {
    static final String COURSE = "CourseOne"
    static final String ACRONYM = "C12"
    static final String ACADEMIC_TERM = "1º Semestre"
    static final int NUMBER_OF_QUESTIONS = 3


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

    def currentDate = LocalDateTime.now()
    def startDate = LocalDateTime.now().plusDays(1)
    def conclusionDate = LocalDateTime.now().plusDays(2)

    def "performance testing to get 1000 tournaments"() {
        given: "a course"
        def course = new Course(COURSE, Course.Type.TECNICO)
        def topicDto = new TopicDto()
        def topic = new Topic(course, topicDto)
        topicRepository.save(topic)
        courseRepository.save(course)
        def courseExecution = new CourseExecution(course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        courseExecutionRepository.save(courseExecution)
        def courseExecutionId = courseExecution.getId()
        def topics = new HashSet()

        topics.add(topic)
        and: "a 1000 course executions"
        1.upto(1, {
            def result = tournamentRepository.save(new Tournament(NUMBER_OF_QUESTIONS, startDate, conclusionDate, topics))
            tournamentRepository.findById(result.getId()).get().setStatus(Tournament.Status.OPENED)
        })


        when:
        1.upto(1, { tournamentService.getTournaments(courseExecutionId)})

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
