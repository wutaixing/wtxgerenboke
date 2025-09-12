package com.wtx.myblog.mapper;

import com.wtx.myblog.model.Article;
import com.wtx.myblog.model.vo.ArticleRequestVO;
import com.wtx.myblog.model.vo.IsReadResponseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 26989
 * @date 2025/9/7
 * @description
 */
@Mapper
@Repository
public interface ArticleMapper {
    int insertArticle(@Param("articleRequestVO") ArticleRequestVO articleRequestVO,@Param("tags")String tags);

    /**
     * 查找早于当前文章的最新文章
     */
    Long findLatestArticleBefore(Long currentArticleId);

    /**
     * 查找晚于当前文章的最早文章
     */
    Long findEarliestArticleAfter(Long currentArticleId);

    /**
     * 更新文章的上下关系
     */
    void updateArticleRelations(@Param("currentId") Long currentId,
                                @Param("lastId") Long lastId,
                                @Param("nextId") Long nextId);

    void updateLastArticleNextPointer(@Param("lastArticleId") Long lastArticleId,@Param("currentArticleId") Long currentArticleId);

    void updateNextArticleLastPointer(@Param("nextArticleId") Long nextArticleId,@Param("currentArticleId") Long currentArticleId);

    List<Article> selectAllArticles();

    int deleteArticleByid(Integer id);

    Article getDraftArticleById(Integer id);

    int setArticleReleaseStatusById(@Param("id") int id,@Param("status") int status);

    Article getDraftArticleByUserName(String username);

    boolean findArticleByArticleId(Long id);

    Article getArticleIdById(@Param("id") Long id);

    int updateArticleByIdAndArticle(@Param("oldArticle") Article oldArticle, @Param("id") int id);

    List<IsReadResponseVO> getAllIsReadArticleByUserName(String author);

    /**
     * 统计未读点赞数量
     */
    int countUnreadLikesByAuthor(@Param("author") String author);

    /**
     * 标记单个点赞记录为已读
     */
    int markLikeAsRead(@Param("id") Integer id);

    /**
     * 标记所有点赞记录为已读
     */
    int markAllLikesAsRead(@Param("author") String author);
}
