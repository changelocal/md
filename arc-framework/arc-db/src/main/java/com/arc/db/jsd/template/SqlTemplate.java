package com.arc.db.jsd.template;


import com.arc.db.jsd.result.BuildResult;


public interface SqlTemplate {
    BuildResult execute(Object obj);
}
