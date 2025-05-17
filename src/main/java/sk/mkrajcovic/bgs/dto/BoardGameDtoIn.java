package sk.mkrajcovic.bgs.dto;

import java.util.Set;

import sk.mkrajcovic.bgs.dto.BoardGameDtoCreate.AgeRangeDtoCreate;

public interface BoardGameDtoIn {

	String getTitle();
	String getDescription();
	Integer getMinPlayers();
	Integer getMaxPlayers();
	Integer getEstimatedPlayTime();
	Set<String> getAuthors();
	// here is the problem
//	AgeRangeDtoCreate getAgeRange();
	Boolean getIsCooperative();
	Boolean getCanPlayOnlyOnce();
}
