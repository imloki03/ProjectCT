package com.projectct.projectservice.service;

import com.projectct.projectservice.DTO.Project.request.ProjectRequest;
import com.projectct.projectservice.DTO.Project.response.ProjectResponse;
import com.projectct.projectservice.repository.httpclient.AuthClient;
import com.projectct.projectservice.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{
    final AuthClient authClient;
    final WebUtil webUtil;
    @Override
    public ProjectResponse createNewProject(ProjectRequest request) {
        var user = authClient.getUserInfo("loki");
        log.error(user.getData().getName());
        log.error(webUtil.getCurrentUsername());
        log.error(webUtil.getCurrentIdUser().toString());
        return null;
    }
}
