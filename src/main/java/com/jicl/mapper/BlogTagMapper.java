package com.jicl.mapper;

import com.jicl.entity.BlogTag;
import com.jicl.entity.BlogTagExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlogTagMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_blog_tag
     *
     * @mbg.generated
     */
    long countByExample(BlogTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_blog_tag
     *
     * @mbg.generated
     */
    int deleteByExample(BlogTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_blog_tag
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_blog_tag
     *
     * @mbg.generated
     */
    int insert(BlogTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_blog_tag
     *
     * @mbg.generated
     */
    int insertSelective(BlogTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_blog_tag
     *
     * @mbg.generated
     */
    List<BlogTag> selectByExample(BlogTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_blog_tag
     *
     * @mbg.generated
     */
    BlogTag selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_blog_tag
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") BlogTag record, @Param("example") BlogTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_blog_tag
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") BlogTag record, @Param("example") BlogTagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_blog_tag
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(BlogTag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_blog_tag
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(BlogTag record);
}