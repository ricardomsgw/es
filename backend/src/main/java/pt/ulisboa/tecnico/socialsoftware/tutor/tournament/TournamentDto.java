package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import org.springframework.data.annotation.Transient;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.dto.UserDto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class TournamentDto implements Serializable {

    private Integer tournamentId;
    private Integer creatorId;
    private int numberOfQuestions;
    private List<Integer> topics = new ArrayList<>();
    private List<Integer> users = new ArrayList<>();
    private String startDate = null;
    private String currentDate = null;
    private String conclusionDate = null;
    private Tournament.Status status;
    private int courseExecutionId;

    @Transient
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TournamentDto(){}

    public TournamentDto(Tournament tournament){

        this.creatorId = tournament.getCreator_tournament();
        this.currentDate = LocalDateTime.now().format(formatter);
        this.tournamentId = tournament.getId();
        this.status = tournament.getStatus();
        this.courseExecutionId = tournament.getCourseExecution().getId();
        this.numberOfQuestions = tournament.getNumberOfQuestions();
        this.topics = defTopics(tournament);
        checkDates(tournament);

    }

    private List<Integer> defTopics(Tournament tournament) {
        List<Integer> listAux = new ArrayList<>();
        Iterator iterator = tournament.getTopics().iterator();
        while(iterator.hasNext()){
            Topic topicAux = (Topic) iterator.next();
            Integer idTopicAux = topicAux.getId();
            listAux.add(idTopicAux);
        }
        return listAux;
    }

    private void checkDates(Tournament tournament) {
        if (tournament.getStartDate() != null)
            this.startDate = tournament.getStartDate().format(formatter);
        if (tournament.getCurrentDate() != null)
            this.currentDate = tournament.getCurrentDate().format(formatter);
        if (tournament.getConclusionDate() != null)
            this.conclusionDate = tournament.getConclusionDate().format(formatter);
    }

    public Integer getId() {
        return tournamentId;
    }

    public void setId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public int getCourseId() {
        return tournamentId;
    }

    public void setCourseId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(String conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public int getNumberOfQuestions() { return numberOfQuestions; }

    public void setNumberOfQuestions(int numberOfQuestions) { this.numberOfQuestions = numberOfQuestions; }

    public List<Integer> getUsers() { return users;    }

    public void setUsers(List<Integer> users) { this.users = users;  }

    public void addUser(Integer userId){ this.users.add(userId);}

    public List<Integer> getTopics() { return topics; }

    public void addTopics(Integer topicId) { this.topics.add(topicId); }

    public Tournament.Status getStatus() {
        return status;
    }

    public void setStatus(Tournament.Status status) {
        this.status = status;
    }

    public int getCourseExecutionId() {
        return courseExecutionId;
    }

    public void setCourseExecutionId(int courseExecutionId) {
        this.courseExecutionId = courseExecutionId;
    }

    public LocalDateTime getStartDateDate() {
        if (getStartDate() == null || getStartDate().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(getStartDate(), formatter);
    }

    public LocalDateTime getCurrentDateDate() {
        if (getCurrentDate() == null || getCurrentDate().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(getCurrentDate(), formatter);
    }

    public LocalDateTime getConclusionDateDate() {
        if (getConclusionDate() == null || getConclusionDate().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(getConclusionDate(), formatter);
    }
}
