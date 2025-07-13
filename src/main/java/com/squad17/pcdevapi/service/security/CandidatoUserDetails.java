package com.squad17.pcdevapi.service.security;

import com.squad17.pcdevapi.models.candidato.Candidato;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class CandidatoUserDetails implements UserDetails {
    private final Candidato candidato;

    public CandidatoUserDetails(Candidato candidato) {
        this.candidato = candidato;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return candidato.getSenha();
    }

    @Override
    public String getUsername() {
        return candidato.getUsername();
    }

    public String getEmail() {
        return candidato.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
