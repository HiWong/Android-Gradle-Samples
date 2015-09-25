package com.yeungeek.rxjava.retrofit;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by yeungeek on 2015/9/24.
 */
public interface GithubApi {
    /**
     * See https://developer.github.com/v3/repos/#list-contributors
     */
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<Contributor>> contributors(@Path("owner") String owner, @Path("repo") String repo);

    /**
     * See https://developer.github.com/v3/users/
     */
    @GET("/users/{user}")
    Observable<User> user(@Path("user") String user);

    /**
     * See https://developer.github.com/v3/users/
     */
    @GET("/users/{user}")
    User getUser(@Path("user") String user);
}
