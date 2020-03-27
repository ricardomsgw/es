package pt.ulisboa.tecnico.socialsoftware.tutor.DDP;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import javax.persistence.*;

@Entity
@Table(name = "submission")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "questions")
    private Question question;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "users")
    private User user;


    public Submission() {
    }

    public Submission(User user, Question question, SubmissionDto MessageDto) {
        this.id = MessageDto.getId();
        this.question = question;
        question.addMessage(this);
        this.user = user;
        user.addMessage(this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", question=" + question +
                ", user=" + user +
                '}';
    }
}
