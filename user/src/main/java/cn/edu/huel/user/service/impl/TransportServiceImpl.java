package cn.edu.huel.user.service.impl;

import cn.edu.huel.user.domain.Transport;
import cn.edu.huel.user.mapper.TransportMapper;
import cn.edu.huel.user.service.ITransportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 运输历史Service业务层处理
 *
 * @author 张晓华
 * @date 2023-02-18
 */
@Service
public class TransportServiceImpl extends ServiceImpl<TransportMapper, Transport> implements ITransportService {
}
