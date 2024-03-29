package com.bridgelabz.fundoonotesservice.service;

import com.bridgelabz.fundoonotesservice.dto.LabelDTO;
import com.bridgelabz.fundoonotesservice.util.Response;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public interface ILabelService {
    Response addLabel(LabelDTO labelDTO, String token, List<Long> labelId, Long noteId);

    Response getAllLabel(String token);

    Response updateLabel(long id, @Valid LabelDTO labelDTO, String token);

    Response deleteLabel(Long id, String token);
}
