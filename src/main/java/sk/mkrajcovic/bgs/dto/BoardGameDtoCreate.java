package sk.mkrajcovic.bgs.dto;

import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BoardGameDtoCreate implements BoardGameDtoIn {

	@NotBlank
	@Size(max = 50)
	private String title;

	@Size(max = 500)
	private String description;

	@Positive
	private Integer minPlayers;

	@Positive
	private Integer maxPlayers;

	@Positive
	@Schema(description = "play time in minutes")
	private Integer estimatedPlayTime;

	@Valid
	private AgeRangeDtoCreate ageRange;

	@Size(max = 10)
	private Set<@NotBlank @Size(max = 100) String> authors = new HashSet<>(10);

	private Boolean isCooperative;
	private Boolean canPlayOnlyOnce;

	// add language?
	// link / picture?
	// publisher? (Dino)

	@Getter
	public static class AgeRangeDtoCreate implements AgeRangeDtoIn {
		@Min(3)
		@Max(18)
		private Integer minAge;

		@Min(4)
		@Max(100)
		private Integer maxAge;
		// validate minAge is <= than maxAge
	}
}
