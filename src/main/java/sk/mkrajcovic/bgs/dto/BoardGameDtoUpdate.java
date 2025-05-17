package sk.mkrajcovic.bgs.dto;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BoardGameDtoUpdate extends BoardGameDtoCreate {

	@NotBlank
	private String description;

	@NotNull
	private Integer minPlayers;

	@NotNull
	private Integer maxPlayers;

	@NotNull
	private Integer estimatedPlayTime;

	@NotNull
	@Valid
	private AgeRangeDtoUpdate ageRange;

	@Size(min = 1, max = 10)
	private Set<@NotBlank @Size(max = 100) String> authors = new HashSet<>(10);

	@NotNull
	private Boolean canPlayOnlyOnce;

	@NotNull
	@PositiveOrZero
	private Long version;

	
	// This is the problem !!!!! inheritance in this level and overriding properties
	@Getter
	@Setter
	public static class AgeRangeDtoUpdate extends AgeRangeDtoCreate {
		@NotNull
		private Integer minAge;
		@NotNull
		private Integer maxAge;
	}
}
