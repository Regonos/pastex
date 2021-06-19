package pl.igormaculewicz.pastex.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="paste")
public class PasteEntity {

  @Id
  @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid DEFAULT gen_random_uuid()")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @Column(name="title", nullable = false)
  private String title;

  @Column(name="content", nullable = false)
  private String content;

  @Column(name="password")
  private String password;

  @Generated(GenerationTime.INSERT)
  @Column(name = "deletion_key", updatable = false, nullable = false, columnDefinition = "uuid DEFAULT gen_random_uuid()")
  private UUID deletionKey;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
  private OffsetDateTime createdAt;
}

