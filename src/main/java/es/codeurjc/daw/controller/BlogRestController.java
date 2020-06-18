package es.codeurjc.daw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.daw.model.Comment;
import es.codeurjc.daw.model.Post;
import es.codeurjc.daw.service.BlogService;

@RestController
@RequestMapping("/api")
public class BlogRestController {

	@Autowired
	private BlogService blogService;

	
	@GetMapping("/post")
	public ResponseEntity<List<PostDTOBasic>> listPosts() {
		return new ResponseEntity<>(blogService.getAllPostPDOBasic(), HttpStatus.OK);	
	}

	@GetMapping("/post/{id}")
	public ResponseEntity<PostDTOFull> getPost(@PathVariable long id) {
		PostDTOFull postDTOFull = blogService.getPostPDOFull(id);
		if (postDTOFull != null) {
			return new ResponseEntity<>(postDTOFull, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/post")
	public ResponseEntity<PostDTOBasic> newPost(@RequestBody PostDTOFull postDTOFull) {
		Post post = blogService.savePost(postDTOFull);
		return new ResponseEntity<>(blogService.getPostPDOBasic(post.getId()), HttpStatus.CREATED);
	}

	@PostMapping("/post/{postId}/comment")
	public ResponseEntity<CommentDTOFull> newComment(@PathVariable long postId, @RequestBody CommentDTOFull commentDtoFull) {
		Comment comment = blogService.saveComment(postId, commentDtoFull);
		if (comment == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(blogService.getCommentPDOFull(comment), HttpStatus.CREATED);
	}

	@DeleteMapping("/post/{postId}/comment/{commentId}")
	public ResponseEntity<CommentDTOFull> deleteComment(@PathVariable long postId, @PathVariable long commentId) {
		Comment comment = blogService.getComment(commentId);
		if (comment == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		blogService.deleteComment(commentId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
