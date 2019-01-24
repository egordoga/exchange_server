package ua.exchange.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.exchange.dao.ParticipantRepository;
import ua.exchange.entity.Participant;

import java.util.ArrayList;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private ParticipantRepository participantRepository;


    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Participant participant = participantRepository.findByName(name).orElseThrow(() -> new UsernameNotFoundException(name));
        return new User(participant.getName(), participant.getPass(), new ArrayList<GrantedAuthority>() {
        });
    }
}
