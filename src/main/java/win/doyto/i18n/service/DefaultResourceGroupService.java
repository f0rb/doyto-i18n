package win.doyto.i18n.service;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import win.doyto.i18n.exception.RestNotFoundException;
import win.doyto.i18n.mapper.ResourceGroupMapper;
import win.doyto.i18n.model.ResourceGroup;
import win.doyto.web.service.AbstractService;

/**
 * GroupDefaultService
 *
 * @author f0rb on 2017-03-29.
 */
@Slf4j
@Service
public class DefaultResourceGroupService extends AbstractService<ResourceGroup> implements ResourceGroupService {
    @Resource
    private ResourceGroupMapper groupMapper;

    @Override
    public ResourceGroupMapper getIMapper() {
        return groupMapper;
    }

    @Override
    public ResourceGroup getGroup(String groupName) {
        ResourceGroup group = groupMapper.getByName(groupName);
        if (group == null) {
            throw new RestNotFoundException("资源分组未配置: " + groupName);
        }
        return group;
    }

    public ResourceGroup save(ResourceGroup group) {
        ResourceGroup origin = groupMapper.get(group.getId());
        if (origin == null) {
            return null;
        }
        //origin.setLabel(group.getLabel());
        //origin.setValue(group.getValue());

        //origin.setUpdateUserId(AppContext.getLoginUserId());
        //origin.setUpdateTime(new Date());
        groupMapper.update(origin);
        return origin;
    }

}