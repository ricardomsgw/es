package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.Tournament;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TournamentRepository extends JpaRepository<Tournament, Integer>{
    @Query(value = "select t.questions_id from topics_questions t,questions q WHERE t.topics_id = :topicid AND  q.status= 'AVAILABLE'", nativeQuery = true)
    Integer getQuestionsByTopic(int topicid);
}
