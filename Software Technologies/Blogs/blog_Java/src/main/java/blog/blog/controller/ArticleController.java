package blog.blog.controller;

import blog.blog.bindingModel.ArticleBindingModel;
import blog.blog.entity.Article;
import blog.blog.entity.User;
import blog.blog.repository.ArticleRepository;
import blog.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create (Model model){
        model.addAttribute("view", "article/create");
        return "base-layout";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess (ArticleBindingModel articleBindingModel){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());

        Article articleEntity = new Article(
                articleBindingModel.getTitle(),
                articleBindingModel.getContent(),
                userEntity
        );

        this.articleRepository.saveAndFlush(articleEntity);

        return "redirect:/";
    }

    @GetMapping("/article/{id}")
    public String details(Model model, @PathVariable Integer id){
        if(!this.articleRepository.exists(id))
            return "redirect:/";

        Article article = this.articleRepository.findOne(id);
        model.addAttribute("article", article);
        model.addAttribute("view", "article/details");

        return "base-layout";
    }

    @GetMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String edit(@PathVariable Integer id, Model model){
        if(!this.articleRepository.exists(id))
            return "redirect:/";

        Article article = this.articleRepository.findOne(id);

        model.addAttribute("view", "article/edit");
        model.addAttribute("article", article);

        return "base-layout";
    }

    @PostMapping("/article/edit/{id}")
    @PreAuthorize("isAuthenticated()")
    public String editPost(@PathVariable Integer id, ArticleBindingModel articleBindingModel){
        if(!this.articleRepository.exists(id))
            return  "redirect:/";

        Article article = this.articleRepository.findOne(id);

        article.setContent(articleBindingModel.getContent());
        article.setTitle(articleBindingModel.getTitle());

        this.articleRepository.saveAndFlush(article);

        return "redirect:/article/"+article.getId();
    }

    @GetMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id){
        if(!this.articleRepository.exists(id))
            return "redirect:/";

        Article article = this.articleRepository.findOne(id);

        model.addAttribute("view", "article/delete");
        model.addAttribute("article", article);

        return "base-layout";
    }



    @PostMapping("/article/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable Integer id){
        if(!this.articleRepository.exists(id))
            return  "redirect:/";

        Article article = this.articleRepository.findOne(id);

        this.articleRepository.delete(article);

        return "redirect:/";
    }
}
