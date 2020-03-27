package pt.ulisboa.tecnico.socialsoftware.tutor.DDP;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
}
