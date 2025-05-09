package sk.mkrajcovic.bgs.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class LendLog extends BaseEntity {

	@ManyToOne
	private BoardGame boardGame;

	@ManyToOne
	private Borrower borrower;

	@Column(nullable = false)
	private LocalDate lendDate;
	private LocalDate returnDate;
	private String notes;
}
