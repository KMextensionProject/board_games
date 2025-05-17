package sk.mkrajcovic.bgs.dto;

import sk.mkrajcovic.bgs.entity.BoardGame.AgeRange;

public interface AgeRangeDtoIn {

	Integer getMinAge();
	Integer getMaxAge();

	public default AgeRange asEntity() {
		return new AgeRange(getMinAge(), getMaxAge());
	}

}
