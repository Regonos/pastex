package pl.igormaculewicz.pastex.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.igormaculewicz.pastex.entity.PasteEntity;
import pl.igormaculewicz.pastex.model.PasteListModel;
import pl.igormaculewicz.pastex.model.PasteInputModel;
import pl.igormaculewicz.pastex.model.PasteOutputModel;
import pl.igormaculewicz.pastex.repository.PasteRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasteService {

  private final PasteRepository pasteRepository;
  private final ObjectMapper objectMapper;

  public boolean isPastePrivate(UUID id) {
    return !pasteRepository.existsByIdAndPasswordIsNull(id);
  }

  public PasteOutputModel getPaste(UUID id) {
    PasteEntity entity = pasteRepository.getById(id);
    return objectMapper.convertValue(Hibernate.unproxy(entity), PasteOutputModel.class);
  }

  public boolean isPasswordCorrect(UUID id, String password) {
    return pasteRepository.existsByIdAndPassword(id, password);
  }

  public boolean isDeletionKeyCorrect(UUID id, UUID deletionKey){
    return pasteRepository.existsByIdAndDeletionKey(id, deletionKey);
  }

  public void deletePaste(UUID id){
    pasteRepository.deleteById(id);
  }

  public Page<PasteListModel> getPastes(Pageable pageable) {
    return pasteRepository.findAll(pageable)
      .map(entity -> new PasteListModel(entity.getId(), entity.getTitle(), entity.getCreatedAt(), StringUtils.hasText(entity.getPassword())));
  }

  public PasteOutputModel addPaste(PasteInputModel pasteModel) {

    if (!StringUtils.hasText(pasteModel.getPassword())) {
      pasteModel.setPassword(null);
    }

    PasteEntity entity = objectMapper.convertValue(pasteModel, PasteEntity.class);

    entity = pasteRepository.save(entity);

    return objectMapper.convertValue(entity, PasteOutputModel.class);
  }

}
