package cc.geektip.geekoj.api.service.user;

import cc.geektip.geekoj.api.model.entity.userchat.Conversation;
import cc.geektip.geekoj.api.model.vo.userchat.ConversationVo;
import cc.geektip.geekoj.common.common.PageRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;


/**
* @author Antares
* @description 针对表【conversation】的数据库操作Service
* @createDate 2023-05-18 21:30:08
*/
public interface ConversationService extends IService<Conversation> {

    Page<ConversationVo> listConversationVoByPage(PageRequest pageRequest, HttpServletRequest request);

    ConversationVo getConversationByTargetUid(Long targetUid, HttpServletRequest request);

    void clearUnread(Long uid, Long conversationId);

    void clearConversationUnread(Long uid);
}