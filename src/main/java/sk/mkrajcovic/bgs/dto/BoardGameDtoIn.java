package sk.mkrajcovic.bgs.dto;

import java.util.Set;

public interface BoardGameDtoIn {

	String getTitle();
	String getDescription();
	Integer getMinPlayers();
	Integer getMaxPlayers();
	Integer getEstimatedPlayTime();
	Set<String> getAuthors();
	AgeRangeDtoIn getAgeRange();
	Boolean getIsCooperative();
	Boolean getCanPlayOnlyOnce();
	Boolean getIsExtension();
}
