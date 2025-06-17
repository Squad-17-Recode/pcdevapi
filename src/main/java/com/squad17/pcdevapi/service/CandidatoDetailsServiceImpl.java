package com.squad17.pcdevapi.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.squad17.pcdevapi.repository.CandidatoRepository;

@Service
public class CandidatoDetailsServiceImpl implements UserDetailsService {

    private final CandidatoRepository candidatoRepository;

    public CandidatoDetailsServiceImpl(CandidatoRepository candidatoRepository) {
        this.candidatoRepository = candidatoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) candidatoRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Candidato não encontrado com username: " + username));
    }
}
