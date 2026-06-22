package org.scoula.thinking.mapper;

import org.apache.ibatis.annotations.Param;
import org.scoula.thinking.domain.ThinkingVO;

import java.util.Date;
import java.util.List;

public interface ThinkingMapper {
    public List<ThinkingVO> getList();
    public void create(ThinkingVO thinking);
    public ThinkingVO getListOne(
            @Param("id") long id
    );
    public int updateLike(long id);
    public List<ThinkingVO> getByDate(Date date);

    public List<ThinkingVO> getByLike();
    // get password
    public String getPassword(long id);
}
