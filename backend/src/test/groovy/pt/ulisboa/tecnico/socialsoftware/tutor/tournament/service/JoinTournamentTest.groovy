package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.service

import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.Tournament
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentDto
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class JoinTournamentSpockTest extends Specification {
    static final USERNAME = 'username'
    public static final String COURSE_NAME = "Software Architecture"
    public static final String ACRONYM = "AS1"
    public static final String ACADEMIC_TERM = "1 SEM"
    
    def tournamentService

    def setup(){
        tournamentService = new TournamentService()
    }

    def "student joins open tournament"(){
        given:

        def user = new User('name', USERNAME, 1, User.Role.STUDENT)
        def courseExecution = new CourseExecution(course, ACRONYM, ACADEMIC_TERM, Course.Type.TECNICO)
        def tournament = new TournamentDto();
        def formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        def creationDate = LocalDateTime.now()
        def availableDate = LocalDateTime.now()
        def conclusionDate = LocalDateTime.now().plusDays(1)

        tournament.setStartDate(availableDate.format(formatter))
        tournament.setConclusionDate(conclusionDate.format(formatter))

        expect: false
    }

    def "student is already in tournament"(){
        //an exception is thrown
        expect: false
    }

    def "student tries to join closed tournament"(){
        //an exception is thrown
        expect: false
    }

    def "user not a student tries to join a tournament"(){
        //an exception is thrown
        expect: false
    }

    def "user tries to join a tournament belonging to a course he's not enrolled in"(){
        //an excepetion is thrown
        expect: false
    }


}
