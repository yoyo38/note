package cn.holdmoral.forever.service;

import cn.holdmoral.forever.entity.ForeverEnvironmentConfig;
import cn.holdmoral.forever.mapper.ForeverEnvironmentConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service实现
 *
 * @author lujianhua
 * @date 2020-06-04 16:10:18
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EnvironmentConfigServiceImpl extends ServiceImpl<ForeverEnvironmentConfigMapper, ForeverEnvironmentConfig> {

}
