package de.zalando.zomcat.jobs.fragments;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

import de.zalando.zomcat.jobs.model.JobRow;
import de.zalando.zomcat.jobs.model.JobRowsModel;

public class HistoryModeFragment extends WebMarkupContainer {
    private static final long serialVersionUID = 1L;

    public HistoryModeFragment(final IModel<JobRow> jobRowModel, final JobRowsModel jobRowsModel) {
        super("placeholderForHistory", jobRowsModel);
        add(new HistoryMode("historyEnabled", jobRowModel, jobRowsModel));
        add(new HistoryMode("historyDisabled", jobRowModel, jobRowsModel));
    }
}
