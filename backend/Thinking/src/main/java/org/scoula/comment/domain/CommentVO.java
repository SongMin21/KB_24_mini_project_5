package org.scoula.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentVO {
    private long id;
    private long thinkingId;
    private String content;
    private String password;
    private Date createdAt;
    private Date updatedAt;
}
