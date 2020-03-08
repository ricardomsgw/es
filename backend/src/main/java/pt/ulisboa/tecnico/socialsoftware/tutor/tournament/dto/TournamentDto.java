package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.dto;

import org.springframework.data.annotation.Transient;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.TopicDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.domain.Tournament;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TournamentDto implements Serializable {
    private Integer id;
    private String title;
    private String startDate = null;
    private String currentDate = null;
    private String conclusionDate = null;
    private int numberOfQuestions;
    private int numberOfTopics;
    private List<QuestionDto> questions = new ArrayList<>();
    private List<TopicDto> topics = new ArrayList<>();

    @Transient
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TournamentDto(){
    }

    public TournamentDto(Tournament tournament){
        this.id = tournament.getId();
        this.title = tournament.getTitle();
        this.numberOfQuestions = tournament.getNumberOfQuestions();
        //this.numberOfAnswers = tournament.getTournamentAnswers().size();
        this.numberOfTopics = tournament.getTopics().size();

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKey() {
        return id;
    }

    public void setKey(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions){
        this.numberOfQuestions = numberOfQuestions;
    }

    public Integer getNumberOfQuestions(){
        return numberOfQuestions;
    }

    public void setTopics(TopicDto topic){
        this.topics.add(topic);
    }

    public List<TopicDto> getTopics(){
        return topics;
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

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public LocalDateTime getCreationDateDate() {
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
