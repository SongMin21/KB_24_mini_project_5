package org.scoula.thinking.mapper;

import org.scoula.thinking.domain.ThinkingVO;

import java.util.List;

public interface ThinkingMapper {
    public List<ThinkingVO> getList();

//    public List<ThinkingVO> get(String category);

    public List<ThinkingVO> getByCategory(String category);
}
