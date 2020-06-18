package es.codeurjc.daw.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.codeurjc.daw.model.Comment;
import es.codeurjc.daw.model.Post;
import es.codeurjc.daw.service.BlogService;

@Controller
public class BlogWebController {

	@Autowired
	private BlogService blogService;

	
	@GetMapping("/")
	public String blog(Model model) {
		model.addAttribute("posts", blogService.getAllPostPDOBasic());
		return "blog";
	}

	@GetMapping("/post/{id}")
	public String post(HttpSession session, @PathVariable long id, Model model) {
		PostDTOFull postDTOFull = blogService.getPostPDOFull(id);
		if (postDTOFull == null) {
			model.addAttribute("errorMessage", "No existe un post con id " + id);
			return "error";
		}
		Object userName = session.getAttribute("userName");
		model.addAttribute("userName", userName != null ? userName : "");
		model.addAttribute("post", postDTOFull);
		return "post";
	}

	@GetMapping("/post/new")
	public String post(Model model) {
		return "newPost";
	}

	@PostMapping("/post")
	public String post(Model model, PostDTOFull postDTOFull) {
		Post post = blogService.savePost(postDTOFull);
		return "redirect:/post/" + post.getId();
	}

	@PostMapping("/post/{id}/comment")
	public String post(HttpSession session, @PathVariable long id, Model model, CommentDTOFull commentDTOFUll) {
		session.setAttribute("userName", commentDTOFUll.getAuthor());
		if (blogService.saveComment(id, commentDTOFUll) == null) {
			model.addAttribute("errorMessage", "No existe un post con id " + id);
			return "error";
		}
		return "redirect:/post/" + id;
	}

	@PostMapping("/post/{postId}/comment/{commentId}/delete")
	public String post(@PathVariable long postId, @PathVariable long commentId, Model model) {
		Post post = blogService.getPost(postId);
		if (post == null) {
			model.addAttribute("errorMessage", "No existe un post con id " + postId);
			return "error";
		}
		Comment comment = blogService.getComment(commentId);
		if (comment == null)  {
			model.addAttribute("errorMessage", "No existe un comentario con id " + postId);
			return "error";
		}
		blogService.deleteComment(commentId);
		return "redirect:/post/" + postId;
	}
	

}
