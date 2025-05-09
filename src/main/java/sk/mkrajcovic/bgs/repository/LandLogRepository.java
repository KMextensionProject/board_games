package sk.mkrajcovic.bgs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sk.mkrajcovic.bgs.entity.LendLog;

@Repository
public interface LandLogRepository extends JpaRepository<LendLog, Long> {

}
