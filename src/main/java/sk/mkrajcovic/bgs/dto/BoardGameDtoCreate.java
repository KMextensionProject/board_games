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
import sk.mkrajcovic.bgs.web.validation.MinMaxFields;
import sk.mkrajcovic.bgs.web.validation.Year;
import sk.mkrajcovic.bgs.web.validation.YouTubeUrl;

@Getter
@MinMaxFields(minField = "minPlayers", maxField = "maxPlayers")
public class BoardGameDtoCreate implements BoardGameDtoIn {

	@NotBlank
	@Size(max = 50)
	private String title; // trim this

	@Size(max = 500)
	private String description; // trim this

	@Positive
	private Integer minPlayers;

	@Positive
	private Integer maxPlayers;

	@Positive
	@Schema(description = "play time in minutes")
	private Integer estimatedPlayTime;

	@Year
	@Schema(description = "value between 1970 and the current year (both inclusive)")
	private Integer year;

	@Valid
	private AgeRangeDtoCreate ageRange;

	@Size(max = 10)
	private Set<@NotBlank @Size(max = 100) String> authors = new HashSet<>(10);

	@Size(max = 120)
	@YouTubeUrl(message = "The tutorial URL must be a valid YouTube link.")
	private String tutorialUrl; // trim this

	private Boolean isCooperative;
	private Boolean canPlayOnlyOnce;
	private Boolean isExtension = Boolean.FALSE;

	@Getter
	@MinMaxFields(minField = "minAge", maxField = "maxAge")
	public static class AgeRangeDtoCreate implements AgeRangeDtoIn {

		@Min(3)
		@Max(18)
		private Integer minAge;

		@Min(4)
		@Max(100)
		private Integer maxAge;
	}
}
