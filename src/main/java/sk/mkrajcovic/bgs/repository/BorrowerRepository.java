package sk.mkrajcovic.bgs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sk.mkrajcovic.bgs.entity.Borrower;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

}
