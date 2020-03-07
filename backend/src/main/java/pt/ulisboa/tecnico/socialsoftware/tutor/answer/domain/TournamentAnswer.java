package pt.ulisboa.tecnico.socialsoftware.tutor.answer.domain;

import pt.ulisboa.tecnico.socialsoftware.tutor.course.CourseExecution;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.domain.Tournament;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "tournament_answers")
public class TournamentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "answer_date")
    private LocalDateTime answerDate;

    private boolean completed;

    @Column(columnDefinition = "boolean default false")
    private boolean usedInStatistics;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tournamentAnswer", fetch=FetchType.LAZY, orphanRemoval=true)
    private List<QuestionAnswer> questionAnswers = new ArrayList<>();

    public TournamentAnswer() {
    }

    public TournamentAnswer(User user, Tournament tournament) {
        this.completed = false;
        this.usedInStatistics = false;
        this.user = user;
        user.addTournamentAnswer(this);
        this.tournament = tournament;
        tournament.addTournamentAnswer(this);

        List<TournamentQuestion> tournamentQuestions = new ArrayList<>(tournament.getTournamentQuestions());
        //TODO
        /*if (quiz.getScramble()) {
            Collections.shuffle(quizQuestions);
        }*/

        for (int i = 0; i < tournamentQuestions.size(); i++) {
            new QuestionAnswer(this, tournamentQuestions.get(i), i);
        }
    }

    public void remove() {
        user.getQuizAnswers().remove(this);
        user = null;

        tournament.getTournamentAnswers().remove(this);
        tournament = null;

        for (QuestionAnswer questionAnswer: getQuestionAnswers()) {
            questionAnswer.remove();
        }

        questionAnswers.clear();
    }

    public boolean canResultsBePublic(CourseExecution courseExecution) {
        return getCompleted() &&
                getTournament().getCourseExecution() == courseExecution &&
                !(getTournament().getConclusionDate().isAfter(LocalDateTime.now()));
    }

    /*public void calculateStatistics() {
        if (!this.usedInStatistics) {
            user.increaseNumberOfQuizzes(getQuiz().getType());

            getQuestionAnswers().forEach(questionAnswer -> {
                user.increaseNumberOfAnswers(getQuiz().getType());
                if (questionAnswer.getOption() != null && questionAnswer.getOption().getCorrect()) {
                    user.increaseNumberOfCorrectAnswers(getQuiz().getType());
                }

            });

            getQuestionAnswers().forEach(questionAnswer ->
                    questionAnswer.getQuizQuestion().getQuestion().addAnswerStatistics(questionAnswer));

            this.usedInStatistics = true;
        }
    }*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(LocalDateTime answerDate) {
        this.answerDate = answerDate;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isUsedInStatistics() {
        return usedInStatistics;
    }

    public void setUsedInStatistics(boolean usedInStatistics) {
        this.usedInStatistics = usedInStatistics;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<QuestionAnswer> getQuestionAnswers() {
        if (questionAnswers == null) {
            questionAnswers = new ArrayList<>();
        }
        return questionAnswers;
    }

    public void addQuestionAnswer(QuestionAnswer questionAnswer) {
        if (questionAnswers == null) {
            questionAnswers = new ArrayList<>();
        }
        questionAnswers.add(questionAnswer);
    }


}
