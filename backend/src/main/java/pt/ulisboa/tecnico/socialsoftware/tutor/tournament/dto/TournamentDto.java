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
    private String creationDate = null;
    private String availableDate = null;
    private String conclusionDate = null;
    private Quiz.QuizType type;
    private Integer series;
    private String version;
    private int numberOfQuestions;
    private int numberOfAnswers;
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
        this.numberOfQuestions = tournament.getTournamentQuestions().size();
        //this.numberOfAnswers = tournament.getTournamentAnswers().size();
        this.numberOfTopics = tournament.getTournamentTopics().size();

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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
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
        if (getCreationDate() == null || getCreationDate().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(getCreationDate(), formatter);
    }

    public LocalDateTime getAvailableDateDate() {
        if (getAvailableDate() == null || getAvailableDate().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(getAvailableDate(), formatter);
    }

    public LocalDateTime getConclusionDateDate() {
        if (getConclusionDate() == null || getConclusionDate().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(getConclusionDate(), formatter);
    }
}
