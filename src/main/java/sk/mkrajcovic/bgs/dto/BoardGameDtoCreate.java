package sk.mkrajcovic.bgs.dto;

import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BoardGameDtoCreate {

	@NotBlank
	@Size(max = 50)
	private String title;
	
	@Positive 
	private Integer minPlayers;

	@Positive
	private Integer maxPlayers;

	@Positive
	@Schema(description = "play time in minutes")
	private Integer estimatedPlayTime;

	@Size(max = 10)
	private Set<@NotBlank @Size(max = 100) String> authors = new HashSet<>(10);
}
