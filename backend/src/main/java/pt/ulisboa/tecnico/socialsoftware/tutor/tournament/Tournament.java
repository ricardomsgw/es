package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "number_of_questions")
    private Integer numberOfQuestions;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "current_date")
    private LocalDateTime currentDate;

    @Column(name = "conclusion_date")
    private LocalDateTime conclusionDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch=FetchType.LAZY, orphanRemoval=true)
    private Set<Topic> topics = new HashSet<>();

    public Tournament() {}

    public Tournament(Integer numberOfQuestions, LocalDateTime startDate, LocalDateTime conclusionDate) {
        this.numberOfQuestions = numberOfQuestions;
        this.startDate = startDate;
        this.conclusionDate = conclusionDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumberOfQuestions() { return numberOfQuestions; }

    public void setNumberOfQuestions(Integer numberOfQuestions) { this.numberOfQuestions = numberOfQuestions; }

    public Set<Topic> getTopics() { return topics; }

    public void setTopics(Topic topic) { this.topics.add(topic); }

    public LocalDateTime getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(LocalDateTime conclusionDate) {
        this.conclusionDate = conclusionDate;
    }

    public LocalDateTime getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDateTime currentDate) {
        this.currentDate = currentDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
