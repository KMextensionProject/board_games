package sk.mkrajcovic.bgs.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.persistence.Column;
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
@MappedSuperclass
public abstract class BaseEntity {

	private static final ZoneId UTC = ZoneId.of("UTC");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;

	@PrePersist
	protected void runPrePersistOperations() {
		this.createdAt = LocalDateTime.now(UTC);
	}

	@PreUpdate
	protected void runPreUpdateOperations() {
		this.modifiedAt = LocalDateTime.now(UTC);
	}
}
