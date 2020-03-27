package pt.ulisboa.tecnico.socialsoftware.tutor.DDP;

import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import java.io.Serializable;

public class SubmissionDto implements Serializable {
    private Integer id;
    private User user;
    private Question question;

    public SubmissionDto() {
    }

    public SubmissionDto(Submission message) {
        this.id = message.getId();
        this.user = message.getUser();
        this.question = message.getQuestion();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "id=" + id +
                ", user=" + user +
                ", question=" + question +
                '}';
    }
}
