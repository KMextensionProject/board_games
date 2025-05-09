package sk.mkrajcovic.bgs.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Borrower extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100, unique = true)
    private String email;

    @OneToMany(mappedBy = "borrower")
    private List<LendLog> lendLogs;

}
