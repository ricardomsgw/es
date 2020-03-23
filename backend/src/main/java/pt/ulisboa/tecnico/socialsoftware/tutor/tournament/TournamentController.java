package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseService;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.dto.StudentDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @GetMapping("/tournaments")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public TournamentDto createTournament(@RequestBody TournamentDto tournamentDto) {
        return tournamentService.createTournament(tournamentDto);
    }

    @PutMapping("/tournaments/{tournamentId}")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public TournamentDto addUser(@PathVariable Integer tournamentId, @Valid @RequestBody UserDto userDto) {
        return this.tournamentService.addUser(userDto.getId(), tournamentId);
    }
}
