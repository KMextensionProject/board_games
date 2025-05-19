package sk.mkrajcovic.bgs.dto;

import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import sk.mkrajcovic.bgs.web.validation.MinMaxFields;
import sk.mkrajcovic.bgs.web.validation.YouTubeUrl;

@Getter
@MinMaxFields(minField = "minPlayers", maxField = "maxPlayers")
public class BoardGameDtoUpdate implements BoardGameDtoIn {

	@NotBlank
	@Size(max = 50)
	private String title;

	@Size(max = 500)
	private String description;

	@Size(max = 120)
	@YouTubeUrl
	private String tutorialUrl;

	@NotNull
	@Positive
	private Integer minPlayers;

	@NotNull
	@Positive
	private Integer maxPlayers;

	@NotNull
	@Positive
	@Schema(description = "play time in minutes")
	private Integer estimatedPlayTime;

	@NotNull
	@Valid
	private AgeRangeDtoUpdate ageRange;

	@NotNull
	@Size(min = 1, max = 10)
	private Set<@NotBlank @Size(max = 100) String> authors = new HashSet<>(10);

	private Boolean isCooperative;

	@NotNull
	private Boolean canPlayOnlyOnce;

	@NotNull
	private Boolean isExtension;

	@NotNull
	@PositiveOrZero
	private Long version;

	@Getter
	@MinMaxFields(minField = "minAge", maxField = "maxAge")
	public static class AgeRangeDtoUpdate implements AgeRangeDtoIn {

		@NotNull
		@Min(3)
		@Max(18)
		private Integer minAge;

		@NotNull
		@Min(4)
		@Max(100)
		private Integer maxAge;
	}
}
