package sk.mkrajcovic.bgs.dto;

import java.util.Set;

public interface BoardGameDtoIn {

	String getTitle();
	Integer getMinPlayers();
	Integer getMaxPlayers();
	Integer getEstimatedPlayTime();
	Set<String> getAuthors();
}
