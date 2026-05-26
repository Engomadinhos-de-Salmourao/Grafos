package scrit.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scrit.project.domain.Destino;

import java.util.Optional;

@Repository
public interface DestinoRepository extends JpaRepository<Destino, Integer> {
    Optional<Destino> findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);
}
