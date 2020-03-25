package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.TopicRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.dto.QuizDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class TournamentService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseExecutionRepository courseExecutionRepository;

    @Autowired
    private TournamentRepository tournamentRepository;


    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TournamentDto createTournament(TournamentDto tournamentDto){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Tournament tournament = null;
        List<TopicDto> topicsDto = tournamentDto.getTopics();

        int courseExecutionId = tournamentDto.getCourseExecutionId();
        CourseExecution courseExecution = courseExecutionRepository.findById(courseExecutionId).orElseThrow(() -> new TutorException(COURSE_EXECUTION_NOT_FOUND, courseExecutionId));
        Course courseAux = courseExecution.getCourse();

        Set<Topic> topics = new HashSet<>();
        getTournamentTopics(topicsDto, courseAux, topics);

        LocalDateTime startDate = tournamentDto.getStartDateDate();
        LocalDateTime conclusionDate = tournamentDto.getConclusionDateDate();
        LocalDateTime currentDate = tournamentDto.getCurrentDateDate();
        Integer numberOfQuestions = tournamentDto.getNumberOfQuestions();
        Integer id = tournamentDto.getId();
        tournament = new Tournament(numberOfQuestions, startDate, conclusionDate, topics);
        //tournament.setId(id);
        tournament.setStatus(Tournament.Status.CREATED);
        tournament.setCourseExecution(courseExecution);
        tournament.setCurrentDate(currentDate);
        tournamentRepository.save(tournament);

        return new TournamentDto(tournament);
    }

    private void getTournamentTopics(List<TopicDto> topicsDto, Course courseAux, Set<Topic> topics) {
        Iterator<TopicDto> iterator = topicsDto.iterator();
        Topic topicAux;
        while(iterator.hasNext()){
            TopicDto topicDtoAux = iterator.next();
            topicAux = new Topic(courseAux, topicDtoAux);
            topics.add(topicAux);
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TournamentDto addUser(int userId, int tournamentId){
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() ->new TutorException(TOURNAMENT_NOT_FOUND, tournamentId));

        User user = userRepository.findById(userId).orElseThrow(() ->new TutorException(USER_NOT_FOUND, userId));
        tournament.addUser(user);

        entityManager.persist(tournament);
        return new TournamentDto(tournament);
    }

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<TournamentDto> getTournaments(int courseExecutionId) {
        CourseExecution courseExecution= courseExecutionRepository.findById(courseExecutionId).orElseThrow(() -> new TutorException(COURSE_NOT_FOUND, courseExecutionId));

        return courseExecution.getOpenedTournaments().stream()
                .map(TournamentDto::new)
                .collect(Collectors.toList());
    }
}
