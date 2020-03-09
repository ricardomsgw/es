package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import org.springframework.data.annotation.Transient;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TournamentDto implements Serializable {

    private Integer tournamentId;
    private int numberOfQuestions;
    private List<TopicDto> topics = new ArrayList<>();
    private String startDate = null;
    private String currentDate = null;
    private String conclusionDate = null;

    @Transient
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TournamentDto(){}

    public TournamentDto(Tournament tournament){

        this.tournamentId = tournament.getId();
        this.numberOfQuestions = tournament.getNumberOfQuestions();
        if (tournament.getStartDate() != null)
            this.startDate = tournament.getStartDate().format(formatter);
        if (tournament.getCurrentDate() != null)
            this.currentDate = tournament.getCurrentDate().format(formatter);
        if (tournament.getConclusionDate() != null)
            this.conclusionDate = tournament.getConclusionDate().format(formatter);
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

    public List<TopicDto> getTopics() { return topics; }

    public void setTopics(TopicDto topic) { this.topics.add(topic); }


}
