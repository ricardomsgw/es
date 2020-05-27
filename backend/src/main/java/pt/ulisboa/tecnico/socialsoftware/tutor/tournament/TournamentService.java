package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.TopicRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.QuizService;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.dto.QuizDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.repository.QuizRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.statement.dto.SolvedQuizDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    /*@Autowired
    QuizService quizService;*/


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
        /*if(tournament.getQuiz() == null) {
            if(tournament.getUsers().size() > 1){
                tournament.setQuiz(generateQuiz(tournament));
                System.out.println(tournament.getQuiz().getId());
            }
        }*/
        entityManager.persist(tournament);
        TournamentDto tournamentDto = new TournamentDto(tournament);

        List<Integer> usersId = tournament.getUsers().stream().map(User::getId).collect(Collectors.toList());
        tournamentDto.setUsers(usersId);
        //tournamentDto.setQuizId(tournament.getQuiz().getId());

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
    public void cancelTournament(int tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(() ->new TutorException(TOURNAMENT_NOT_FOUND, tournamentId));
        //User user = userRepository.findById(userId).orElseThrow(() ->new TutorException(USER_NOT_FOUND, userId));

        tournament.cancel();
        tournamentRepository.delete(tournament);

    }
/*
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Quiz generateQuiz(Tournament tournament){
        Quiz quiz = new Quiz();
        quiz.setAvailableDate(tournament.getStartDate());
        quiz.setConclusionDate(tournament.getConclusionDate());
        //quiz.setCreationDate(LocalDateTime.now());
        quiz.setResultsDate(tournament.getConclusionDate());
        quiz.setScramble( false );
        quiz.setQrCodeOnly( false );
        //quiz.setTimed( true );
        quiz.setType(Quiz.QuizType.TOURNAMENT.toString());
        quiz.setOneWay( false );
        quiz.setTitle("TOURNAMENT_QUIZ");
        quiz.setCourseExecution(tournament.getCourseExecution());

        ArrayList<Integer> questionsId = new ArrayList<>();
        ArrayList<Question> questions = new ArrayList<>();
        for( Topic topic : tournament.getTopics()){
            List<Topic> topics = new ArrayList<>(tournament.getTopics());
            ArrayList<Integer> questionAux = new ArrayList<>();
            questionAux =tournamentRepository.getQuestionsByTopic(topic.getId());
            System.out.println(1);
            Integer i = 0;
            for(i= 0; i< tournament.getNumberOfQuestions(); i++){
                questionsId.add(questionAux.get(i));
            }
            System.out.println(questionsId.size());
        }

        for ( Integer id : questionsId){
            System.out.println(2);
            questions.add(questionRepository.findById(id).orElseThrow(() ->new TutorException(QUESTION_NOT_FOUND, id)));
        }
        System.out.println(questions.size());
        for(Question quest : questions){
            quizService.addQuestionToQuiz(quest.getId(),quiz.getId());
        }
        quizRepository.save(quiz);
        return quiz;
    }*/

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public boolean numberOfQuestionsValid(Tournament tournament) {
        ArrayList<Integer> questionsId = new ArrayList<Integer>();
        for (Topic topic : tournament.getTopics()) {
            ArrayList<Integer> questionAux = new ArrayList<>();
            questionAux =tournamentRepository.getQuestionsByTopic(topic.getId());
            for( Integer question : questionAux){
                questionsId.add(question);
            }
        }
        if (questionsId.size() > tournament.getNumberOfQuestions()) {
            return false;
        }
        return true;
    }
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<TournamentDto> getJoinedTournaments(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new TutorException(USER_NOT_FOUND, userId));

        return user.getTournaments().stream()
                .map(TournamentDto::new)
                .collect(Collectors.toList());
    }

}
