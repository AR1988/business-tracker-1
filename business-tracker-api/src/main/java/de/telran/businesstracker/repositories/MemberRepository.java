package de.telran.businesstracker.repositories;


import de.telran.businesstracker.model.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {
    List<Member> findAll();
}
