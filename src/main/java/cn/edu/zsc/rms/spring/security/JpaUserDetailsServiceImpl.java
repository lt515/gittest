package cn.edu.zsc.rms.spring.security;

import cn.edu.zsc.rms.domain.User;
import cn.edu.zsc.rms.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author hsj
 */
@Service
public class JpaUserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepo;

    public JpaUserDetailsServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String number) throws UsernameNotFoundException {
        User user = userRepo.findByNumber(number).orElseThrow(() ->
                new UsernameNotFoundException(number + "not found")
        );
        return user.toUserDetail();
    }
}
