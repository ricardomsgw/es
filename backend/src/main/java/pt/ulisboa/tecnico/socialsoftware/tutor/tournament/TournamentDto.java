package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import org.springframework.data.annotation.Transient;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TournamentDto implements Serializable {

    private Integer tournamentId;
    private int numberOfQuestions;
    private List<TopicDto> topics = new ArrayList<>();
    private String startDate = null;
    private String currentDate = null;
    private String conclusionDate = null;
    private Tournament.Status status;
    private int courseExecutionId;

    @Transient
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TournamentDto(){}

    public TournamentDto(Tournament tournament){

        this.tournamentId = tournament.getId();
        this.numberOfQuestions = tournament.getNumberOfQuestions();
        this.topics = tournament.getTopics().stream().map(topic -> { TopicDto topicDto = new TopicDto(topic); return topicDto; }).collect(Collectors.toList());
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

    public void setTopics(List<TopicDto> topics) { this.topics = topics; }

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
}
