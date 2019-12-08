package learn.zzyft.sb.springboot.controller;

import learn.zzyft.sb.springboot.dto.AccesstokenDTO;
import learn.zzyft.sb.springboot.dto.GithubUser;
import learn.zzyft.sb.springboot.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

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
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code ,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request){
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setClient_id(clientID);
        accesstokenDTO.setClient_secret(clientSecret);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri(redirectUri);
        accesstokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accesstokenDTO);
        GithubUser user  = githubProvider.getUser(accessToken);
        if (user!=null){
            //登录成功,写session和cookie
            request.getSession().setAttribute("user" , user);
            return "redirect:/";
        }else {
            //登录失败
            return "redirect:/";
        }
    }
}
