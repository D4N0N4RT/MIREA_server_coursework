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
import ru.mirea.server_coursework.dto.BasicApiResponse;
import ru.mirea.server_coursework.dto.RegisterUserDTO;
import ru.mirea.server_coursework.dto.TokenRefreshDTO;
import ru.mirea.server_coursework.dto.TokenRefreshResponseDTO;
import ru.mirea.server_coursework.dto.UpdateUserDTO;
import ru.mirea.server_coursework.exception.DuplicateUsernameException;
import ru.mirea.server_coursework.exception.PasswordCheckException;
import ru.mirea.server_coursework.exception.TokenRefreshException;
import ru.mirea.server_coursework.mapper.UserMapper;
import ru.mirea.server_coursework.model.RefreshToken;
import ru.mirea.server_coursework.model.User;
import ru.mirea.server_coursework.security.JwtTokenProvider;
import ru.mirea.server_coursework.service.RefreshTokenService;
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
    private final RefreshTokenService refreshTokenService;

    public ResponseEntity<?> register(@RequestBody @Valid RegisterUserDTO userDTO) throws DuplicateUsernameException,
            PasswordCheckException {
        try {
            userService.loadUserByUsername(userDTO.getUsername());
            throw new DuplicateUsernameException("Данная почта уже используется для другого аккаунта");
        } catch(UsernameNotFoundException ex) {
            userService.checkDTO(userDTO);
            User user = userDTO.toUser();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRating(0.0f);
            userService.create(user);
            return new ResponseEntity<>(new BasicApiResponse(200,"Вы успешно зарегистрировались в системе"),
                    HttpStatus.OK);
        }
    }

    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthRequestDTO request) throws UsernameNotFoundException {
        String username = request.getUsername();
        manager.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));
        User user = (User) userService.loadUserByUsername(request.getUsername());
        String token = jwtTokenProvider.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        AuthResponseDTO response = AuthResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .accessToken(token)
                .refreshToken(refreshToken.getToken())
                .role(user.getRole().toString())
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
        return new ResponseEntity<>(new BasicApiResponse(200, "Данные вашего профиля обновлены"), HttpStatus.OK);
    }

    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshDTO request) {
        String requestRefreshToken = request.getRefreshToken();

        System.out.println(requestRefreshToken);

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenProvider.generateToken(user);
                    return ResponseEntity.ok(new TokenRefreshResponseDTO(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }
}
