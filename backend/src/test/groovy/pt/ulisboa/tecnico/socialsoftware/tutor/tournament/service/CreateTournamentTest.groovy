package pt.ulisboa.tecnico.socialsoftware.tutor.tournament

import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import spock.lang.Specification
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter

class CreateTournament extends Specification{

    static final String TOURNAMENT_TITLE = "Tournament title"
    static final Integer TOURNAMENT_NUMBER_OF_QUESTIONS = 3;
    public static final String COURSE_NAME = "Software Architecture"
    public static final String ACRONYM = "AS1"
    public static final String ACADEMIC_TERM = "1 SEM"

    def tournamentService
    def startDate
    def conclusionDate
    def currentDate
    def formatter

    def setup() {
        tournamentService = new TournamentService()
    }

    def "create a tournament"() {
        given: 'a tournament with title, number of questions, topics and dates'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
        User.Role role = User.Role.STUDENT
        user.setRole(role)
        tournament.setNumberOfQuestions(TOURNAMENT_NUMBER_OF_QUESTIONS)
        tournament.setTopics(topic)
        currentDate = LocalDateTime.now()
        startDate = LocalDateTime.now()
        conclusionDate = LocalDateTime.now().plusDays(1)
        tournament.setStartDate(startDate.format(formatter))
        tournament.setCurrentDate(currentDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        when:
        def result = tournamentService.createTournament(tournament)

        then: "the data are correct to create the tournament"
        result.getTitle() == TOURNAMENT_TITLE
        result.getStartDate() == startDate.format(formatter)
        result.getConclusionDate() == conclusionDate.format(formatter)
        result.getNumberOfQuestions() == TOURNAMENT_NUMBER_OF_QUESTIONS
        result.getTopics().size() == 1
    }


    def "create a tournament no start date"(){
        given: 'a tournament with title, number of questions, topics but no start date'
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        def tournament = new TournamentDto()
        def user = new User()
        def topic = new TopicDto()
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
}
