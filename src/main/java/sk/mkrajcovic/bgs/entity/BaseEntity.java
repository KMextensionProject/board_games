package sk.mkrajcovic.bgs.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity {

	private static final ZoneId UTC = ZoneId.of("UTC");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	@Column(updatable = false)
	@CreatedBy
	private String createdBy;

	@PrePersist
	protected void runPrePersistOperations() {
		this.createdAt = LocalDateTime.now(UTC);
	}

	@PreUpdate
	protected void runPreUpdateOperations() {
		this.modifiedAt = LocalDateTime.now(UTC);
	}
}
