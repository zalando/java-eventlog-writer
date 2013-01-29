package de.zalando.zomcat.jobs.listener;

import org.quartz.JobExecutionContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import de.zalando.domain.ComponentBean;

import de.zalando.zomcat.ExecutionContext;
import de.zalando.zomcat.flowid.FlowId;
import de.zalando.zomcat.flowid.FlowIdType;
import de.zalando.zomcat.flowid.FlowUserContext;
import de.zalando.zomcat.jobs.JobListener;
import de.zalando.zomcat.jobs.RunningWorker;

@Component(JobFlowIdListener.JOB_FLOW_ID_LISTENER)
public class JobFlowIdListener implements JobListener, ComponentBean {
    private static final Logger LOG = LoggerFactory.getLogger(JobFlowIdListener.class);

    // The bean name
    static final String JOB_FLOW_ID_LISTENER = "jobFlowIdListener";

    // get the bean name
    public static String beanName() {
        return JOB_FLOW_ID_LISTENER;
    }

    @Override
    public String getBeanName() {
        return beanName();
    }

    @Override
    public void startRunning(final RunningWorker runningWorker, final JobExecutionContext context, final String host) {

        // clear this thread: there can be no other context in there...
        ExecutionContext.clear();
        FlowId.clear();

        FlowUserContext.setUserContext(getUserContext(context, host));
        FlowId.generateAndPushFlowIdWithPayload(FlowIdType.JOB);
        LOG.trace("start running job with flowId {} and userContext {}", FlowId.peekFlowId(),
            FlowUserContext.getUserContext());
    }

    private String getUserContext(final JobExecutionContext context, final String host) {
        final StringBuilder stringBuilder = new StringBuilder("job:");
        stringBuilder.append(host);
        stringBuilder.append(":");
        stringBuilder.append(context.getJobDetail().getFullName());
        return stringBuilder.toString();
    }

    @Override
    public void stopRunning(final RunningWorker runningWorker, final Throwable t) {
        final String userContext = FlowUserContext.clear();
        final String flowId = FlowId.popFlowId();
        LOG.trace("stop running job with flowId {} and userContext {}", flowId, userContext);
    }
}
