package pt.ulisboa.tecnico.socialsoftware.tutor.DDP;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.domain.Question;
import pt.ulisboa.tecnico.socialsoftware.tutor.question.repository.QuestionRepository;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.UserRepository;

import java.sql.SQLException;

public class MessageService {

    @Autowired
    private UserRepository userRepository;

    private QuestionRepository questionRepository;
    private SubmissionRepository submissionRepository;

    @Retryable(
            value = { SQLException.class },
            backoff = @Backoff(delay = 5000))
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public SubmissionDto createMessage(Integer userId, Integer questionId, SubmissionDto messDto){
        User user = userRepository.findById(userId).orElseThrow(() -> new TutorException(ErrorMessage.USER_NOT_FOUND, userId));
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new TutorException(ErrorMessage.QUESTION_NOT_FOUND, questionId));
        Submission message = new Submission(user, question, messDto);

        submissionRepository.save(message);

        return new SubmissionDto(message);
    }



}
