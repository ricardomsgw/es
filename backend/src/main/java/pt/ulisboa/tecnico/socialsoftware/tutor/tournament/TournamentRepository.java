package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.socialsoftware.tutor.course.Course;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.*;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface TournamentRepository extends JpaRepository<Tournament, Integer>{
    @Query(value = "select * from tournaments t where t.id = :id", nativeQuery = true)
    Optional<Tournament> findById(Integer id);
}
