package org.scoula.thinking.service;

import org.scoula.thinking.dto.ThinkingDTO;
import org.scoula.thinking.dto.ThinkingUpdateDTO;

import java.util.List;

public interface ThinkingService {
    // 전체
    public List<ThinkingDTO> getThinking();

    // 강민주

    // 복원준
    public boolean updateThinking(ThinkingUpdateDTO thinking);

    // 이현서

    // 이현주
}
