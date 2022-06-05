package com.example.deutchetest.service;

import com.example.deutchetest.entity.Article;
import com.example.deutchetest.entity.Draft;
import com.example.deutchetest.entity.History;
import com.example.deutchetest.entity.User;
import com.example.deutchetest.repository.ArticleRepo;
import com.example.deutchetest.repository.DraftRepo;
import com.example.deutchetest.repository.HistoryRepo;
import com.example.deutchetest.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private HistoryRepo historyRepo;

    @Autowired
    private DraftRepo draftRepo;

    @Transactional
    public Draft saveDraftArticle(Draft draft){
        ObjectMapper objectMapper = new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        String email = myUserDetail.getEmail();
        User user = userRepo.findByEmail(email).get();
        draft.setUser(user);
        if(!draftRepo.existsById(draft.getId())) {
            draft.setPublishedDate(LocalDate.now());
        }
        draftRepo.save(draft);
        historyRepo.save(new History(draft.getBlog(), LocalDate.now(), user));
        return draft;
    }

    @Transactional
    public Article saveSuperAdminArticle(Draft draft, String author){
        Article article = new Article(draft.getBlog(), draft.getPublishedDate(), draft.getUser());
        ObjectMapper objectMapper = new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        String email = myUserDetail.getEmail();
        User user = userRepo.findByEmail(email).get();
        article.setUser(user);
        article.setPublishedDate(LocalDate.now());
        article.setAuthor(author);
        articleRepo.save(article);
        historyRepo.save(new History(article.getBlog(), LocalDate.now(), user));
        return article;
    }

    @Transactional
    public Article updateArticle(int id, Article article){
        ObjectMapper objectMapper = new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        String email = myUserDetail.getEmail();
        User user = userRepo.findByEmail(email).get();
        Article article1 = articleRepo.findById(id).get();
        article1.setBlog(article.getBlog());
        historyRepo.save(new History(article.getBlog(), LocalDate.now(), user));

        return articleRepo.save(article1);
    }

    @Transactional
    public Article findArticle(int id){
        return articleRepo.findById(id).get();
    }

    @Transactional
    public void deleteDraft(int id){
        draftRepo.deleteById(id);
    }

    @Transactional
    public Draft editDraft(int id,Draft draft){
       Draft draftExist = draftRepo.findById(id).get();
       draftExist.setBlog(draft.getBlog());
       draftRepo.save(draftExist);
        ObjectMapper objectMapper = new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        String email = myUserDetail.getEmail();
        User user = userRepo.findByEmail(email).get();
        historyRepo.save(new History(draft.getBlog(), LocalDate.now(), user));

        return draftExist;
    }

    public void deleteArticle(int id){
        articleRepo.deleteById(id);
    }

    public Draft findDraft(int id){
        return draftRepo.findById(id).get();
    }
    @Transactional
    public List<Draft> findAllDraft(){
        ObjectMapper objectMapper = new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        String email = myUserDetail.getEmail();
        User user = userRepo.findByEmail(email).get();
        return draftRepo.findAllByUser(user);
    }

    @Transactional
    public List<Draft> allDraft(){
        return draftRepo.findAll();
    }

    @Transactional
    public List<Article> findAllArticle(){
        ObjectMapper objectMapper = new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        String email = myUserDetail.getEmail();
        User user = userRepo.findByEmail(email).get();
        List<Article> articles = articleRepo.findAllByUser(user);
            return articles;
    }

    @Transactional
    public List<Article> allArticle(String sortDirection, String sortField){
        List<Article> list = new ArrayList<>();
        if(sortDirection!=null) {
            Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                    Sort.by(sortField).ascending() : Sort.by(sortField).descending();
            Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
            Page<Article> page = articleRepo.findAll(pageable);
          list = page.getContent();

        }
        else {
            list= articleRepo.findAll();
        }
        return list;
    }

    @Transactional
    public List<Article> adminArticle(){
        ObjectMapper objectMapper = new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        String email = myUserDetail.getEmail();
        List<Article> articles = articleRepo.findAllByAuthor(email);
        return articles;
    }

    @Transactional
    public List<History> findhistory(){
        ObjectMapper objectMapper = new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        return historyRepo.findAllByUser(myUserDetail);
    }
}
