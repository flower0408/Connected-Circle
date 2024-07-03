package rs.ac.uns.ftn.svtkvtproject.service.interfaces;

import rs.ac.uns.ftn.svtkvtproject.elasticmodel.PostDocument;
import rs.ac.uns.ftn.svtkvtproject.model.dto.SearchPostsByNumberOfLikes;

import java.util.List;

public interface SearchServicePost {
    List<PostDocument> getPostsByPostName(String postName);
    List<PostDocument> getPostsByPostContent(String content);
    List<PostDocument> getPostsByPDFContent(String content);
    List<PostDocument> getPostsByNumberOfLikes(SearchPostsByNumberOfLikes criteria);
}
