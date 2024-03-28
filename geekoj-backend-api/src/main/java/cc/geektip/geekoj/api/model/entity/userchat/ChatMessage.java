package cc.geektip.geekoj.api.model.entity.userchat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName message
 */
@TableName(value ="chat_message")
@Data
public class ChatMessage implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long conversationId;
    private Integer type;
    private Long fromUid;
    private Long toUid;
    private Integer toGroupId;
    private String content;
    private Date createTime;
    private Integer isDelete;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}