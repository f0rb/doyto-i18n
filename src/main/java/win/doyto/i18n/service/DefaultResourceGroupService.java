package win.doyto.i18n.service;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private ResourceGroupMapper resourceGroupMapper;

    @Override
    public ResourceGroupMapper getIMapper() {
        return resourceGroupMapper;
    }

    @Override
    public ResourceGroup getGroup(String user, String groupName) {
        ResourceGroup group = resourceGroupMapper.getByName(user, groupName);
        if (group == null) {
            throw new RestNotFoundException("资源分组未配置: " + groupName);
        }
        return group;
    }

    @Override
    public ResourceGroup checkGroup(Integer id, String user, String groupName) {
        ResourceGroup group = resourceGroupMapper.get(id);
        if (group == null ||
                (!StringUtils.equalsIgnoreCase(group.getOwner(), user)
                        && !StringUtils.equalsIgnoreCase(group.getName(), groupName))) {
            throw new RestNotFoundException("资源分组未配置: " + groupName);
        }
        return group;
    }

    @Override
    @Transactional
    public ResourceGroup add(ResourceGroup resourceLocale) {

        return null;
    }


    public ResourceGroup save(ResourceGroup group) {
        ResourceGroup origin = resourceGroupMapper.get(group.getId());
        if (origin == null) {
            return null;
        }
        //origin.setLabel(group.getLabel());
        //origin.setValue(group.getValue());

        //origin.setUpdateUserId(AppContext.getLoginUserId());
        //origin.setUpdateTime(new Date());
        resourceGroupMapper.update(origin);
        return origin;
    }

}