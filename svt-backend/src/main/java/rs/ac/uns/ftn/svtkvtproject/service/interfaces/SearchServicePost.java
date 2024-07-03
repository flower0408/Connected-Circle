package rs.ac.uns.ftn.svtkvtproject.service.interfaces;

import rs.ac.uns.ftn.svtkvtproject.elasticmodel.PostDocument;

import java.util.List;

public interface SearchServicePost {
    List<PostDocument> getPostsByPostName(String postName);
    List<PostDocument> getPostsByPostContent(String content);
    List<PostDocument> getPostsByPDFContent(String content);
}
