package ru.mirea.server_coursework.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.server_coursework.controller.api.AuthApi;
import ru.mirea.server_coursework.dto.AuthRequestDTO;
import ru.mirea.server_coursework.dto.AuthResponseDTO;
import ru.mirea.server_coursework.dto.RegisterUserDTO;
import ru.mirea.server_coursework.dto.UpdateUserDTO;
import ru.mirea.server_coursework.exception.DuplicateUsernameException;
import ru.mirea.server_coursework.exception.PasswordCheckException;
import ru.mirea.server_coursework.mapper.UserMapper;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.security.JwtTokenProvider;
import ru.mirea.server_coursework.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class AuthController implements AuthApi {

    private final AuthenticationManager manager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public ResponseEntity<?> register(@RequestBody @Valid RegisterUserDTO userDTO) throws DuplicateUsernameException, PasswordCheckException {
        try {
            userService.loadUserByUsername(userDTO.getUsername());
            throw new DuplicateUsernameException("Данная почта уже используется для другого аккаунта");
        } catch(UsernameNotFoundException ex) {
            userService.checkDTO(userDTO);
            User user = userDTO.toUser();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.create(user);
            return new ResponseEntity<>("Вы успешно зарегистрировались в системе", HttpStatus.OK);
        }
    }

    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthRequestDTO request) throws UsernameNotFoundException {
        String username = request.getUsername();
        manager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));
        User user = (User) userService.loadUserByUsername(request.getUsername());
        String token = jwtTokenProvider.generateToken(user);
        AuthResponseDTO response = AuthResponseDTO.builder()
                .username(user.getUsername()).token(token)
                .build();
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> editProfile(@RequestBody @Valid UpdateUserDTO dto, HttpServletRequest request)
            throws PasswordCheckException {
        String pass = userService.checkDTO(dto);
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsernameFromToken(token);
        User user = (User) userService.loadUserByUsername(username);
        userMapper.updateUserFromDto(dto, user);
        if (pass != null) {
            user.setPassword(pass);
        }
        userService.update(user);
        return new ResponseEntity<>("Данные вашего профиля обновлены", HttpStatus.OK);
    }

    /*public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.logout(request, response, null);
    }*/
}
