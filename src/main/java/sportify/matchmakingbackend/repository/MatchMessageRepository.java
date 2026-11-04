package sportify.matchmakingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportify.matchmakingbackend.model.MatchMessage;

import java.util.List;

@Repository
public interface MatchMessageRepository extends JpaRepository<MatchMessage, Long> {

    // последните 50, од најнова кон најстара (при отворање на чатот)
    List<MatchMessage> findTop50ByMatchIdOrderByIdDesc(Long matchId);

    // само новите пораки по последната што ја има апликацијата (polling)
    List<MatchMessage> findByMatchIdAndIdGreaterThanOrderByIdAsc(Long matchId, Long afterId);
}