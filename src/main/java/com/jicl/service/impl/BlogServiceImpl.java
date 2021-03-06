package com.jicl.service.impl;

import com.jicl.NotFoundException;
import com.jicl.dao.BlogRepository;
import com.jicl.pojo.Blog;
import com.jicl.pojo.Type;
import com.jicl.service.BlogService;
import com.jicl.util.MarkdownUtils;
import com.jicl.util.MyBeanUtils;
import com.jicl.vo.BlogQuery;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @Auther: xianzilei
 * @Date: 2019/11/24 20:36
 * @Description: 博客管理实现类
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    /**
     * 根据编号查询博客信息
     *
     * @param id 1
     * @return: com.jicl.pojo.Blog
     * @auther: xianzilei
     * @date: 2019/11/24 20:24
     **/
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findOne(id);
    }

    /**
     * TODO
     *
     * @param id 1
     * @return: com.jicl.pojo.Blog
     * @auther: xianzilei
     * @date: 2019/11/24 20:24
     **/
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findOne(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在！");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));

        blogRepository.updateViews(id);
        return b;
    }

    /**
     * 根据条件分页查询博客信息
     *
     * @param pageable 1
     * @param blog     2
     * @return: org.springframework.data.domain.Page<com.jicl.pojo.Blog>
     * @auther: xianzilei
     * @date: 2019/11/24 20:24
     **/
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            //博客标题
            if (StringUtils.isNotBlank(blog.getTitle())) {
                predicates.add(criteriaBuilder.like(root.get("title"),
                        "%" + blog.getTitle() +
                                "%"));
            }
            //博客类型
            if (blog.getTypeId() != null) {
                predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"),
                        blog.getTypeId()));
            }
            //是否推荐
            if (blog.isRecommend()) {
                predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"),
                        blog.isRecommend()));
            }
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            return null;
        }, pageable);
    }

    /**
     * 分页查询博客信息
     *
     * @param pageable 1
     * @return: org.springframework.data.domain.Page<com.jicl.pojo.Blog>
     * @auther: xianzilei
     * @date: 2019/11/24 20:24
     **/
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    /**
     * TODO
     *
     * @param tagId    1
     * @param pageable 2
     * @return: org.springframework.data.domain.Page<com.jicl.pojo.Blog>
     * @auther: xianzilei
     * @date: 2019/11/24 20:24
     **/
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll((root, cq, cb) -> cb.equal(root.join("tags").get("id"),
                tagId), pageable);
    }

    /**
     * 根据关键字查询博客列表
     *
     * @param query    1
     * @param pageable 2
     * @return: org.springframework.data.domain.Page<com.jicl.pojo.Blog>
     * @auther: xianzilei
     * @date: 2019/11/24 20:24
     **/
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    /**
     * 查询推荐的博客列表
     *
     * @param size 1
     * @return: java.util.List<com.jicl.pojo.Blog>
     * @auther: xianzilei
     * @date: 2019/11/24 20:24
     **/
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    /**
     * TODO
     *
     * @return: java.util.Map<java.lang.String   ,   java.util.List   <   com.jicl.pojo.Blog>>
     * @auther: xianzilei
     * @date: 2019/11/24 20:24
     **/
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    /**
     * TODO
     *
     * @return: java.lang.Long
     * @auther: xianzilei
     * @date: 2019/11/24 20:24
     **/
    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    /**
     * 新增博客信息
     *
     * @param blog 1
     * @return: com.jicl.pojo.Blog
     * @auther: xianzilei
     * @date: 2019/11/24 20:24
     **/
    @Override
    @Transactional
    public Blog saveBlog(Blog blog) {
        Date date = new Date();
        blog.setUpdateTime(date);
        if (blog.getId() == null) {
            blog.setCreateTime(date);
            blog.setViews(0);
        }
        return blogRepository.save(blog);
    }

    /**
     * 更新博客信息
     *
     * @param id   1
     * @param blog 2
     * @return: com.jicl.pojo.Blog
     * @auther: xianzilei
     * @date: 2019/11/24 20:25
     **/
    @Override
    @Transactional
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findOne(id);
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    /**
     * 删除博客信息
     *
     * @param id 1
     * @return: void
     * @auther: xianzilei
     * @date: 2019/11/24 20:25
     **/
    @Override
    @Transactional
    public void deleteBlog(Long id) {
        blogRepository.delete(id);
    }
}
