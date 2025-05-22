package sk.mkrajcovic.bgs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sk.mkrajcovic.bgs.entity.Borrower;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

}
