package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
//import pt.ulisboa.tecnico.socialsoftware.tutor.administration.AdministrationService
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecutionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.TopicRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.Tournament
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.user.dto.UserDto
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest
class AddUserServiceSpockPerformanceTest extends Specification {
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

    @Autowired
    QuestionRepository questionRepository

    @Autowired
    UserRepository userRepository;

    def currentDate = LocalDateTime.now()
    def startDate = LocalDateTime.now().plusDays(1)
    def conclusionDate = LocalDateTime.now().plusDays(2)

    def "performance testing to get 1000 addUsers"() {
        given: "a tournament and a user"
        def course = new Course(COURSE, Course.Type.TECNICO)
        TopicDto topicDto = new TopicDto();
        topicDto.setName("NEWTOPIC");
        def topic = new Topic(course,topicDto);

        topicRepository.save(topic)

        def question = new Question()
        question.setKey(1)
        question.setTitle("Question title")
        question.addTopic(topic);
        question.setStatus(Question.Status.AVAILABLE)
        questionRepository.save(question)
        courseRepository.save(course)
        def courseExecution = new CourseExecution(course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        courseExecutionRepository.save(courseExecution)
        def topics = new HashSet()

        topics.add(topic)
        def List<Integer> resultTournaments = []
        def List<Integer> resultUsers = []
        def creator = new User('creator','creatorusername',9999999,User.Role.STUDENT)
        creator.addCourseExecutions(courseExecution)
        def iterator = 0
        and: "100 users and 100 tournaments"
        1.upto(100, {

            resultTournaments.add(tournamentRepository.save(new Tournament(NUMBER_OF_QUESTIONS, startDate, conclusionDate , topics,creator)).getId())
            tournamentRepository.findById(resultTournaments[it-1]).get().setStatus(Tournament.Status.OPENED)
            tournamentRepository.findById(resultTournaments[it-1]).get().setCourseExecution(courseExecution)
            def user = new User('name','username'+it,it+1000,User.Role.STUDENT)
            def result  = userRepository.save(user).getId()
            resultUsers.add(result)
            userRepository.findById(resultUsers[it-1]).get().addCourseExecutions(courseExecution)
        })
        when:
        1.upto(10000, {
            tournamentService.addUser(resultUsers[(int)(iterator/100)],resultTournaments[iterator%100])
            iterator += 1
        })

        then:
        true
    };

    @TestConfiguration
    static class ServiceImplTestContextConfiguration {

        @Bean
        TournamentService tournamentService() {
            return new TournamentService()
        }

    }
}
