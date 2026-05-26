package scrit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scrit.project.domain.Roteiro;

import java.util.List;

@Repository
public interface RoteiroRepository extends JpaRepository<Roteiro, Integer> {
    List<Roteiro> findByDestinoId(Integer destinoId);
}
