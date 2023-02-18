package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.domain.PostOrder;
import cn.edu.huel.user.mapper.PostOrderMapper;
import cn.edu.huel.user.service.IPostOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 订单Service业务层处理
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Service
public class PostOrderServiceImpl extends ServiceImpl<PostOrderMapper, PostOrder> implements IPostOrderService {
}
