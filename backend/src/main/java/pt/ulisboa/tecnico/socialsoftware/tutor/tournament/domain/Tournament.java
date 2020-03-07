package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.QuizAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain.TournamentAnswer;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.dto.QuestionDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.Quiz;
import pt.ulisboa.tecnico.socialsoftware.tutor.quiz.domain.QuizQuestion;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.dto.TournamentDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.QUIZ_NOT_CONSISTENT;

@Entity
@Table(
        name = "tournaments",
        indexes = {
                @Index(name = "tournaments_indx_0", columnList = "key")
        })

public class Tournament {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true, nullable = false)
    private Integer key;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "available_date")
    private LocalDateTime currentDate;

    @Column(name = "conclusion_date")
    private LocalDateTime conclusionDate;

    @Column(nullable = false)
    private String title = "Title";

    @Column(nullable = false)
    private Integer numberOfQuestions;

    @Column(nullable = false)
    private boolean opened;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "topic", fetch = FetchType.LAZY, orphanRemoval=true)
    private Set<Topic> topics = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "course_execution_id")
    private CourseExecution courseExecution;

    public Tournament() {}

    public Tournament(TournamentDto tournamentDto) {

        checkQuestions(tournamentDto.getQuestions());
        this.opened = false;
        this.key = tournamentDto.getKey();
        setTitle(tournamentDto.getTitle());
        this.startDate = tournamentDto.getCreationDateDate();
        //setAvailableDate(tournamentDto.getAvailableDateDate());
        setConclusionDate(tournamentDto.getConclusionDateDate());

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        checkTitle(title);
        this.title = title;
    }

    public LocalDateTime getCreationDate() {
        return startDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.startDate = creationDate;
    }


    public LocalDateTime getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(LocalDateTime conclusionDate) {
        checkConclusionDate(conclusionDate);
        this.conclusionDate = conclusionDate;
    }

    public CourseExecution getCourseExecution() {
        return courseExecution;
    }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
        courseExecution.addTournament(this);
    }

    public Set<Topic> getTopics() {
        return topics;
    }

    public Integer getNumberOfQuestions(){
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions){
        this.numberOfQuestions = numberOfQuestions;
    }

    public void startTournament(){
        if(currentDate == startDate) {
            this.opened = true;
            courseExecution.addTournament(this);
        }
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", creationDate=" + startDate +
                ", conclusionDate=" + conclusionDate +
                ", scramble=" + //scramble +
                ", title='" + title + '\'' +
                ", type=" + //type +
                ", id=" + id +
                ", series=" + //series +
                ", version='" + //version + '\'' +
                '}';
    }

    private void checkTitle(String title) {
        if (title == null || title.trim().length() == 0) {
            throw new TutorException(QUIZ_NOT_CONSISTENT, "Title");
        }
    }

    private void checkAvailableDate(LocalDateTime availableDate) {
        if (availableDate == null) {
            throw new TutorException(QUIZ_NOT_CONSISTENT, "Available date");
        }
        if (this.conclusionDate != null && conclusionDate.isBefore(availableDate)) {
            throw new TutorException(QUIZ_NOT_CONSISTENT, "Available date");
        }
    }

    private void checkConclusionDate(LocalDateTime conclusionDate) {
        if (conclusionDate != null) {
            throw new TutorException(QUIZ_NOT_CONSISTENT, "Conclusion date " + conclusionDate);
        }
    }

    private void checkQuestions(List<QuestionDto> questions) {
        if (questions != null) {
            for (QuestionDto questionDto : questions) {
                if (questionDto.getSequence() != questions.indexOf(questionDto) + 1) {
                    throw new TutorException(QUIZ_NOT_CONSISTENT, "sequence of questions not correct");
                }
            }
        }
    }

    public void remove() {
        //checkCanChange();

        courseExecution.getTournaments().remove(this);
        courseExecution = null;
    }
}
