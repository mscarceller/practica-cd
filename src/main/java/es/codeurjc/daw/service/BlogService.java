package es.codeurjc.daw.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.model.Comment;
import es.codeurjc.daw.model.Post;
import es.codeurjc.daw.controller.CommentDTOFull;
import es.codeurjc.daw.controller.PostDTOBasic;
import es.codeurjc.daw.controller.PostDTOFull;
import es.codeurjc.daw.repository.CommentRepository;
import es.codeurjc.daw.repository.PostRepository;

@Service
public class BlogService {

	@Autowired
    private CommentRepository commentRepository;
    
    @Autowired
	private PostRepository postRepository;

	@Autowired
	private ModelMapper modelMapper;

	/********************** PARA ENTITY POST   **********************/

	public List<PostDTOBasic> getAllPostPDOBasic(){
		return postRepository.findAll().stream()
				.map(this::convertPostToPostBasicDto)
				.collect(Collectors.toList());	
	}

	public Post getPost(long id) {
		Optional<Post> found = postRepository.findById(id);
		if (found.isPresent()) {
			return found.get();
		} else {
			return null;
		}
	}

	public PostDTOBasic getPostPDOBasic(long id){
		Optional<Post> found = postRepository.findById(id);
		if (found.isPresent()) {
			return convertPostToPostBasicDto(found.get());
		} else {
			return null;
		}
	}

	public PostDTOFull getPostPDOFull(long id){
		Optional<Post> found = postRepository.findById(id);
		if (found.isPresent()) {
			return convertPostToPostFullDto(found.get());
		} else {
			return null;
		}
	}

	public Post savePost(PostDTOFull post){
		return postRepository.save(convertPostFullDtoToPost(post));
	}


	/********************** FUNCIONES PARA ENTITY COMMENT   **********************/

	public Comment getComment(long id) {
		Optional<Comment> found = commentRepository.findById(id);
		if (found.isPresent()) {
			return found.get();
		} else {
			return null;
		}
    }
    
    public CommentDTOFull getCommentPDOFull(Comment comment){
		return convertToCommentFullDto(comment);
	}

	public Comment saveComment(long postId, CommentDTOFull commentDTOFull){
		Post post = getPost(postId);
		if (post == null) {
			return null;
		}
		Comment comment = convertCommentFullDtoToComment(commentDTOFull);
        comment.setPost(post);
        commentRepository.save(comment);
        return comment;
    }

    public void deleteComment(long id){
		commentRepository.deleteById(id);
    }

    
    private PostDTOBasic convertPostToPostBasicDto(Post post) {
		return modelMapper.map(post, PostDTOBasic.class);
	}

	private PostDTOFull convertPostToPostFullDto(Post post) {
		return modelMapper.map(post, PostDTOFull.class);
	}

	private Post convertPostFullDtoToPost(PostDTOFull post) {
		return modelMapper.map(post, Post.class);
    }

	private CommentDTOFull convertToCommentFullDto(Comment comment) {
		return modelMapper.map(comment, CommentDTOFull.class);
    }

    private Comment convertCommentFullDtoToComment(CommentDTOFull commentFullDTO) {
		return modelMapper.map(commentFullDTO, Comment.class);
    }
    
}
