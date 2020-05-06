package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

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
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.Tournament
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentRepository
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@DataJpaTest
class GenerateQuiz extends Specification {
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

    def user1
    def creator
    def course
    def courseExecution
    def tournament
    def topic
    def currentDate
    def startDate
    def conclusionDate
    def formatter
    def topicDto

    def setup(){
        course = new Course(COURSE_NAME, Course.Type.TECNICO)
        courseExecution = new CourseExecution(course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        courseRepository.save(course)
        courseExecutionRepository.save(courseExecution)

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        TopicDto topicDto = new TopicDto();
        topicDto.setName("NEWTOPIC");
        topic = new Topic(course,topicDto);

        user1 = new User()
        user1.setKey(1)
        userRepository.save(user1)

        creator = new User()
        creator.setKey(2)
        creator.addCourseExecutions(courseExecution)
        creator.setRole(User.Role.STUDENT);
        userRepository.save(creator)

        tournament = CreateOpenTournament(courseExecution)
        tournamentRepository.save(tournament)



    }

    private Tournament CreateOpenTournament(CourseExecution courseExecution) {



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
        tournament.setCreator_tournament(userRepository.findAll().get(1).getId());
        tournament.addUser(userRepository.findAll().get(1))
        return tournament;
    }

    def "quiz gets generated when 1 user joins"(){
    }
    def "quiz doesnt exist when no one joins"(){
    }
    def "quiz is still the same after several people have joined"(){
    }
    @TestConfiguration
    static class TournamentServiceImplTestContextConfiguration {

        @Bean
        TournamentService tournamentService() {
            return new TournamentService()
        }

    }
}
