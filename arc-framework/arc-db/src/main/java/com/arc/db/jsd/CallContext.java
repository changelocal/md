package com.arc.db.jsd;


import com.arc.db.jsd.clause.CallClause;
import com.arc.db.jsd.clause.CallEndClause;
import com.arc.db.jsd.result.BuildResult;
import com.arc.db.jsd.result.CallResult;

import java.util.Arrays;


public final class CallContext implements CallClause {
    private ConnectionManager manager;
    private CallInfo info;

    CallContext(ConnectionManager manager, String sp) {
        this.manager = manager;
        this.info = new CallInfo(sp);
    }

    @Override
    public CallResult result() {
        BuildResult result = this.buildCall();
        Debug.log(result);
        return new CallResult(manager, result.getSql(), this.info.params);
    }

    @Override
    public BuildResult print() {
        return this.buildCall();
    }

    @Override
    public CallEndClause with(CallParams params) {
        this.info.params = params;
        return this;
    }

    private BuildResult buildCall() {
        BuildBuffer buffer = new BuildBuffer();

        buffer.addSql("{");
        if (this.info.params != null && this.info.params.hasReturnParam()) {
            buffer.addSql("? = ");
        }
        buffer.addSql("call %s(", this.info.sp);
        if (this.info.params != null && this.info.params.paramCount > 0) {
            String[] array = new String[this.info.params.paramCount];
            Arrays.fill(array, "default");
            for (CallParams.CallParam param : this.info.params.getParams()) {
                array[param.getIndex() - 1] = "?";
            }
            buffer.addSql(String.join(",", array));
        }
        buffer.addSql(")}");
        return buffer.getResult();
    }

    static class CallInfo {
        private String sp;
        private CallParams params;

        CallInfo(String sp) {
            this.sp = sp;
        }
    }
}
