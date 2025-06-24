package com.squad17.pcdevapi.service;

import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.models.candidato.Candidato;
import com.squad17.pcdevapi.repository.empresa.EmpresaRepository;
import com.squad17.pcdevapi.repository.candidato.CandidatoRepository;
import com.squad17.pcdevapi.security.EmpresaUserDetails;
import com.squad17.pcdevapi.security.CandidatoUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Empresa> empresa = empresaRepository.findByEmail(login);
        if (empresa.isPresent()) {
            return new EmpresaUserDetails(empresa.get());
        }
        empresa = empresaRepository.findByUsername(login);
        if (empresa.isPresent()) {
            return new EmpresaUserDetails(empresa.get());
        }
        Optional<Candidato> candidato = candidatoRepository.findByEmail(login);
        if (candidato.isPresent()) {
            return new CandidatoUserDetails(candidato.get());
        }
        candidato = candidatoRepository.findByUsername(login);
        if (candidato.isPresent()) {
            return new CandidatoUserDetails(candidato.get());
        }
        throw new UsernameNotFoundException("Usuário não encontrado com email ou username: " + login);
    }
}
