package sk.mkrajcovic.bgs.dto;

import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BoardGameDtoUpdate implements BoardGameDtoIn {

	@NotBlank
	@Size(max = 50)
	private String title;
	
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

	@Size(min = 1, max = 10)
	private Set<@NotBlank @Size(max = 100) String> authors = new HashSet<>(10);

	@NotNull
	@PositiveOrZero
	private Long version;
}
