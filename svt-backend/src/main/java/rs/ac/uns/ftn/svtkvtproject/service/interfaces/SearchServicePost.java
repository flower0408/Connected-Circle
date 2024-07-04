package rs.ac.uns.ftn.svtkvtproject.service.interfaces;

import rs.ac.uns.ftn.svtkvtproject.elasticmodel.GroupDocument;
import rs.ac.uns.ftn.svtkvtproject.elasticmodel.PostDocument;
import rs.ac.uns.ftn.svtkvtproject.model.dto.SearchPostsByNumberOfLikes;

import java.util.List;

public interface SearchServicePost {
    List<PostDocument> getPostsByPostName(String postName, boolean usePhraseQuery, boolean useFuzzyQuery);
    List<PostDocument> getPostsByPostContent(String content, boolean usePhraseQuery, boolean useFuzzyQuery);
    List<PostDocument> getPostsByPDFContent(String content, boolean usePhraseQuery, boolean useFuzzyQuery);
    List<PostDocument> getPostsByNumberOfLikes(SearchPostsByNumberOfLikes criteria);
    List<PostDocument> searchPostsBooleanQuery(String title, String content, String pdfContent, String operation, boolean usePhraseQuery, boolean useFuzzyQuery);
}
