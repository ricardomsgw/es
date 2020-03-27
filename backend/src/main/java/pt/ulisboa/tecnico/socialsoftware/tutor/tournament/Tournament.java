package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Topic;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.sql.Date;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.COURSE_EXECUTION_ACRONYM_IS_EMPTY;

@Entity
@Table(name = "tournaments")
public class Tournament {

    public enum Status {CREATED, OPENED, CLOSED, CANCELED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Tournament.Status status;

    @Column(name = "number_of_questions")
    private Integer numberOfQuestions;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "today_date")
    private LocalDateTime currentDate;

    @Column(name = "conclusion_date")
    private LocalDateTime conclusionDate;


    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY, mappedBy = "tournaments")
    private Set<Topic> topics = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    private Set<User> users = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "course_execution_id")
    private CourseExecution courseExecution;

    public Tournament() {}

    public Tournament(Integer numberOfQuestions, LocalDateTime startDate, LocalDateTime conclusionDate, Set<Topic> topics) {

        this.currentDate = LocalDateTime.now();
        checkInputs(numberOfQuestions, startDate, conclusionDate, topics);
        this.numberOfQuestions = numberOfQuestions;
        this.startDate = startDate;
        this.conclusionDate = conclusionDate;
        this.status = Status.CREATED;
        this.topics = topics;

    }

    private void checkInputs(Integer numberOfQuestions, LocalDateTime startDate, LocalDateTime conclusionDate, Set<Topic> topics) {
        if (numberOfQuestions <= 0) {
            throw new TutorException(TOURNAMENT_NO_NUMBER_OF_QUESTIONS);
        }

        if (!checkStartDate(startDate)) {
            throw new TutorException(TOURNAMENT_WITH_DATA_NO_VALID);
        }

        if (startDate.isAfter(conclusionDate)){
            throw new TutorException(TOURNAMENT_WITH_DATA_NO_VALID);
        }

        if (!checkConclusionDate(conclusionDate)) {
            throw new TutorException(TOURNAMENT_WITH_DATA_NO_VALID);
        }

        if (topics.size() == 0) {
            throw new TutorException(TOURNAMENT_NO_TOPICS);
        }
    }

    public boolean checkStartDate (LocalDateTime startDate) {
        if (startDate == null) {
            return false;
        }
        else if (currentDate != null && startDate.isBefore(currentDate)){
            return false;
        }

        return true;
    }

    public boolean checkConclusionDate (LocalDateTime conclusionDate) {
        if (conclusionDate == null) {
            return false;
        }

        if (startDate != null && conclusionDate.isBefore(startDate)){
            return false;
        }
        return true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumberOfQuestions() { return numberOfQuestions; }

    public void setNumberOfQuestions(Integer numberOfQuestions) { this.numberOfQuestions = numberOfQuestions; }


    public void addTopics(Set<Topic> topics) {

        this.topics = topics;
    }

    public LocalDateTime getConclusionDate() {
        return conclusionDate;
    }

    public void setConclusionDate(LocalDateTime conclusionDate) {
        if(!checkConclusionDate(conclusionDate)){
            throw new TutorException(TOURNAMENT_WITH_DATA_NO_VALID);
        }
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
        if(!checkStartDate(startDate)){
            throw new TutorException(TOURNAMENT_WITH_DATA_NO_VALID);
        }
        this.startDate = startDate;
    }

    public Tournament.Status getStatus() {
        return status;
    }

    public void setStatus(Tournament.Status status) {
        this.status = status;
    }

    public void openTournament(){
        setStatusToOpened();
    }

    private void setStatusToOpened() {
        if (this.status != Status.CREATED) {
            throw new TutorException(TOURNAMENT_IS_NOT_CREATED);
        }
        Status status = Status.OPENED;
        setStatus(status);
    }

    public CourseExecution getCourseExecution() {
        return courseExecution;
    }

    public void setCourseExecution(CourseExecution courseExecution) {
        this.courseExecution = courseExecution;
        courseExecution.addTournament(this);
    }

    public Set<Topic> getTopics() { return topics; }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }

    public Set<User> getUsers(){ return users;}

    
    public void addUser(User user) {
        checkToAddUser(user);
        users.add(user);
        user.addTournaments(this);
    }


    private void checkToAddUser(User user) {
        if (status != Status.OPENED)
            throw new TutorException(TOURNAMENT_NOT_OPEN);
        if (user.getRole() != User.Role.STUDENT)
            throw new TutorException(TOURNAMENT_NOT_ELEGIBLE);
        CourseExecution courseExec = getCourseExecution();
        Set<CourseExecution> usercourseexecs = user.getCourseExecutions();
        if (!(usercourseexecs.contains(courseExec)))
            throw new TutorException(TOURNAMENT_NOT_ELEGIBLE);
        if (this.getUsers().contains(user))
            throw new TutorException(TOURNAMENT_ALREADY_JOINED);
    }
}
