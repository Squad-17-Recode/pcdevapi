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
        return empresaRepository.findByEmail(login)
                .map(EmpresaUserDetails::new)
                .or(() -> empresaRepository.findByUsername(login).map(EmpresaUserDetails::new))
                .or(() -> candidatoRepository.findByEmail(login).map(CandidatoUserDetails::new))
                .or(() -> candidatoRepository.findByUsername(login).map(CandidatoUserDetails::new))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email ou username: " + login));
    }
}
