package sk.mkrajcovic.bgs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sk.mkrajcovic.bgs.entity.LendLog;

public interface LandLogRepository extends JpaRepository<LendLog, Long> {

}
