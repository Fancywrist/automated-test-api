import Models.Post;
import Models.User;
import com.google.gson.Gson;
import okhttp3.*;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
  private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
  private static final OkHttpClient client = new OkHttpClient();
  private static final String urlStub = "https://jsonplaceholder.typicode.com/";
  private static Gson gson = new Gson();
  
  public static void main(String[] args) {
    
    System.out.println("Hello world");
    fetchRandomUser();
    
  }
  
  
  public static void fetchRandomUser() {
    int id = new Random().nextInt(9);
    Request request = new Request.Builder()
        .url(urlStub + "users/" + id)
        .build();
    try (Response response = client.newCall(request).execute()) {
      User user = gson.fromJson(response.body().string(), User.class);
      System.out.println("User Email: " + user.getEmail());
      getPostsById(user.getId());
      createPost(user.getId());
    } catch (Exception e) {
      e.printStackTrace();
      logger.log(Level.INFO, "failed to complete request: " + e);
    }
  }
  
  public static void getPostsById(int userId) {
    Request request = new Request.Builder()
        .url(urlStub + "posts/")
        .build();
    
    try (Response response = client.newCall(request).execute()) {
      Post[] posts = gson.fromJson(response.body().string(), Post[].class);
      
      for (Post p : posts) {
        if (p.getUserId() == userId && p.getId() < 0 || p.getId() > 100) {
          System.out.println("Status: Invalid post id detected: " + p.getId() + ". Post Id greater than 100 or less than 0");
          return;
        }
      }
      System.out.println("Status: all user's posts are valid");
    } catch (Exception e) {
      e.printStackTrace();
      logger.log(Level.INFO, "failed to complete request: " + e);
    }
  }
  
  public static void createPost(int userId) {
    Post p = new Post();
    p.setUserId(userId);
    p.setBody("test body");
    p.setTitle("this is test title");
    String json = gson.toJson(p);
    String url = urlStub + "posts";
    MediaType JSON = MediaType.get("application/json; charset=utf-8");
    RequestBody body = RequestBody.create(json, JSON);
    Request request = new Request.Builder()
        .url(url)
        .post(body)
        .build();
    try (Response response = client.newCall(request).execute()) {
      System.out.println("Response Code: " + response.code());
      if (response.code() >= 200 && response.code() < 300) {
        System.out.println("Request Status: Request successful");
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.log(Level.INFO, "failed to complete request: " + e);
    }
  }
}


