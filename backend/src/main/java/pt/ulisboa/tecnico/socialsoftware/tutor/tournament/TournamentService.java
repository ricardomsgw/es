package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.TopicRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.dto.QuizDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;

@Service
public class TournamentService {

    public TournamentDto createTournament(TournamentDto tournamentDto) {
        return null;
    }
    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private CourseExecutionRepository courseExecutionRepository;

    @Autowired
    private TopicRepository topicRepository;

    /*@Transactional(isolation = Isolation.REPEATABLE_READ)
    public TournamentDto findById(Integer tournamentId) {
        return this.tournamentRepository.findById(tournamentId).map(tournament -> new TournamentDto(tournament))
                .orElseThrow(() -> new TutorException(TOURNAMENT_NOT_FOUND));
    }*/
    /*
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public TournamentDto createTournament(TournamentDto tournamentDto){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Tournament tournament = tournamentRepository.findById(tournamentDto.getId()).orElse(null);

        if (tournament == null){

            List<TopicDto> topicsDto = tournamentDto.getTopics();
            int courseExecutionId = tournamentDto.getCourseExecutionId();
            CourseExecution courseExecution = courseExecutionRepository.findById(courseExecutionId).orElseThrow(() -> new TutorException(COURSE_EXECUTION_NOT_FOUND, courseExecutionId));
            Course courseAux = courseExecution.getCourse();
            Set<Topic> topics = new HashSet<>();
            Iterator <TopicDto>  iterator = topicsDto.iterator();
            Topic topicAux;
            while(iterator.hasNext()){
                TopicDto topicDtoAux = iterator.next();
                topicAux = new Topic(courseAux, topicDtoAux);
                topics.add(topicAux);
            }
            LocalDateTime startDate = LocalDateTime.parse(tournamentDto.getStartDate(), formatter);
            LocalDateTime conclusionDate = LocalDateTime.parse(tournamentDto.getConclusionDate(), formatter);
            Integer numberOfQuestions = tournamentDto.getNumberOfQuestions();
            tournament = new Tournament();
            tournament.setStartDate(startDate);
            tournament.setConclusionDate(conclusionDate);
            tournament.setNumberOfQuestions(numberOfQuestions);
            tournament.setTopics(topics);
            tournament.setStatus(Tournament.Status.CREATED);
            tournamentRepository.save(tournament);
        }
        return new TournamentDto(tournament);
    }*/
}
