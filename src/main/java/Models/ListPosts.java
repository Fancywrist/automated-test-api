package Models;

import java.util.List;

public class ListPosts {
  private List<Post> postList;
  
  public List<Post> getPostList() {
    return postList;
  }
  
  public void setPostList(List<Post> postList) {
    this.postList = postList;
  }
  
  public ListPosts(List<Post> postList) {
    this.postList = postList;
  }
}
