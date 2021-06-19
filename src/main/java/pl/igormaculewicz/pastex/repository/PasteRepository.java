package pl.igormaculewicz.pastex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.igormaculewicz.pastex.entity.PasteEntity;

import java.util.UUID;

@Repository
public interface PasteRepository extends JpaRepository<PasteEntity, UUID> {

  boolean existsByIdAndPasswordIsNull(UUID id);

  boolean existsByIdAndPassword(UUID id, String password);
  boolean existsByIdAndDeletionKey(UUID id, UUID deletionKey);
}
