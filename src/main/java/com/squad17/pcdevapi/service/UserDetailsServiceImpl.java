package com.squad17.pcdevapi.service;

import com.squad17.pcdevapi.models.empresa.Empresa;
import com.squad17.pcdevapi.models.candidato.Candidato;
import com.squad17.pcdevapi.repositories.EmpresaRepository;
import com.squad17.pcdevapi.repositories.CandidatoRepository;
import com.squad17.pcdevapi.security.EmpresaUserDetails;
import com.squad17.pcdevapi.security.CandidatoUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Empresa empresa = empresaRepository.findByEmail(login);
        if (empresa == null) {
            empresa = empresaRepository.findByUsername(login);
        }
        if (empresa != null) {
            return new EmpresaUserDetails(empresa);
        }
        Candidato candidato = candidatoRepository.findByEmail(login);
        if (candidato == null) {
            candidato = candidatoRepository.findByUsername(login);
        }
        if (candidato != null) {
            return new CandidatoUserDetails(candidato);
        }
        throw new UsernameNotFoundException("Usuário não encontrado com email ou username: " + login);
    }
}
