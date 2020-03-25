package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.administration.AdministrationService;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseService;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.QuestionService;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.api.QuestionController;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.dto.QuizDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.dto.StudentDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.dto.UserDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class TournamentController {
    //private static Logger logger = LoggerFactory.getLogger(TournamentController.class);
    static final Integer TOURNAMENT_NUMBER_OF_QUESTIONS = 3;
    static final Integer COURSE_EXECUTION_ID = 10;
    static final Integer TOURNAMENT_ID = 11;
    public static final String COURSE_NAME = "Software Architecture";
    public static final String ACRONYM = "AS1";
    public static final String ACADEMIC_TERM = "1 SEM";

    @Autowired
    private TournamentService tournamentService;


    /*TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }*/

    @GetMapping("/admin/courses/executions/{courseExecutionId}")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public  List<TournamentDto> getTournaments(@PathVariable Integer courseExecutionId){
        return this.tournamentService.getTournaments(courseExecutionId);
    }


    /*@PutMapping("/tournaments/{tournamentId}")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public TournamentDto addUser(@PathVariable Integer tournamentId, @RequestBody UserDto userDto) {
        return this.tournamentService.addUser(userDto.getId(), tournamentId);
    }*/

    @PostMapping("/tournaments")
    //@PreAuthorize("hasRole('ROLE_STUDENT')")
    public TournamentDto createTournament(@Valid @RequestBody TournamentDto tournamentDto) {
        System.out.println("PRINT");
        return tournamentService.createTournament(tournamentDto);

    }

    private void formatDates(TournamentDto tournament) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (tournament.getCurrentDate() != null && !tournament.getCurrentDate().matches("(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2})")){
            tournament.setCurrentDate(LocalDateTime.parse(tournament.getCurrentDate().replaceAll(".$", ""), DateTimeFormatter.ISO_DATE_TIME).format(formatter));
        }
        if (tournament.getConclusionDate() !=null && !tournament.getConclusionDate().matches("(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2})"))
            tournament.setConclusionDate(LocalDateTime.parse(tournament.getConclusionDate().replaceAll(".$", ""), DateTimeFormatter.ISO_DATE_TIME).format(formatter));

        if (tournament.getStartDate() !=null && !tournament.getStartDate().matches("(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2})"))
            tournament.setStartDate(LocalDateTime.parse(tournament.getStartDate().replaceAll(".$", ""), DateTimeFormatter.ISO_DATE_TIME).format(formatter));

    }
}
