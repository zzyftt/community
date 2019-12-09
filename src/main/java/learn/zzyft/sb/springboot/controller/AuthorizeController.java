package learn.zzyft.sb.springboot.controller;

import learn.zzyft.sb.springboot.dto.AccesstokenDTO;
import learn.zzyft.sb.springboot.dto.GithubUser;
import learn.zzyft.sb.springboot.mapper.UserMapper;
import learn.zzyft.sb.springboot.model.User;
import learn.zzyft.sb.springboot.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 *
 */
@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientID;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code ,
                           @RequestParam(name = "state")String state,
                           HttpServletResponse response ){

        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setClient_id(clientID);
        accesstokenDTO.setClient_secret(clientSecret);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri(redirectUri);
        accesstokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accesstokenDTO);
        GithubUser githubUser  = githubProvider.getUser(accessToken);
        if (githubUser!=null){
            //登录成功,写session和cookie
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountID(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
        }else {
            //登录失败
        }
        return "redirect:/";
    }
}
