package learn.zzyft.sb.springboot.controller;

import learn.zzyft.sb.springboot.dto.AccesstokenDTO;
import learn.zzyft.sb.springboot.dto.GithubUser;
import learn.zzyft.sb.springboot.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code ,
                           @RequestParam(name = "state")String state){
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setClient_id("3590502b3278ec843484");
        accesstokenDTO.setClient_secret("69ebf503caa026567854c820458c80933deba74a");
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri("http://localhost:8887/callback");
        accesstokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accesstokenDTO);
        GithubUser user  = githubProvider.getUser(accessToken);

        System.out.println(user.getId());
        return "index";
    }
}
