package com.squad17.pcdevapi.service.candidatura;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.squad17.pcdevapi.models.candidatura.Candidatura;
import com.squad17.pcdevapi.repository.candidatura.CandidaturaRepository;

@Service
public class CandidaturaService {

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    public Candidatura save(Candidatura candidatura) {
        return candidaturaRepository.save(candidatura);
    }

}
