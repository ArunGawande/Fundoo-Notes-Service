package com.bridgelabz.fundoonotesservice.repository;

import com.bridgelabz.fundoonotesservice.model.NoteServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface INoteServiceRepository extends JpaRepository<NoteServiceModel, Long> {
    Optional<NoteServiceModel> findByUserIdAndId(Long userId, long id);

    List<NoteServiceModel> findByUserId(Long userId);
}
