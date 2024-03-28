package cc.geektip.geekoj.api.service.user;


import cc.geektip.geekoj.api.model.entity.userchat.ChatMessage;
import cc.geektip.geekoj.api.model.vo.userchat.MessageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;


/**
* @author Antares
* @description 针对表【message】的数据库操作Service
* @createDate 2023-05-18 21:30:23
*/
public interface ChatMessageService extends IService<ChatMessage> {

    Page<MessageVo> listMessageVoByPage(MessageQueryRequest messageQueryRequest, HttpServletRequest request);

    MessageVo messageToMessageVo(ChatMessage chatMessage);

    Long saveMessage(ChatMessage chatMessage);
}
