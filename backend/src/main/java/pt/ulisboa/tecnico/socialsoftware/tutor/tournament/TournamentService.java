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
        List<Integer> topicsDto = tournamentDto.getTopics();
        int creatorId = tournamentDto.getCreatorId();
        User userCreator = userRepository.findById(creatorId).orElseThrow(() -> new TutorException(USER_NOT_FOUND, creatorId));
        int courseExecutionId = tournamentDto.getCourseExecutionId();
        CourseExecution courseExecution = courseExecutionRepository.findById(courseExecutionId).orElseThrow(() -> new TutorException(COURSE_EXECUTION_NOT_FOUND, courseExecutionId));
        Course courseAux = courseExecution.getCourse();

        Set<Topic> topics = defTopics(topicsDto);
        LocalDateTime startDate = tournamentDto.getStartDateDate();
        LocalDateTime conclusionDate = tournamentDto.getConclusionDateDate();
        LocalDateTime currentDate = tournamentDto.getCurrentDateDate();
        Integer numberOfQuestions = tournamentDto.getNumberOfQuestions();
        Integer id = tournamentDto.getId();
        tournament = new Tournament(numberOfQuestions, startDate, conclusionDate, topics, userCreator);
        tournament.setStatus(Tournament.Status.OPENED);
        tournament.setCourseExecution(courseExecution);
        tournament.setCurrentDate(currentDate);
        tournament.setConclusionDate(conclusionDate);
        tournament.setStartDate(startDate);
        tournamentRepository.save(tournament);
        TournamentDto tournamentAux = new TournamentDto(tournament);
        return tournamentAux;
    }

    private Set<Topic> defTopics (List<Integer> topics){
        Iterator iterator = topics.iterator();
        Set <Topic> topicAux = new HashSet<>();
        while(iterator.hasNext()){
            Integer aux = (Integer) iterator.next();
            Topic topicAuxiliar = (Topic) topicRepository.findById(aux).get();
            topicAux.add(topicAuxiliar);
        }
        return topicAux;
    }

    private Set<Topic> getTournamentTopics(List<TopicDto> topicsDto, Course courseAux) {
        Iterator iterator = topicsDto.iterator();
        Topic topicAux;
        Set<Topic> topicsAux = new HashSet<>();
        while(iterator.hasNext()){
            TopicDto topicDtoAux = (TopicDto) iterator.next();
            topicAux = new Topic(courseAux, topicDtoAux);
            topicsAux.add(topicAux);
        }
        return topicsAux;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TournamentDto addUser(int userId, int tournamentId){
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() ->new TutorException(TOURNAMENT_NOT_FOUND, tournamentId));


        User user = userRepository.findById(userId).orElseThrow(() ->new TutorException(USER_NOT_FOUND, userId));
        tournamentRepository.findById(tournamentId).get().addUser(user);

        entityManager.persist(tournament);
        TournamentDto tournamentDto = new TournamentDto(tournament);

        List<Integer> usersId = tournament.getUsers().stream().map(User::getId).collect(Collectors.toList());
        tournamentDto.setUsers(usersId);
        return tournamentDto;
    }

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<TournamentDto> getTournaments(int courseExecutionId) {
        CourseExecution courseExecution= courseExecutionRepository.findById(courseExecutionId).orElseThrow(() -> new TutorException(COURSE_EXECUTION_NOT_FOUND, courseExecutionId));
        List<Integer> listAux = new ArrayList<>();
        Iterator it = courseExecution.getOpenedTournaments().iterator();
        while(it.hasNext()){
            Tournament tAux = (Tournament) it.next();

        }
        return courseExecution.getOpenedTournaments().stream()
                .map(TournamentDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void cancelTournament(int userId, int tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() ->new TutorException(TOURNAMENT_NOT_FOUND, tournamentId));
        User user = userRepository.findById(userId).orElseThrow(() ->new TutorException(USER_NOT_FOUND, userId));
        if ((int)user.getId() == (int)tournament.getCreator_tournament()) {
            tournament.cancel();
            tournamentRepository.delete(tournament);
        }
        else {
            System.out.println(user.getId());
            System.out.println(tournament.getCreator_tournament());
            throw new TutorException(USER_NOT_CREATOR);
        }
    }

}
