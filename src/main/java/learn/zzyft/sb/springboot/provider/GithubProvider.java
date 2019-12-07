package learn.zzyft.sb.springboot.provider;

import com.alibaba.fastjson.JSON;
import learn.zzyft.sb.springboot.dto.AccesstokenDTO;
import learn.zzyft.sb.springboot.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccesstokenDTO accesstokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accesstokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token  = string.split("&")[0].split("=")[1];

            return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public GithubUser getUser(String accessToken){
//        System.out.println(accessToken);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
//            System.out.println(string);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
//            System.out.println(githubUser.getName()+"wsss"+githubUser.getId()+"wsss"+githubUser.getBio());
            return githubUser;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

}
